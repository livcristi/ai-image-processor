package org.ubb.cloud_storage_service.dto;

import java.util.Objects;
import java.util.UUID;

public class TagResponse
{
    private UUID tagId;

    private String key;

    private String value;

    public TagResponse()
    {
        // Empty constructor
    }

    public TagResponse(UUID tagId, String key, String value)
    {
        this.tagId = tagId;
        this.key = key;
        this.value = value;
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

    public UUID getTagId()
    {
        return tagId;
    }

    public void setTagId(UUID tagId)
    {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagResponse that = (TagResponse) o;
        return Objects.equals(tagId, that.tagId) && Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tagId, key, value);
    }

    @Override
    public String toString()
    {
        return "TagResponse{" +
                "tagId=" + tagId +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
