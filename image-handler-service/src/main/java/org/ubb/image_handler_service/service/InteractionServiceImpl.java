package org.ubb.image_handler_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ubb.image_handler_service.dto.*;
import org.ubb.image_handler_service.dto.interaction.*;
import org.ubb.image_handler_service.exception.ObjectNotFoundException;
import org.ubb.image_handler_service.exception.ProcessingException;
import org.ubb.image_handler_service.service.client.CloudStorageClient;

import java.util.Set;
import java.util.UUID;

import static org.ubb.image_handler_service.utils.Converters.*;

@Service
public class InteractionServiceImpl implements InteractionService
{
    private static final Logger LOG = LoggerFactory.getLogger(InteractionServiceImpl.class);
    public static final String INPUT_TAG_KEY = "isInput";

    private final CloudStorageClient cloudStorageClient;
    private final RabbitMqProducer rabbitMqProducer;

    public InteractionServiceImpl(CloudStorageClient cloudStorageClient, RabbitMqProducer rabbitMqProducer)
    {
        this.cloudStorageClient = cloudStorageClient;
        this.rabbitMqProducer = rabbitMqProducer;
    }

    @Override
    public ObjectInfoResponse createInteraction(String userId, OperationType operationType, MultipartFile image)
    {
        LOG.info("Will createInteraction, userId: {}, operationType: {}, imageType: {}", userId, operationType, image.getContentType());
        // Create the interaction
        InteractionRequest interactionRequest = new InteractionRequest();
        interactionRequest.setUserId(userId);
        interactionRequest.setOperationType(operationType.toString());
        InteractionResponse createdInteraction = cloudStorageClient.createInteraction(interactionRequest);

        // Create and upload the image
        ObjectInfoRequest objectMetadataRequest = new ObjectInfoRequest();
        objectMetadataRequest.setUserId(userId);
        objectMetadataRequest.setName(image.getName());
        objectMetadataRequest.setType(image.getContentType());
        objectMetadataRequest.setInteractionId(createdInteraction.getInteractionId());
        objectMetadataRequest.setTags(Set.of(new TagRequest(INPUT_TAG_KEY, "true")));
        String metadata;
        try
        {
            metadata = toJsonString(objectMetadataRequest);
        } catch (JsonProcessingException e)
        {
            LOG.error("Unable to convert object metadata to json string, userId: {}, interactionId: {}", userId, createdInteraction.getInteractionId(), e);
            throw new ProcessingException("Unable to create metadata for image", e);
        }
        ObjectInfoResponse response = cloudStorageClient.createAndUploadObject(metadata, image);

        // Add the task to rabbitmq
        TaskInfo taskInfo = toTaskInfo(createdInteraction, response);
        rabbitMqProducer.sendTask(taskInfo);

        // Return a response with the metadata of the uploaded image
        LOG.info("Done createInteraction, userId: {}, interactionId: {}, objectId: {}", userId, createdInteraction.getInteractionId(), response.getObjectId());
        return response;
    }

    @Override
    public Page<InteractionResponse> getUserInteractions(String userId, Pageable pageable)
    {
        LOG.info("Will getUserInteractions, userId: {}, pageable: {}", userId, pageable);
        var result = cloudStorageClient.getUserInteractions(userId, pageable);
        LOG.info("Done getUserInteractions, userId: {}, pageable: {}", userId, pageable);
        return result;
    }

    @Override
    public InteractionPreview getInteractionPreview(String userId, UUID interactionId)
    {
        LOG.info("Will getInteractionPreview, userId: {}, interactionId: {}", userId, interactionId);
        // Get the interaction metadata
        InteractionResponse interactionResponse = cloudStorageClient.getInteraction(userId, interactionId);
        // Search for the metadata of the input image
        ObjectInfoResponse inputImage = interactionResponse.getObjectInfoList()
                .stream()
                .filter(objectInfoResponse -> objectInfoResponse.getTags().stream().anyMatch(tag -> INPUT_TAG_KEY.equals(tag.getKey())))
                .findFirst().orElseThrow(() -> new ObjectNotFoundException("Unable to find input image for interaction"));
        // Get the image content (simple) and return the response
        Resource imageContent = cloudStorageClient.getObjectContent(userId, inputImage.getObjectId(), true);
        InteractionPreview preview = toInteractionPreview(interactionResponse, imageContent);
        LOG.info("Done getInteractionPreview, userId: {}, interactionId: {}", userId, interactionId);
        return preview;
    }

    @Override
    public InteractionStatus getInteractionStatus(String userId, UUID interactionId)
    {
        LOG.info("Will getInteractionStatus, userId: {}, interactionId: {}", userId, interactionId);
        InteractionResponse interactionResponse = cloudStorageClient.getInteraction(userId, interactionId);
        InteractionStatus interactionStatus = toInteractionStatus(interactionResponse);
        LOG.info("Done getInteractionStatus, userId: {}, interactionId: {}, status: {}", userId, interactionId, interactionStatus);
        return interactionStatus;
    }

    @Override
    public InteractionContent getInteractionResult(String userId, UUID interactionId)
    {
        LOG.info("Will getInteractionResult, userId: {}, interactionId: {}", userId, interactionId);
        // Get the interaction metadata
        InteractionResponse interactionResponse = cloudStorageClient.getInteraction(userId, interactionId);
        // Get the objects metadata
        ObjectInfoResponse inputImageMetadata = interactionResponse.getObjectInfoList()
                .stream()
                .filter(objectInfoResponse -> objectInfoResponse.getTags().stream().anyMatch(tag -> "isInput".equals(tag.getKey())))
                .findFirst().orElseThrow(() -> new ObjectNotFoundException("Unable to find input image for interaction"));
        ObjectInfoResponse resultMetadata = interactionResponse.getObjectInfoList()
                .stream()
                .filter(objectInfoResponse -> objectInfoResponse.getTags().stream().noneMatch(tag -> "isInput".equals(tag.getKey())))
                .findFirst().orElseThrow(() -> new ObjectNotFoundException("Unable to find result for interaction"));
        // Get the content of the input and of the result
        LOG.info("Will get input and result content, userId: {}, interactionId: {}, inputId:{}, resultId: {}", userId,
                interactionId, inputImageMetadata.getObjectId(), resultMetadata.getObjectId());
        Resource imageContent = cloudStorageClient.getObjectContent(userId, inputImageMetadata.getObjectId(), false);
        Resource resultContent = cloudStorageClient.getObjectContent(userId, resultMetadata.getObjectId(), false);
        InteractionContent result = toInteractionContent(interactionResponse, inputImageMetadata, resultMetadata, imageContent, resultContent);
        LOG.info("Done getInteractionResult, userId: {}, interactionId: {}", userId, interactionId);
        return result;
    }

    @Override
    public void deleteInteraction(String userId, UUID interactionId)
    {
        LOG.info("Will deleteInteraction, userId: {}, interactionId: {}", userId, interactionId);
        InteractionResponse interactionResponse = cloudStorageClient.getInteraction(userId, interactionId);
        interactionResponse.getObjectInfoList()
                        .forEach(objectInfoResponse -> cloudStorageClient.deleteObject(userId, objectInfoResponse.getObjectId()));
        cloudStorageClient.deleteInteraction(userId, interactionId);
        LOG.info("Done deleteInteraction, userId: {}, interactionId: {}", userId, interactionId);
    }
}
