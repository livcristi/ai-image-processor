package org.ubb.cloud_storage_service.service.interaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ubb.cloud_storage_service.dto.InteractionRequest;
import org.ubb.cloud_storage_service.dto.InteractionResponse;
import org.ubb.cloud_storage_service.exception.BadRequestException;
import org.ubb.cloud_storage_service.exception.ResourceNotFoundException;
import org.ubb.cloud_storage_service.model.EntityStatus;
import org.ubb.cloud_storage_service.model.Interaction;
import org.ubb.cloud_storage_service.model.OperationType;
import org.ubb.cloud_storage_service.repository.InteractionRepository;
import org.ubb.cloud_storage_service.utils.Converter;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InteractionServiceImpl implements InteractionService
{
    private final InteractionRepository interactionRepository;

    public InteractionServiceImpl(InteractionRepository interactionRepository)
    {
        this.interactionRepository = interactionRepository;
    }

    @Override
    public InteractionResponse createInteraction(InteractionRequest request)
    {
        Interaction interaction = Converter.toInteraction(request);
        Interaction savedInteraction = interactionRepository.save(interaction);
        return Converter.toInteractionResponse(savedInteraction);
    }

    @Override
    public Optional<InteractionResponse> getInteraction(String userId, UUID interactionId)
    {
        return interactionRepository.findByUserIdAndInteractionId(userId, interactionId)
                .map(Converter::toInteractionResponse);
    }

    @Override
    public Page<InteractionResponse> getUserInteractions(String userId, Pageable pageable)
    {
        return interactionRepository.findByUserId(userId, pageable)
                .map(Converter::toInteractionResponse);
    }

    @Override
    public InteractionResponse updateInteraction(String userId, UUID interactionId, InteractionRequest request)
    {
        Interaction existingInteraction = interactionRepository.findByUserIdAndInteractionId(userId, interactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found"));

        // Update tags and status
        existingInteraction.setStatus(EntityStatus.valueOf(request.getStatus()));
        existingInteraction.setOperationType(OperationType.valueOf(request.getOperationType()));
        existingInteraction.setTags(Converter.toTagDataSet(request.getTags()));

        Interaction updatedInteraction = interactionRepository.save(existingInteraction);
        return Converter.toInteractionResponse(updatedInteraction);
    }

    @Override
    public void deleteInteraction(String userId, UUID interactionId)
    {
        Interaction existingInteraction = interactionRepository.findByUserIdAndInteractionId(userId, interactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found"));

        if (existingInteraction.getObjectInfoList() != null && !existingInteraction.getObjectInfoList().isEmpty())
        {
            throw new BadRequestException("Cannot delete an interaction that is not empty");
        }

        interactionRepository.delete(existingInteraction);
    }
}
