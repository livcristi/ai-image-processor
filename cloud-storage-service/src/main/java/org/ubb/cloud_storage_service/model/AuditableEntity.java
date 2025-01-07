package org.ubb.cloud_storage_service.model;

import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class AuditableEntity
{
    @CreationTimestamp
    private long createdTime;

    @UpdateTimestamp
    private long updatedTime;

    public long getCreatedTime()
    {
        return createdTime;
    }

    public long getUpdatedTime()
    {
        return updatedTime;
    }
}
