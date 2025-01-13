package org.ubb.cloud_storage_service.service.object;

import org.springframework.stereotype.Service;
import org.ubb.cloud_storage_service.dto.ObjectInfoRequest;
import org.ubb.cloud_storage_service.dto.ObjectInfoResponse;
import org.ubb.cloud_storage_service.exception.BadRequestException;
import org.ubb.cloud_storage_service.exception.ResourceNotFoundException;
import org.ubb.cloud_storage_service.model.EntityStatus;
import org.ubb.cloud_storage_service.model.Interaction;
import org.ubb.cloud_storage_service.model.ObjectInfo;
import org.ubb.cloud_storage_service.model.TagData;
import org.ubb.cloud_storage_service.repository.InteractionRepository;
import org.ubb.cloud_storage_service.repository.ObjectInfoRepository;
import org.ubb.cloud_storage_service.service.cloud.ObjectStorageProvider;
import org.ubb.cloud_storage_service.utils.Converter;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ObjectServiceImpl implements ObjectService
{
    private final ObjectInfoRepository objectInfoRepository;
    private final InteractionRepository interactionRepository;
    private final ObjectStorageProvider objectStorageProvider;

    public ObjectServiceImpl(ObjectInfoRepository objectInfoRepository,
                             InteractionRepository interactionRepository,
                             ObjectStorageProvider objectStorageProvider)
    {
        this.objectInfoRepository = objectInfoRepository;
        this.interactionRepository = interactionRepository;
        this.objectStorageProvider = objectStorageProvider;
    }

    @Override
    public ObjectInfoResponse createObjectMetadata(ObjectInfoRequest request)
    {
        // Validate interaction ID
        UUID interactionId = request.getInteractionId();
        if (interactionId == null)
        {
            throw new BadRequestException("Interaction ID must not be null for object metadata creation.");
        }

        Interaction interaction = interactionRepository.findById(interactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found for ID: " + interactionId));

        // Create ObjectInfo entity
        ObjectInfo objectInfo = Converter.toObjectInfo(request);
        objectInfo.setStatus(EntityStatus.CREATED);
        objectInfo.setInteraction(interaction);

        // Save and return response
        ObjectInfo savedObject = objectInfoRepository.save(objectInfo);
        return Converter.toObjectInfoResponse(savedObject);
    }

    @Override
    public ObjectInfoResponse uploadObjectContent(String userId, UUID objectId, InputStream content)
    {
        ObjectInfo objectInfo = getObjectInfoOrThrow(userId, objectId);

        if (objectInfo.getStatus() == EntityStatus.COMPLETED)
        {
            throw new BadRequestException("Content for the object has already been uploaded.");
        }

        // Upload content to cloud storage
        objectStorageProvider.uploadObjectContent(
                userId,
                objectInfo.getInteraction().getInteractionId(),
                objectInfo.getName(),
                content
        );

        // Update object status to COMPLETED
        objectInfo.setStatus(EntityStatus.COMPLETED);
        objectInfo = objectInfoRepository.save(objectInfo);
        return Converter.toObjectInfoResponse(objectInfo);
    }

    @Override
    public ObjectInfoResponse createAndUploadObject(ObjectInfoRequest request, InputStream content)
    {
        ObjectInfoResponse metadataResponse = createObjectMetadata(request);
        return uploadObjectContent(metadataResponse.getUserId(), metadataResponse.getObjectId(), content);
    }

    @Override
    public Optional<ObjectInfoResponse> getObjectMetadata(String userId, UUID objectId)
    {
        return objectInfoRepository.findByUserIdAndObjectId(userId, objectId)
                .map(Converter::toObjectInfoResponse);
    }

    @Override
    public InputStream getObjectContent(String userId, UUID objectId, boolean isSimple)
    {
        ObjectInfo objectInfo = getObjectInfoOrThrow(userId, objectId);

        String objectName = objectInfo.getName();
        UUID interactionId = objectInfo.getInteraction().getInteractionId();

        if (isSimple)
        {
            String simpleObjectName = objectName + "-simple";
            if (objectStorageProvider.objectExists(userId, interactionId, simpleObjectName))
            {
                return objectStorageProvider.getObjectContent(userId, interactionId, simpleObjectName);
            }
        }
        return objectStorageProvider.getObjectContent(userId, interactionId, objectName);
    }

    @Override
    public ObjectInfoResponse updateObjectMetadata(String userId, UUID objectId, ObjectInfoRequest request)
    {
        ObjectInfo objectInfo = getObjectInfoOrThrow(userId, objectId);

        // Update only tags
        Set<TagData> updatedTags = Converter.toTagDataSet(request.getTags());
        objectInfo.setTags(updatedTags);

        ObjectInfo updatedObject = objectInfoRepository.save(objectInfo);
        return Converter.toObjectInfoResponse(updatedObject);
    }

    @Override
    public void deleteObject(String userId, UUID objectId)
    {
        ObjectInfo objectInfo = getObjectInfoOrThrow(userId, objectId);
        String objectName = objectInfo.getName();
        UUID interactionId = objectInfo.getInteraction().getInteractionId();

        objectStorageProvider.deleteObject(
                userId,
                interactionId,
                objectName
        );

        objectInfoRepository.delete(objectInfo);

        // Also try to delete the thumbnail, if it exists
        String simpleObjectName = objectName + "-simple";
        if (objectStorageProvider.objectExists(userId, interactionId, simpleObjectName))
        {
            objectStorageProvider.deleteObject(userId, interactionId, simpleObjectName);
        }
    }

    private ObjectInfo getObjectInfoOrThrow(String userId, UUID objectId)
    {
        return objectInfoRepository.findByUserIdAndObjectId(userId, objectId)
                .orElseThrow(() -> new ResourceNotFoundException("Object not found for userId: " + userId + " and objectId: " + objectId));
    }
}