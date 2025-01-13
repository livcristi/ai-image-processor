package org.ubb.cloud_storage_service.dto;

import java.util.Objects;
import java.util.Set;

public class InteractionRequest
{
    private String userId;

    private String status;

    private String operationType;

    private Set<TagRequest> tags;

    public InteractionRequest()
    {
        // Empty constructor
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

    public Set<TagRequest> getTags()
    {
        return tags;
    }

    public void setTags(Set<TagRequest> tags)
    {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionRequest that = (InteractionRequest) o;
        return Objects.equals(userId, that.userId) && Objects.equals(status, that.status) && Objects.equals(operationType, that.operationType) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userId, status, operationType, tags);
    }

    @Override
    public String toString()
    {
        return "InteractionRequest{" +
                "userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", operationType='" + operationType + '\'' +
                ", tags=" + tags +
                '}';
    }
}
