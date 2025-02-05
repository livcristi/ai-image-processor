package org.ubb.image_handler_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.ubb.image_handler_service.dto.ObjectInfoResponse;
import org.ubb.image_handler_service.dto.OperationType;
import org.ubb.image_handler_service.dto.TaskInfo;
import org.ubb.image_handler_service.dto.interaction.InteractionContent;
import org.ubb.image_handler_service.dto.interaction.InteractionPreview;
import org.ubb.image_handler_service.dto.interaction.InteractionResponse;
import org.ubb.image_handler_service.dto.interaction.InteractionStatus;
import org.ubb.image_handler_service.exception.FileRetrievalException;

import java.io.IOException;
import java.util.UUID;

public class Converters
{
    private final static ObjectMapper mapper = new ObjectMapper();

    public static InteractionPreview toInteractionPreview(InteractionResponse interactionResponse, Resource content)
    {
        InteractionPreview interactionPreview = new InteractionPreview();
        interactionPreview.setInteractionId(interactionResponse.getInteractionId());
        interactionPreview.setUserId(interactionResponse.getUserId());
        interactionPreview.setStatus(interactionResponse.getStatus());
        interactionPreview.setOperationType(interactionResponse.getOperationType());
        try
        {
            interactionPreview.setInputImage(content.getContentAsByteArray());
        } catch (IOException e)
        {
            throw new FileRetrievalException("Unable to get the file image content", e);
        }
        return interactionPreview;
    }

    public static InteractionStatus toInteractionStatus(InteractionResponse interactionResponse)
    {
        InteractionStatus interactionStatus = new InteractionStatus();
        interactionStatus.setInteractionId(interactionResponse.getInteractionId());
        interactionStatus.setUserId(interactionResponse.getUserId());
        interactionStatus.setStatus(interactionResponse.getStatus());
        return interactionStatus;
    }

    public static InteractionContent toInteractionContent(InteractionResponse interactionResponse,
                                                          ObjectInfoResponse inputMetadata, ObjectInfoResponse resultMetadata,
                                                          Resource inputContent, Resource resultContent)
    {
        InteractionContent interactionContent = new InteractionContent();
        interactionContent.setInteractionMetadata(interactionResponse);
        interactionContent.setInputMetadata(inputMetadata);
        interactionContent.setResultMetadata(resultMetadata);
        try
        {
            interactionContent.setInputImage(inputContent.getContentAsByteArray());
            interactionContent.setResultData(resultContent.getContentAsByteArray());
        } catch (IOException e)
        {
            throw new FileRetrievalException("Unable to get the file content", e);
        }
        return interactionContent;
    }

    public static TaskInfo toTaskInfo(InteractionResponse interactionResponse, ObjectInfoResponse objectInfoResponse)
    {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(UUID.randomUUID());
        taskInfo.setUserId(interactionResponse.getUserId());
        taskInfo.setObjectId(objectInfoResponse.getObjectId());
        taskInfo.setInteractionId(interactionResponse.getInteractionId());
        taskInfo.setOperationType(OperationType.valueOf(interactionResponse.getOperationType()));
        taskInfo.setObjectName(objectInfoResponse.getName());
        return taskInfo;
    }

    public static String toJsonString(Object object) throws JsonProcessingException
    {
        return mapper.writeValueAsString(object);
    }
}
