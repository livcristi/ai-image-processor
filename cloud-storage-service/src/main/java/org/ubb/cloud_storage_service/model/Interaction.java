package org.ubb.cloud_storage_service.model;

import jakarta.persistence.*;

import java.util.*;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @OneToMany(mappedBy = "interaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectInfo> objectInfoList;

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

    public List<ObjectInfo> getObjectInfoList()
    {
        return objectInfoList;
    }

    public void setObjectInfoList(List<ObjectInfo> objectInfoList)
    {
        this.objectInfoList = objectInfoList;
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
        return Objects.equals(interactionId, that.interactionId) && Objects.equals(userId, that.userId) && status == that.status && operationType == that.operationType && Objects.equals(objectInfoList, that.objectInfoList) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(interactionId, userId, status, operationType, objectInfoList, tags);
    }

    @Override
    public String toString()
    {
        return "Interaction{" +
                "interactionId=" + interactionId +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", operationType=" + operationType +
                ", objectInfoList=" + objectInfoList +
                ", tags=" + tags +
                "} " + super.toString();
    }
}
