package org.ubb.cloud_storage_service.service.object;

import org.ubb.cloud_storage_service.dto.ObjectInfoRequest;
import org.ubb.cloud_storage_service.dto.ObjectInfoResponse;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

public interface ObjectService
{
    ObjectInfoResponse createObjectMetadata(ObjectInfoRequest objectInfo);

    ObjectInfoResponse uploadObjectContent(String userId, UUID objectId, InputStream content);

    ObjectInfoResponse createAndUploadObject(ObjectInfoRequest objectInfo, InputStream content);

    Optional<ObjectInfoResponse> getObjectMetadata(String userId, UUID objectId);

    InputStream getObjectContent(String userId, UUID objectId, boolean isSimple);

    ObjectInfoResponse updateObjectMetadata(String userId, UUID objectId, ObjectInfoRequest objectInfo);

    void deleteObject(String userId, UUID objectId);
}
