package org.ubb.cloud_storage_service.model;

import java.util.Objects;
import java.util.UUID;

public class AuditRecord
{
    private UUID recordId;

    private long timestamp;

    private EntityStatus status;

    private String objectId;

    private String objectName;

    private String userId;

    private String containerId;

    public AuditRecord()
    {
        // Empty
    }

    private AuditRecord(Builder builder)
    {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.objectId = builder.objectId;
        this.objectName = builder.objectName;
        this.userId = builder.userId;
        this.containerId = builder.containerId;
    }

    public static ObjectInfo.Builder builder()
    {
        return new ObjectInfo.Builder();
    }

    public static class Builder
    {
        private long timestamp;
        private EntityStatus status;
        private String objectId;
        private String objectName;
        private String userId;
        private String containerId;

        public Builder timestamp(long timestamp)
        {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(EntityStatus status)
        {
            this.status = status;
            return this;
        }

        public Builder objectId(String objectId)
        {
            this.objectId = objectId;
            return this;
        }

        public Builder objectName(String objectName)
        {
            this.objectName = objectName;
            return this;
        }

        public Builder userId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public Builder containerId(String containerId)
        {
            this.containerId = containerId;
            return this;
        }

        public AuditRecord build()
        {
            return new AuditRecord(this);
        }
    }

    public UUID getRecordId()
    {
        return recordId;
    }

    public void setRecordId(UUID recordId)
    {
        this.recordId = recordId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public EntityStatus getStatus()
    {
        return status;
    }

    public void setStatus(EntityStatus status)
    {
        this.status = status;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getContainerId()
    {
        return containerId;
    }

    public void setContainerId(String containerId)
    {
        this.containerId = containerId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditRecord that = (AuditRecord) o;
        return timestamp == that.timestamp && Objects.equals(recordId, that.recordId) && status == that.status && Objects.equals(objectId, that.objectId) && Objects.equals(objectName, that.objectName) && Objects.equals(userId, that.userId) && Objects.equals(containerId, that.containerId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(recordId, timestamp, status, objectId, objectName, userId, containerId);
    }

    @Override
    public String toString()
    {
        return "AuditRecord{" +
                "recordId=" + recordId +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", objectId=" + objectId +
                ", objectName='" + objectName + '\'' +
                ", userId=" + userId +
                ", containerId='" + containerId + '\'' +
                '}';
    }
}
