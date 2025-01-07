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
    private String key;

    @Column(nullable = false)
    private String value;

    public TagData()
    {
        // Empty
    }

    public TagData(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public UUID getTagId()
    {
        return tagId;
    }

    public void setTagId(UUID tagId)
    {
        this.tagId = tagId;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagData tagData = (TagData) o;
        return Objects.equals(tagId, tagData.tagId) && Objects.equals(key, tagData.key) && Objects.equals(value, tagData.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tagId, key, value);
    }

    @Override
    public String toString()
    {
        return "TagData{" +
                "tagId=" + tagId +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
