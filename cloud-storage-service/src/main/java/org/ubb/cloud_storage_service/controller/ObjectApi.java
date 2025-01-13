package org.ubb.cloud_storage_service.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.ubb.cloud_storage_service.dto.ObjectInfoRequest;
import org.ubb.cloud_storage_service.dto.ObjectInfoResponse;

import java.util.UUID;

public interface ObjectApi
{
    ResponseEntity<ObjectInfoResponse> createObjectMetadata(ObjectInfoRequest objectInfoRequest);

    ResponseEntity<ObjectInfoResponse> uploadObjectContent(String userId, UUID objectId, MultipartFile file);

    ResponseEntity<ObjectInfoResponse> createAndUploadObject(String metadata, MultipartFile file);

    ResponseEntity<ObjectInfoResponse> getObjectMetadata(String userId, UUID objectId);

    ResponseEntity<Resource> getObjectContent(String userId, UUID objectId, boolean isSimple);

    ResponseEntity<ObjectInfoResponse> updateObjectMetadata(String userId, UUID objectId, ObjectInfoRequest objectInfoRequest);

    ResponseEntity<Void> deleteObject(String userId, UUID objectId);
}
