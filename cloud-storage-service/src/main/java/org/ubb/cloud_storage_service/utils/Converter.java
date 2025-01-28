package org.ubb.cloud_storage_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.ubb.cloud_storage_service.dto.*;
import org.ubb.cloud_storage_service.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter
{
    private static final ObjectMapper mapper = new ObjectMapper();

    // Convert InteractionRequest to Interaction model
    public static Interaction toInteraction(InteractionRequest request)
    {
        Interaction interaction = new Interaction();
        interaction.setUserId(request.getUserId());
        if (StringUtils.isNotBlank(request.getStatus()))
        {
            interaction.setStatus(EntityStatus.valueOf(request.getStatus()));
        } else
        {
            interaction.setStatus(EntityStatus.CREATED);
        }
        if (StringUtils.isNotBlank(request.getOperationType()))
        {
            interaction.setOperationType(OperationType.valueOf(request.getOperationType()));
        } else
        {
            interaction.setOperationType(OperationType.BGR);
        }
        interaction.setTags(toTagDataSet(request.getTags()));

        return interaction;
    }

    // Convert Interaction model to InteractionResponse
    public static InteractionResponse toInteractionResponse(Interaction interaction)
    {
        InteractionResponse response = new InteractionResponse();
        response.setInteractionId(interaction.getInteractionId());
        response.setUserId(interaction.getUserId());
        response.setStatus(interaction.getStatus().name());
        response.setOperationType(interaction.getOperationType().name());

        if (interaction.getObjectInfoList() != null)
        {
            List<ObjectInfoResponse> objectInfoList = interaction.getObjectInfoList().stream()
                    .map(Converter::toObjectInfoResponse)
                    .collect(Collectors.toList());
            response.setObjectInfoList(objectInfoList);
        } else
        {
            response.setObjectInfoList(List.of());
        }

        response.setTags(toTagResponseSet(interaction.getTags()));

        return response;
    }

    public static ObjectInfoRequest toObjectInfoRequest(String jsonData) throws JsonProcessingException
    {
        return Converter.mapper.readValue(jsonData, ObjectInfoRequest.class);
    }

    // Convert ObjectInfoRequest to ObjectInfo model
    public static ObjectInfo toObjectInfo(ObjectInfoRequest request)
    {
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setUserId(request.getUserId());
        objectInfo.setName(request.getName());
        if (StringUtils.isNotBlank(request.getStatus()))
        {
            objectInfo.setStatus(EntityStatus.valueOf(request.getStatus()));
        } else
        {
            objectInfo.setStatus(EntityStatus.CREATED);
        }
        objectInfo.setType(request.getType());

        Interaction interaction = new Interaction();
        interaction.setInteractionId(request.getInteractionId());
        objectInfo.setInteraction(interaction);

        objectInfo.setTags(toTagDataSet(request.getTags()));

        return objectInfo;
    }

    // Convert ObjectInfo model to ObjectInfoResponse
    public static ObjectInfoResponse toObjectInfoResponse(ObjectInfo objectInfo)
    {
        ObjectInfoResponse response = new ObjectInfoResponse();
        response.setObjectId(objectInfo.getObjectId());
        response.setUserId(objectInfo.getUserId());
        response.setName(objectInfo.getName());
        response.setStatus(objectInfo.getStatus().name());
        response.setType(objectInfo.getType());
        response.setInteractionId(objectInfo.getInteraction().getInteractionId());
        response.setTags(toTagResponseSet(objectInfo.getTags()));

        return response;
    }

    // Convert TagRequest to TagData model
    public static TagData toTagModel(TagRequest request)
    {
        TagData tag = new TagData();
        tag.setTagKey(request.getKey());
        tag.setTagValue(request.getValue());
        return tag;
    }

    // Convert TagData model to TagResponse
    public static TagResponse toTagResponse(TagData tagData)
    {
        TagResponse response = new TagResponse();
        response.setTagId(tagData.getTagId());
        response.setKey(tagData.getTagKey());
        response.setValue(tagData.getTagValue());
        return response;
    }

    // Convert set of TagRequest to set of TagData
    public static Set<TagData> toTagDataSet(Set<TagRequest> tags)
    {
        if (tags == null)
        {
            return Set.of();
        }
        return tags.stream()
                .map(Converter::toTagModel)
                .collect(Collectors.toSet());
    }

    // Convert set of TagData to set of TagResponse
    public static Set<TagResponse> toTagResponseSet(Set<TagData> tags)
    {
        return tags.stream()
                .map(Converter::toTagResponse)
                .collect(Collectors.toSet());
    }
}
