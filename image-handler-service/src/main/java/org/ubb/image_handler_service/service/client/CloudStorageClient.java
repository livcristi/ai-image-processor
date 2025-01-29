package org.ubb.image_handler_service.service.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.ubb.image_handler_service.config.CloudStorageProperties;
import org.ubb.image_handler_service.dto.ObjectInfoRequest;
import org.ubb.image_handler_service.dto.ObjectInfoResponse;
import org.ubb.image_handler_service.dto.interaction.InteractionRequest;
import org.ubb.image_handler_service.dto.interaction.InteractionResponse;
import org.ubb.image_handler_service.exception.FileRetrievalException;
import org.ubb.image_handler_service.utils.CustomPageImpl;

import java.io.IOException;
import java.util.UUID;

@Component
public class CloudStorageClient
{
    private final RestTemplate restTemplate;
    private final CloudStorageProperties cloudStorageProperties;

    public CloudStorageClient(RestTemplateBuilder restTemplateBuilder, CloudStorageProperties cloudStorageProperties)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.cloudStorageProperties = cloudStorageProperties;
    }

    // --- Interaction Endpoints ---

    public InteractionResponse createInteraction(InteractionRequest request)
    {
        String url = cloudStorageProperties.getBaseUrl() + "/api/interactions";
        return restTemplate.postForObject(url, request, InteractionResponse.class);
    }

    public InteractionResponse getInteraction(String userId, UUID interactionId)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "interactions", userId, interactionId.toString())
                .toUriString();
        return restTemplate.getForObject(url, InteractionResponse.class);
    }

    public Page<InteractionResponse> getUserInteractions(String userId, Pageable pageable)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "interactions", userId)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .toUriString();

        ResponseEntity<CustomPageImpl<InteractionResponse>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<CustomPageImpl<InteractionResponse>>()
                {
                });

        return response.getBody();
    }

    public InteractionResponse updateInteraction(String userId, UUID interactionId, InteractionRequest request)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "interactions", userId, interactionId.toString())
                .toUriString();

        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), InteractionResponse.class).getBody();
    }

    public void deleteInteraction(String userId, UUID interactionId)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "interactions", userId, interactionId.toString())
                .toUriString();

        restTemplate.delete(url);
    }

    // --- Object Endpoints ---

    public ObjectInfoResponse createObjectMetadata(ObjectInfoRequest request)
    {
        String url = cloudStorageProperties.getBaseUrl() + "/api/objects/metadata";
        return restTemplate.postForObject(url, request, ObjectInfoResponse.class);
    }

    public ObjectInfoResponse uploadObjectContent(String userId, UUID objectId, MultipartFile file)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "objects", userId, objectId.toString(), "content")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try
        {
            body.add("file", new ByteArrayResource(file.getBytes())
            {
                @Override
                public String getFilename()
                {
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e)
        {
            throw new FileRetrievalException("Unable to upload file content", e);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, requestEntity, ObjectInfoResponse.class);
    }

    public ObjectInfoResponse createAndUploadObject(String metadata, MultipartFile file)
    {
        String url = cloudStorageProperties.getBaseUrl() + "/api/objects";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("metadata", metadata);
        try
        {
            body.add("file", new ByteArrayResource(file.getBytes())
            {
                @Override
                public String getFilename()
                {
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e)
        {
            throw new FileRetrievalException("Unable to upload file content", e);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, requestEntity, ObjectInfoResponse.class);
    }

    public ObjectInfoResponse getObjectMetadata(String userId, UUID objectId)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "objects", userId, objectId.toString(), "metadata")
                .toUriString();

        return restTemplate.getForObject(url, ObjectInfoResponse.class);
    }

    public Resource getObjectContent(String userId, UUID objectId, boolean isSimple)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "objects", userId, objectId.toString(), "content")
                .queryParam("simple", isSimple)
                .toUriString();

        return restTemplate.exchange(url, HttpMethod.GET, null, Resource.class).getBody();
    }

    public ObjectInfoResponse updateObjectMetadata(String userId, UUID objectId, ObjectInfoRequest request)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "objects", userId, objectId.toString(), "metadata")
                .toUriString();

        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), ObjectInfoResponse.class).getBody();
    }

    public void deleteObject(String userId, UUID objectId)
    {
        String url = UriComponentsBuilder.fromUriString(cloudStorageProperties.getBaseUrl())
                .pathSegment("api", "objects", userId, objectId.toString())
                .toUriString();

        restTemplate.delete(url);
    }
}
