package org.ubb.cloud_storage_service.service.cloud;

import java.io.InputStream;
import java.util.UUID;

public interface ObjectStorageProvider
{
    void uploadObjectContent(String userId, UUID interactionId, String objectName, InputStream content);

    InputStream getObjectContent(String userId, UUID interactionId, String objectName);

    void deleteObject(String userId, UUID interactionId, String objectName);

    boolean objectExists(String userId, UUID interactionId, String objectName);
}
