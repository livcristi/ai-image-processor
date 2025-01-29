package org.ubb.image_handler_service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class TaskInfo implements Serializable
{
    private UUID taskId;

    private String userId;

    private UUID objectId;

    private UUID interactionId;

    private OperationType operationType; // BGR or OCR

    private String objectName;

    public TaskInfo()
    {
        // Empty constructor
    }

    private TaskInfo(Builder builder)
    {
        this.taskId = builder.taskId;
        this.userId = builder.userId;
        this.objectId = builder.objectId;
        this.interactionId = builder.interactionId;
        this.operationType = builder.operationType;
        this.objectName = builder.objectName;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private UUID taskId;
        private String userId;
        private UUID objectId;
        private UUID interactionId;
        private OperationType operationType;
        private String objectName;

        public Builder taskId(UUID taskId)
        {
            this.taskId = taskId;
            return this;
        }

        public Builder userId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public Builder objectId(UUID objectId)
        {
            this.objectId = objectId;
            return this;
        }

        public Builder containerId(UUID interactionId)
        {
            this.interactionId = interactionId;
            return this;
        }

        public Builder operationType(OperationType operationType)
        {
            this.operationType = operationType;
            return this;
        }

        public Builder objectName(String objectName)
        {
            this.objectName = objectName;
            return this;
        }

        public TaskInfo build()
        {
            return new TaskInfo(this);
        }
    }

    public UUID getTaskId()
    {
        return taskId;
    }

    public void setTaskId(UUID taskId)
    {
        this.taskId = taskId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public UUID getObjectId()
    {
        return objectId;
    }

    public void setObjectId(UUID objectId)
    {
        this.objectId = objectId;
    }

    public UUID getInteractionId()
    {
        return interactionId;
    }

    public void setInteractionId(UUID interactionId)
    {
        this.interactionId = interactionId;
    }

    public OperationType getOperationType()
    {
        return operationType;
    }

    public void setOperationType(OperationType operationType)
    {
        this.operationType = operationType;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInfo taskInfo = (TaskInfo) o;
        return Objects.equals(taskId, taskInfo.taskId) && Objects.equals(userId, taskInfo.userId) && Objects.equals(objectId, taskInfo.objectId) && Objects.equals(interactionId, taskInfo.interactionId) && operationType == taskInfo.operationType && Objects.equals(objectName, taskInfo.objectName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(taskId, userId, objectId, interactionId, operationType, objectName);
    }

    @Override
    public String toString()
    {
        return "TaskInfo{" +
                "taskId=" + taskId +
                ", userId=" + userId +
                ", objectId=" + objectId +
                ", interactionId='" + interactionId + '\'' +
                ", operationType=" + operationType +
                ", objectName='" + objectName + '\'' +
                '}';
    }
}