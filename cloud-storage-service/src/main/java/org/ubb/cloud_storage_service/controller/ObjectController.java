package org.ubb.cloud_storage_service.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ubb.cloud_storage_service.dto.ObjectInfoRequest;
import org.ubb.cloud_storage_service.dto.ObjectInfoResponse;
import org.ubb.cloud_storage_service.exception.BadRequestException;
import org.ubb.cloud_storage_service.exception.ResourceNotFoundException;
import org.ubb.cloud_storage_service.service.object.ObjectService;
import org.ubb.cloud_storage_service.utils.Converter;
import org.ubb.cloud_storage_service.utils.MediaTypeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/objects")
public class ObjectController implements ObjectApi
{
    private final ObjectService objectService;

    public ObjectController(ObjectService objectService)
    {
        this.objectService = objectService;
    }

    @Override
    @PostMapping("/metadata")
    public ResponseEntity<ObjectInfoResponse> createObjectMetadata(@RequestBody ObjectInfoRequest objectInfoRequest)
    {
        ObjectInfoResponse response = objectService.createObjectMetadata(objectInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/{userId}/{objectId}/content")
    public ResponseEntity<ObjectInfoResponse> uploadObjectContent(
            @PathVariable String userId,
            @PathVariable UUID objectId,
            @RequestParam("file") MultipartFile file)
    {
        try (InputStream contentStream = file.getInputStream())
        {
            ObjectInfoResponse response = objectService.uploadObjectContent(userId, objectId, contentStream);
            return ResponseEntity.ok(response);
        } catch (IOException e)
        {
            throw new BadRequestException("Unable to open file data. Please correct and try again");
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<ObjectInfoResponse> createAndUploadObject(
            @RequestParam("metadata") String metadata,
            @RequestParam("file") MultipartFile file)
    {
        try (InputStream contentStream = file.getInputStream())
        {
            ObjectInfoRequest objectInfoRequest = Converter.toObjectInfoRequest(metadata);
            ObjectInfoResponse response = objectService.createAndUploadObject(objectInfoRequest, contentStream);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e)
        {
            throw new BadRequestException("Unable to convert metadata to JSON. Please correct and try again");
        } catch (IOException e)
        {
            throw new BadRequestException("Unable to open file data. Please correct and try again");
        }
    }

    @Override
    @GetMapping("/{userId}/{objectId}/metadata")
    public ResponseEntity<ObjectInfoResponse> getObjectMetadata(
            @PathVariable String userId,
            @PathVariable UUID objectId)
    {
        return objectService.getObjectMetadata(userId, objectId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Object metadata not found"));
    }

    @Override
    @GetMapping("/{userId}/{objectId}/content")
    public ResponseEntity<Resource> getObjectContent(
            @PathVariable String userId,
            @PathVariable UUID objectId,
            @RequestParam(value = "simple", defaultValue = "false") boolean isSimple)
    {
        ObjectInfoResponse objectInfoData = objectService.getObjectMetadata(userId, objectId)
                .orElseThrow(() -> new ResourceNotFoundException("Object not found"));
        String contentType = MediaTypeUtils.inferContentType(objectInfoData.getName());

        InputStream contentStream = objectService.getObjectContent(userId, objectId, isSimple);
        Resource resource = new InputStreamResource(contentStream);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectInfoData.getName() + "\"")
                .body(resource);
    }

    @Override
    @PutMapping("/{userId}/{objectId}/metadata")
    public ResponseEntity<ObjectInfoResponse> updateObjectMetadata(
            @PathVariable String userId,
            @PathVariable UUID objectId,
            @RequestBody ObjectInfoRequest objectInfoRequest)
    {
        ObjectInfoResponse response = objectService.updateObjectMetadata(userId, objectId, objectInfoRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{userId}/{objectId}")
    public ResponseEntity<Void> deleteObject(@PathVariable String userId, @PathVariable UUID objectId)
    {
        objectService.deleteObject(userId, objectId);
        return ResponseEntity.noContent().build();
    }
}
