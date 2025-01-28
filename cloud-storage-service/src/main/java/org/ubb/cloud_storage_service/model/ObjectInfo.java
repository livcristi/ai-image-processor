package org.ubb.cloud_storage_service.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityStatus status;

    @Column(nullable = false)
    private String type; // img or text

    @ManyToOne
    @JoinColumn(name = "interaction_id", nullable = false)
    private Interaction interaction;

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

    public UUID getObjectId()
    {
        return objectId;
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

    public Interaction getInteraction()
    {
        return interaction;
    }

    public void setInteraction(Interaction interaction)
    {
        this.interaction = interaction;
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
        return Objects.equals(objectId, that.objectId) && Objects.equals(userId, that.userId) && Objects.equals(name, that.name) && status == that.status && Objects.equals(type, that.type) && Objects.equals(interaction, that.interaction) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(objectId, userId, name, status, type, interaction, tags);
    }

    @Override
    public String toString()
    {
        return "ObjectInfo{" +
                "objectId=" + objectId +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", interaction=" + interaction +
                ", tags=" + tags +
                "} " + super.toString();
    }
}
