package org.ubb.image_handler_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.ubb.image_handler_service.dto.ObjectInfoResponse;
import org.ubb.image_handler_service.dto.OperationType;
import org.ubb.image_handler_service.dto.interaction.InteractionContent;
import org.ubb.image_handler_service.dto.interaction.InteractionPreview;
import org.ubb.image_handler_service.dto.interaction.InteractionResponse;
import org.ubb.image_handler_service.dto.interaction.InteractionStatus;

import java.util.UUID;

public interface InteractionApi
{
    ResponseEntity<ObjectInfoResponse> createInteraction(String userId, OperationType operationType, MultipartFile image);

    ResponseEntity<Page<InteractionResponse>> getUserInteractions(String userId, Pageable pageable);

    ResponseEntity<InteractionPreview> getInteractionPreview(String userId, UUID interactionId);

    ResponseEntity<InteractionStatus> getInteractionStatus(String userId, UUID interactionId);

    ResponseEntity<InteractionContent> getInteractionResult(String userId, UUID interactionId);

    ResponseEntity<Void> deleteInteraction(String userId, UUID interactionId);
}
