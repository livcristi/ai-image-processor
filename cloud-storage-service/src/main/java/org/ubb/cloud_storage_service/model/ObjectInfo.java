package org.ubb.cloud_storage_service.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "object_info")
public class ObjectInfo extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "object_id")
    private UUID objectId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String container;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityStatus status;

    @Column(nullable = false)
    private String type; // img or text

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "object_tags",
            joinColumns = @JoinColumn(name = "object_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagData> tags = new HashSet<>();

    public ObjectInfo()
    {
        // Empty constructor
    }

    private ObjectInfo(Builder builder)
    {
        this.userId = builder.userId;
        this.name = builder.name;
        this.container = builder.container;
        this.status = builder.status;
        this.type = builder.type;
        this.tags = builder.tags;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private String userId;
        private String name;
        private String container;
        private EntityStatus status;
        private String type;
        private Set<TagData> tags = new HashSet<>();

        public Builder userId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder container(String container)
        {
            this.container = container;
            return this;
        }

        public Builder status(EntityStatus status)
        {
            this.status = status;
            return this;
        }

        public Builder type(String type)
        {
            this.type = type;
            return this;
        }

        public Builder tags(Set<TagData> tags)
        {
            this.tags = tags;
            return this;
        }

        public ObjectInfo build()
        {
            return new ObjectInfo(this);
        }
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

    public String getContainer()
    {
        return container;
    }

    public void setContainer(String container)
    {
        this.container = container;
    }

    public EntityStatus getStatus()
    {
        return status;
    }

    public void setStatus(EntityStatus status)
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

    public Set<TagData> getTags()
    {
        return tags;
    }

    public void setTags(Set<TagData> tags)
    {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectInfo that = (ObjectInfo) o;
        return Objects.equals(objectId, that.objectId) && Objects.equals(userId, that.userId) && Objects.equals(name, that.name) && Objects.equals(container, that.container) && status == that.status && Objects.equals(type, that.type) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(objectId, userId, name, container, status, type, tags);
    }

    @Override
    public String toString()
    {
        return "ObjectInfo{" +
                "objectId=" + objectId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", container='" + container + '\'' +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", tags=" + tags +
                "} " + super.toString();
    }
}
