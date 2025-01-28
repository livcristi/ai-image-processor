package org.ubb.cloud_storage_service.service.interaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.ubb.cloud_storage_service.model.TagData;
import org.ubb.cloud_storage_service.repository.InteractionRepository;
import org.ubb.cloud_storage_service.utils.Converter;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class InteractionServiceImpl implements InteractionService
{
    private static final Logger LOG = LoggerFactory.getLogger(InteractionServiceImpl.class);

    private final InteractionRepository interactionRepository;

    public InteractionServiceImpl(InteractionRepository interactionRepository)
    {
        this.interactionRepository = interactionRepository;
    }

    @Override
    public InteractionResponse createInteraction(InteractionRequest request)
    {
        LOG.info("Will createInteraction, request: {}", request);
        Interaction interaction = Converter.toInteraction(request);
        Interaction savedInteraction = interactionRepository.save(interaction);
        InteractionResponse response = Converter.toInteractionResponse(savedInteraction);
        LOG.info("Done createInteraction, response: {}", response);
        return response;
    }

    @Override
    public Optional<InteractionResponse> getInteraction(String userId, UUID interactionId)
    {
        LOG.info("Will getInteraction, userId: {}, interactionId: {}", userId, interactionId);
        var response = interactionRepository.findByUserIdAndInteractionId(userId, interactionId)
                .map(Converter::toInteractionResponse);
        LOG.info("Done getInteraction, userId: {}, interactionId: {}, isPresent: {}", userId, interactionId, response.isPresent());
        return response;
    }

    @Override
    public Page<InteractionResponse> getUserInteractions(String userId, Pageable pageable)
    {
        LOG.info("Will getUserInteractions, userId: {}, pageable: {}", userId, pageable);
        var response = interactionRepository.findByUserId(userId, pageable)
                .map(Converter::toInteractionResponse);
        LOG.info("Done getUserInteractions, userId: {}, pageable: {}, items: {}", userId, pageable, response.getNumberOfElements());
        return response;
    }

    @Override
    public InteractionResponse updateInteraction(String userId, UUID interactionId, InteractionRequest request)
    {
        LOG.info("Will updateInteraction, userId: {}, interactionId: {}, request: {}", userId, interactionId, request);
        Interaction existingInteraction = interactionRepository.findByUserIdAndInteractionId(userId, interactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found"));

        // Update tags and status
        existingInteraction.setStatus(EntityStatus.valueOf(request.getStatus()));
        existingInteraction.setOperationType(OperationType.valueOf(request.getOperationType()));

        if (request.getTags() != null && !request.getTags().isEmpty())
        {
            updateTags(existingInteraction, Converter.toTagDataSet(request.getTags()));
        }

        Interaction updatedInteraction = interactionRepository.save(existingInteraction);
        InteractionResponse response = Converter.toInteractionResponse(updatedInteraction);
        LOG.info("Done updateInteraction, userId: {}, interactionId: {}, response: {}", userId, interactionId, response);
        return response;
    }

    @Override
    public void deleteInteraction(String userId, UUID interactionId)
    {
        LOG.info("Will deleteInteraction, userId: {}, interactionId: {}", userId, interactionId);
        Interaction existingInteraction = interactionRepository.findByUserIdAndInteractionId(userId, interactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found"));

        if (existingInteraction.getObjectInfoList() != null && !existingInteraction.getObjectInfoList().isEmpty())
        {
            LOG.warn("Unable to deleteInteraction, it is not empty, userId: {}, interactionId: {}", userId, interactionId);
            throw new BadRequestException("Cannot delete an interaction that is not empty");
        }

        interactionRepository.delete(existingInteraction);
        LOG.info("Done deleteInteraction, userId: {}, interactionId: {}", userId, interactionId);
    }

    private static void updateTags(Interaction existingInteraction, Set<TagData> newTags)
    {
        // Updates existing tags with the new values or appends the tags which do not exist
        Set<TagData> existingTags = existingInteraction.getTags();
        for (TagData tag : newTags)
        {
            TagData existingTag = existingTags.stream()
                    .filter(tagData -> tagData.getTagKey().equals(tag.getTagKey()))
                    .findFirst().orElse(null);
            if (existingTag != null)
            {
                existingTag.setTagValue(tag.getTagValue());
            } else
            {
                existingTags.add(tag);
            }
        }
    }
}
