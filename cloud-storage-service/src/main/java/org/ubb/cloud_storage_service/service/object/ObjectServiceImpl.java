package org.ubb.cloud_storage_service.service.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class ObjectServiceImpl implements ObjectService
{
    private static final Logger LOG = LoggerFactory.getLogger(ObjectServiceImpl.class);

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
        LOG.info("Will createObjectMetadata, request: {}", request);

        // Validate interaction ID
        UUID interactionId = request.getInteractionId();
        if (interactionId == null)
        {
            LOG.warn("Unable to createObjectMetadata, interaction id is null for request: {}", request);
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
        ObjectInfoResponse response = Converter.toObjectInfoResponse(savedObject);
        LOG.info("Done creatingObjectMetadata, response: {}", response);
        return response;
    }

    @Override
    public ObjectInfoResponse uploadObjectContent(String userId, UUID objectId, InputStream content)
    {
        LOG.info("Will uploadObjectContent, userId: {}, objectId: {}", userId, objectId);

        ObjectInfo objectInfo = getObjectInfoOrThrow(userId, objectId);

        if (objectInfo.getStatus() == EntityStatus.COMPLETED)
        {
            LOG.warn("Unable to uploadObjectContent, userId: {}, objectId: {} is already completed", userId, objectId);
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
        ObjectInfoResponse response = Converter.toObjectInfoResponse(objectInfo);
        LOG.info("Done uploadObjectContent, response: {}", response);
        return response;
    }

    @Override
    public ObjectInfoResponse createAndUploadObject(ObjectInfoRequest request, InputStream content)
    {
        LOG.info("Will createAndUploadObject, request: {}", request);
        ObjectInfoResponse metadataResponse = createObjectMetadata(request);
        ObjectInfoResponse response = uploadObjectContent(metadataResponse.getUserId(), metadataResponse.getObjectId(), content);
        LOG.info("Done createAndUploadObject, response: {}", response);
        return response;
    }

    @Override
    public Optional<ObjectInfoResponse> getObjectMetadata(String userId, UUID objectId)
    {
        LOG.info("Will getObjectMetadata, userId: {}, objectId: {}", userId, objectId);
        var response = objectInfoRepository.findByUserIdAndObjectId(userId, objectId)
                .map(Converter::toObjectInfoResponse);
        LOG.info("Done getObjectMetadata, userId: {}, objectId: {}, isPresent: {}", userId, objectId, response.isPresent());
        return response;
    }

    @Override
    public InputStream getObjectContent(String userId, UUID objectId, boolean isSimple)
    {
        LOG.info("Will getObjectContent, userId: {}, objectId: {}, isSimple: {}", userId, objectId, isSimple);
        ObjectInfo objectInfo = getObjectInfoOrThrow(userId, objectId);

        String objectName = objectInfo.getName();
        UUID interactionId = objectInfo.getInteraction().getInteractionId();

        if (isSimple)
        {
            String simpleObjectName = objectName + "-simple";
            if (objectStorageProvider.objectExists(userId, interactionId, simpleObjectName))
            {
                var response = objectStorageProvider.getObjectContent(userId, interactionId, simpleObjectName);
                LOG.info("Done simple getObjectContent, userId: {}, objectId: {}", userId, objectId);
                return response;
            }
        }
        var response = objectStorageProvider.getObjectContent(userId, interactionId, objectName);
        LOG.info("Done not-simple getObjectContent, userId: {}, objectId: {}", userId, objectId);
        return response;
    }

    @Override
    public ObjectInfoResponse updateObjectMetadata(String userId, UUID objectId, ObjectInfoRequest request)
    {
        LOG.info("Will updateObjectMetadata, userId: {}, objectId: {}, request: {}", userId, objectId, request);
        ObjectInfo objectInfo = getObjectInfoOrThrow(userId, objectId);

        // Update only tags
        Set<TagData> updatedTags = Converter.toTagDataSet(request.getTags());
        objectInfo.setTags(updatedTags);

        ObjectInfo updatedObject = objectInfoRepository.save(objectInfo);
        ObjectInfoResponse response = Converter.toObjectInfoResponse(updatedObject);
        LOG.info("Done updateObjectMetadata, userId: {}, objectId: {}, response: {}", userId, objectId, response);
        return response;
    }

    @Override
    public void deleteObject(String userId, UUID objectId)
    {
        LOG.info("Will deleteObject, userId: {}, objectId: {}", userId, objectId);
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
        LOG.info("Done deleteObject, userId: {}, objectId: {}", userId, objectId);
    }

    private ObjectInfo getObjectInfoOrThrow(String userId, UUID objectId)
    {
        return objectInfoRepository.findByUserIdAndObjectId(userId, objectId)
                .orElseThrow(() -> new ResourceNotFoundException("Object not found for userId: " + userId + " and objectId: " + objectId));
    }
}