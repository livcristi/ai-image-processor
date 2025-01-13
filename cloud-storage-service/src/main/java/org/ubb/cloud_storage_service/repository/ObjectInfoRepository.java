package org.ubb.cloud_storage_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ubb.cloud_storage_service.model.ObjectInfo;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObjectInfoRepository extends JpaRepository<ObjectInfo, UUID>
{
    Optional<ObjectInfo> findByUserIdAndObjectId(String userId, UUID objectId);
}
