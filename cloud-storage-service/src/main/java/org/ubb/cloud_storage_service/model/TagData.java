package org.ubb.cloud_storage_service.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tag_data")
public class TagData
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID tagId;

    @Column(nullable = false)
    private String tagKey;

    @Column(nullable = false)
    private String tagValue;

    public TagData()
    {
        // Empty
    }

    public TagData(String tagKey, String tagValue)
    {
        this.tagKey = tagKey;
        this.tagValue = tagValue;
    }

    public UUID getTagId()
    {
        return tagId;
    }

    public void setTagId(UUID tagId)
    {
        this.tagId = tagId;
    }

    public String getTagKey()
    {
        return tagKey;
    }

    public void setTagKey(String key)
    {
        this.tagKey = key;
    }

    public String getTagValue()
    {
        return tagValue;
    }

    public void setTagValue(String value)
    {
        this.tagValue = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagData tagData = (TagData) o;
        return Objects.equals(tagId, tagData.tagId) && Objects.equals(tagKey, tagData.tagKey) && Objects.equals(tagValue, tagData.tagValue);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tagId, tagKey, tagValue);
    }

    @Override
    public String toString()
    {
        return "TagData{" +
                "tagId=" + tagId +
                ", key='" + tagKey + '\'' +
                ", value='" + tagValue + '\'' +
                '}';
    }
}
