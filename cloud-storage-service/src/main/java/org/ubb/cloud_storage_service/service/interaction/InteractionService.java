package org.ubb.cloud_storage_service.service.interaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ubb.cloud_storage_service.dto.InteractionRequest;
import org.ubb.cloud_storage_service.dto.InteractionResponse;

import java.util.Optional;
import java.util.UUID;

public interface InteractionService
{
    // Create a new interaction and return the response
    InteractionResponse createInteraction(InteractionRequest interaction);

    // Get a specific interaction by userId and interactionId
    Optional<InteractionResponse> getInteraction(String userId, UUID interactionId);

    // Get paginated user interactions
    Page<InteractionResponse> getUserInteractions(String userId, Pageable pageable);

    // Update an existing interaction and return the updated response
    InteractionResponse updateInteraction(String userId, UUID interactionId, InteractionRequest interaction);

    // Delete an interaction by userId and interactionId
    void deleteInteraction(String userId, UUID interactionId);
}
