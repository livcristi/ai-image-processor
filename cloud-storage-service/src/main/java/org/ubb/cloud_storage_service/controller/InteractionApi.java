package org.ubb.cloud_storage_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.ubb.cloud_storage_service.dto.InteractionRequest;
import org.ubb.cloud_storage_service.dto.InteractionResponse;

import java.util.UUID;

public interface InteractionApi
{
    ResponseEntity<InteractionResponse> createInteraction(InteractionRequest interactionRequest);

    ResponseEntity<InteractionResponse> getInteraction(String userId, UUID interactionId);

    ResponseEntity<Page<InteractionResponse>> getUserInteractions(String userId, Pageable pageable);

    ResponseEntity<InteractionResponse> updateInteraction(String userId, UUID interactionId, InteractionRequest interactionRequest);

    ResponseEntity<Void> deleteInteraction(String userId, UUID interactionId);
}
