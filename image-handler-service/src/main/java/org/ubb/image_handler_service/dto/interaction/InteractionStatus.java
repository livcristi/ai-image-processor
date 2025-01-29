package org.ubb.image_handler_service.dto.interaction;

import java.util.Objects;
import java.util.UUID;

public class InteractionStatus
{
    private UUID interactionId;
    private String userId;
    private String status;

    public InteractionStatus()
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionStatus that = (InteractionStatus) o;
        return Objects.equals(interactionId, that.interactionId) && Objects.equals(userId, that.userId) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(interactionId, userId, status);
    }

    @Override
    public String toString()
    {
        return "InteractionStatus{" +
                "interactionId=" + interactionId +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                '}';
    }
}
