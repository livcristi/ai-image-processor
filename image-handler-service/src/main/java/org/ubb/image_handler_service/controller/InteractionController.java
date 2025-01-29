package org.ubb.image_handler_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ubb.image_handler_service.dto.ObjectInfoResponse;
import org.ubb.image_handler_service.dto.OperationType;
import org.ubb.image_handler_service.dto.interaction.InteractionContent;
import org.ubb.image_handler_service.dto.interaction.InteractionPreview;
import org.ubb.image_handler_service.dto.interaction.InteractionResponse;
import org.ubb.image_handler_service.dto.interaction.InteractionStatus;
import org.ubb.image_handler_service.service.InteractionService;

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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObjectInfoResponse> createInteraction(@RequestParam("userId") String userId,
                                                                @RequestParam("operationType") OperationType operationType,
                                                                @RequestParam("image") MultipartFile image)
    {
        ObjectInfoResponse response = interactionService.createInteraction(userId, operationType, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<InteractionResponse>> getUserInteractions(@RequestParam("userId") String userId,
                                                                         @PageableDefault(size = 5, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<InteractionResponse> response = interactionService.getUserInteractions(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{interactionId}/preview")
    public ResponseEntity<InteractionPreview> getInteractionPreview(@RequestParam("userId") String userId, @PathVariable("interactionId") UUID interactionId)
    {
        InteractionPreview response = interactionService.getInteractionPreview(userId, interactionId);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{interactionId}/status")
    public ResponseEntity<InteractionStatus> getInteractionStatus(@RequestParam("userId") String userId, @PathVariable("interactionId") UUID interactionId)
    {
        InteractionStatus response = interactionService.getInteractionStatus(userId, interactionId);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{interactionId}/result")
    public ResponseEntity<InteractionContent> getInteractionResult(@RequestParam("userId") String userId, @PathVariable("interactionId") UUID interactionId)
    {
        InteractionContent response = interactionService.getInteractionResult(userId, interactionId);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{interactionId}")
    public ResponseEntity<Void> deleteInteraction(@RequestParam("userId") String userId, @PathVariable("interactionId") UUID interactionId)
    {
        interactionService.deleteInteraction(userId, interactionId);
        return ResponseEntity.noContent().build();
    }
}
