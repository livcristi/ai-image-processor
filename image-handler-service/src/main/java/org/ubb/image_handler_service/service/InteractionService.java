package org.ubb.image_handler_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.ubb.image_handler_service.dto.ObjectInfoResponse;
import org.ubb.image_handler_service.dto.OperationType;
import org.ubb.image_handler_service.dto.interaction.InteractionContent;
import org.ubb.image_handler_service.dto.interaction.InteractionPreview;
import org.ubb.image_handler_service.dto.interaction.InteractionResponse;
import org.ubb.image_handler_service.dto.interaction.InteractionStatus;

import java.util.UUID;

public interface InteractionService
{
    ObjectInfoResponse createInteraction(String userId, OperationType operationType, MultipartFile image);

    Page<InteractionResponse> getUserInteractions(String userId, Pageable pageable);

    InteractionPreview getInteractionPreview(String userId, UUID interactionId);

    InteractionStatus getInteractionStatus(String userId, UUID interactionId);

    InteractionContent getInteractionResult(String userId, UUID interactionId);

    void deleteInteraction(String userId, UUID interactionId);
}
