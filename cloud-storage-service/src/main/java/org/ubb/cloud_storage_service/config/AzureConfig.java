package org.ubb.cloud_storage_service.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig
{
    @Value("${blob-storage.azure.key}")
    private String blobConnectionString;

    @Bean
    public BlobServiceClient blobServiceClient()
    {
        if (StringUtils.isBlank(blobConnectionString))
        {
            throw new IllegalStateException("Azure storage connection string is not set in the environment variables.");
        }
        return new BlobServiceClientBuilder()
                .connectionString(blobConnectionString)
                .buildClient();
    }
}
