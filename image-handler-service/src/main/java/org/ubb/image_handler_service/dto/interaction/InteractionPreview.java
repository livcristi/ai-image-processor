package org.ubb.image_handler_service.dto.interaction;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class InteractionPreview
{
    private UUID interactionId;
    private String userId;
    private String status;
    private String operationType;
    private byte[] inputImage;

    public InteractionPreview()
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

    public byte[] getInputImage()
    {
        return inputImage;
    }

    public void setInputImage(byte[] inputImage)
    {
        this.inputImage = inputImage;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionPreview that = (InteractionPreview) o;
        return Objects.equals(interactionId, that.interactionId) && Objects.equals(userId, that.userId) && Objects.equals(status, that.status) && Objects.equals(operationType, that.operationType) && Objects.deepEquals(inputImage, that.inputImage);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(interactionId, userId, status, operationType, Arrays.hashCode(inputImage));
    }

    @Override
    public String toString()
    {
        return "InteractionPreview{" +
                "interactionId=" + interactionId +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", operationType='" + operationType + '\'' +
                ", inputImage=" + Arrays.toString(inputImage) +
                '}';
    }
}
