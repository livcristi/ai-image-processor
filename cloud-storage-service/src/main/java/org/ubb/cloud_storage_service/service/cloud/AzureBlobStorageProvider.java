package org.ubb.cloud_storage_service.service.cloud;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.ubb.cloud_storage_service.exception.ObjectNotFoundException;
import org.ubb.cloud_storage_service.exception.ObjectStorageException;
import org.ubb.cloud_storage_service.utils.MediaTypeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class AzureBlobStorageProvider implements ObjectStorageProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(AzureBlobStorageProvider.class);
    private static final String CONTAINER_NAME = "ai-image-processor";

    private final BlobServiceClient blobServiceClient;

    public AzureBlobStorageProvider(BlobServiceClient blobServiceClient)
    {
        this.blobServiceClient = blobServiceClient;
    }

    private BlobClient getBlobClient(String userId, UUID interactionId, String objectName)
    {
        String blobPath = String.format("%s/%s/%s", userId, interactionId, objectName);
        return blobServiceClient.getBlobContainerClient(CONTAINER_NAME).getBlobClient(blobPath);
    }

    @Override
    public void uploadObjectContent(String userId, UUID interactionId, String objectName, InputStream content)
    {
        LOG.info("Will uploadObjectContent to Azure, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
        BlobClient blobClient = getBlobClient(userId, interactionId, objectName);
        if (blobClient.exists())
        {
            LOG.warn("Attempted content overwrite, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
            throw new ObjectStorageException("Content already exists and cannot be overwritten.");
        }
        try
        {
            BlobHttpHeaders blobHttpHeaders = new BlobHttpHeaders().setContentType(MediaTypeUtils.inferContentType(objectName));

            blobClient.upload(content, content.available(), true);
            blobClient.setHttpHeaders(blobHttpHeaders);
        } catch (IOException e)
        {
            LOG.error("Unable to uploadObjectContent, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName, e);
            throw new ObjectStorageException("Unable to upload content to blob", e);
        }
        LOG.info("Done uploadObjectContent to Azure, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
    }

    @Override
    public InputStream getObjectContent(String userId, UUID interactionId, String objectName)
    {
        LOG.info("Will getObjectContent, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
        BlobClient blobClient = getBlobClient(userId, interactionId, objectName);
        if (!blobClient.exists())
        {
            LOG.warn("Unable to find object content, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
            throw new ObjectNotFoundException("Object content not found.");
        }
        InputStream response = blobClient.openInputStream();
        LOG.info("Done getObjectContent, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
        return response;
    }

    @Override
    public void deleteObject(String userId, UUID interactionId, String objectName)
    {
        LOG.info("Will deleteObject, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
        BlobClient blobClient = getBlobClient(userId, interactionId, objectName);
        if (blobClient.exists())
        {
            blobClient.delete();
        }
        LOG.info("Done deleteObject, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
    }

    @Override
    public boolean objectExists(String userId, UUID interactionId, String objectName)
    {
        LOG.info("Will check objectExists, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
        boolean response = getBlobClient(userId, interactionId, objectName).exists();
        LOG.info("Done check objectExists, userId: {}, interactionId: {}, objectName: {}", userId, interactionId, objectName);
        return response;
    }
}
