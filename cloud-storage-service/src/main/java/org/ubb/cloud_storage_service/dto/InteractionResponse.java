package org.ubb.cloud_storage_service.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class InteractionResponse
{
    private UUID interactionId;

    private String userId;

    private String status;

    private String operationType;

    private List<ObjectInfoResponse> objectInfoList;

    private Set<TagResponse> tags;

    public InteractionResponse()
    {
        // Empty constructor
    }

    public UUID getInteractionId()
    {
        return interactionId;
    }

    public void setInteractionId(UUID interactionId)
    {
        this.interactionId = interactionId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }

    public Set<TagResponse> getTags()
    {
        return tags;
    }

    public void setTags(Set<TagResponse> tags)
    {
        this.tags = tags;
    }

    public List<ObjectInfoResponse> getObjectInfoList()
    {
        return objectInfoList;
    }

    public void setObjectInfoList(List<ObjectInfoResponse> objectInfoList)
    {
        this.objectInfoList = objectInfoList;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionResponse that = (InteractionResponse) o;
        return Objects.equals(interactionId, that.interactionId) && Objects.equals(userId, that.userId) && Objects.equals(status, that.status) && Objects.equals(operationType, that.operationType) && Objects.equals(objectInfoList, that.objectInfoList) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(interactionId, userId, status, operationType, objectInfoList, tags);
    }

    @Override
    public String toString()
    {
        return "InteractionResponse{" +
                "interactionId=" + interactionId +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", operationType='" + operationType + '\'' +
                ", objectInfoList=" + objectInfoList +
                ", tags=" + tags +
                '}';
    }
}
