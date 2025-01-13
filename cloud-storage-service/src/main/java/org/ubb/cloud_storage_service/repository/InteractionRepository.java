package org.ubb.cloud_storage_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ubb.cloud_storage_service.model.Interaction;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, UUID>
{
    Optional<Interaction> findByUserIdAndInteractionId(String userId, UUID interactionId);

    Page<Interaction> findByUserId(String userId, Pageable pageable);
}
