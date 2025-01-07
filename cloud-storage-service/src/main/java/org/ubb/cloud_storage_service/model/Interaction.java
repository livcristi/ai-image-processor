package org.ubb.cloud_storage_service.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "interaction")
public class Interaction extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID interactionId;

    @Column(nullable = false)
    private String userId;

    @Column
    private String container;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "interaction_tags",
            joinColumns = @JoinColumn(name = "interaction_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagData> tags = new HashSet<>();

    public Interaction()
    {
        // Empty constructor
    }

    public Interaction(Builder builder)
    {
        this.userId = builder.userId;
        this.container = builder.container;
        this.status = builder.status;
        this.operationType = builder.operationType;
        this.tags = builder.tags;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private String userId;
        private String container;
        private EntityStatus status;
        private OperationType operationType;
        private Set<TagData> tags = new HashSet<>();

        public Builder userId(String userId)
        {
            this.userId = userId;
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

        public Builder operationType(OperationType operationType)
        {
            this.operationType = operationType;
            return this;
        }

        public Builder tags(Set<TagData> tags)
        {
            this.tags = tags;
            return this;
        }

        public Interaction build() {
            return new Interaction(this);
        }
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

    public OperationType getOperationType()
    {
        return operationType;
    }

    public void setOperationType(OperationType operationType)
    {
        this.operationType = operationType;
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
        Interaction that = (Interaction) o;
        return Objects.equals(interactionId, that.interactionId) && Objects.equals(userId, that.userId) && Objects.equals(container, that.container) && status == that.status && operationType == that.operationType && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(interactionId, userId, container, status, operationType, tags);
    }

    @Override
    public String toString()
    {
        return "Interaction{" +
                "interactionId=" + interactionId +
                ", userId=" + userId +
                ", container='" + container + '\'' +
                ", status=" + status +
                ", operationType=" + operationType +
                ", tags=" + tags +
                "} " + super.toString();
    }
}
