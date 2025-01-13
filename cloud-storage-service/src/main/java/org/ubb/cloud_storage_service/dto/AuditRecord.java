package org.ubb.cloud_storage_service.dto;

import org.ubb.cloud_storage_service.model.EntityStatus;

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
