package org.ubb.cloud_storage_service.dto;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;


public class ObjectInfoResponse
{
    private UUID objectId;

    private String userId;

    private String name;

    private String status;

    private String type;

    private UUID interactionId;

    private Set<TagResponse> tags;

    public ObjectInfoResponse()
    {
        // Empty constructor
    }

    public UUID getObjectId()
    {
        return objectId;
    }

    public void setObjectId(UUID objectId)
    {
        this.objectId = objectId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public UUID getInteractionId()
    {
        return interactionId;
    }

    public void setInteractionId(UUID interactionId)
    {
        this.interactionId = interactionId;
    }

    public Set<TagResponse> getTags()
    {
        return tags;
    }

    public void setTags(Set<TagResponse> tags)
    {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectInfoResponse that = (ObjectInfoResponse) o;
        return Objects.equals(objectId, that.objectId) && Objects.equals(userId, that.userId) && Objects.equals(name, that.name) && Objects.equals(status, that.status) && Objects.equals(type, that.type) && Objects.equals(interactionId, that.interactionId) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(objectId, userId, name, status, type, interactionId, tags);
    }

    @Override
    public String toString()
    {
        return "ObjectInfoResponse{" +
                "objectId=" + objectId +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", interactionId=" + interactionId +
                ", tags=" + tags +
                '}';
    }
}
