package org.ubb.cloud_storage_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ubb.cloud_storage_service.dto.InteractionRequest;
import org.ubb.cloud_storage_service.dto.InteractionResponse;
import org.ubb.cloud_storage_service.exception.ResourceNotFoundException;
import org.ubb.cloud_storage_service.service.interaction.InteractionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController implements InteractionApi
{
    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService)
    {
        this.interactionService = interactionService;
    }

    @Override
    @PostMapping
    public ResponseEntity<InteractionResponse> createInteraction(@RequestBody InteractionRequest interactionRequest)
    {
        InteractionResponse response = interactionService.createInteraction(interactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/{userId}/{interactionId}")
    public ResponseEntity<InteractionResponse> getInteraction(
            @PathVariable String userId,
            @PathVariable UUID interactionId)
    {
        return interactionService.getInteraction(userId, interactionId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found"));
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<Page<InteractionResponse>> getUserInteractions(@PathVariable String userId,
                                                                         @PageableDefault(size = 5, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<InteractionResponse> interactions = interactionService.getUserInteractions(userId, pageable);
        return ResponseEntity.ok(interactions);
    }

    @Override
    @PutMapping("/{userId}/{interactionId}")
    public ResponseEntity<InteractionResponse> updateInteraction(
            @PathVariable String userId,
            @PathVariable UUID interactionId,
            @RequestBody InteractionRequest interactionRequest)
    {
        InteractionResponse updatedResponse = interactionService.updateInteraction(userId, interactionId, interactionRequest);
        return ResponseEntity.ok(updatedResponse);
    }

    @Override
    @DeleteMapping("/{userId}/{interactionId}")
    public ResponseEntity<Void> deleteInteraction(
            @PathVariable String userId,
            @PathVariable UUID interactionId)
    {
        interactionService.deleteInteraction(userId, interactionId);
        return ResponseEntity.noContent().build();
    }
}
