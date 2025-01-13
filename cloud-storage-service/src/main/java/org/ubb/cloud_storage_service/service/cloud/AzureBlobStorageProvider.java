package org.ubb.cloud_storage_service.service.cloud;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
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
    private final BlobServiceClient blobServiceClient;
    private static final String CONTAINER_NAME = "ai-image-processor";

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
        BlobClient blobClient = getBlobClient(userId, interactionId, objectName);
        if (blobClient.exists())
        {
            throw new ObjectStorageException("Content already exists and cannot be overwritten.");
        }
        try
        {
            BlobHttpHeaders blobHttpHeaders = new BlobHttpHeaders().setContentType(MediaTypeUtils.inferContentType(objectName));

            blobClient.upload(content, content.available(), true);
            blobClient.setHttpHeaders(blobHttpHeaders);
        } catch (IOException e)
        {
            throw new ObjectStorageException("Unable to upload content to blob", e);
        }
    }

    @Override
    public InputStream getObjectContent(String userId, UUID interactionId, String objectName)
    {
        BlobClient blobClient = getBlobClient(userId, interactionId, objectName);
        if (!blobClient.exists())
        {
            throw new ObjectNotFoundException("Object content not found.");
        }
        return blobClient.openInputStream();
    }

    @Override
    public void deleteObject(String userId, UUID interactionId, String objectName)
    {
        BlobClient blobClient = getBlobClient(userId, interactionId, objectName);
        if (blobClient.exists())
        {
            blobClient.delete();
        }
    }

    @Override
    public boolean objectExists(String userId, UUID interactionId, String objectName)
    {
        return getBlobClient(userId, interactionId, objectName).exists();
    }


}
