package org.ubb.cloud_storage_service.utils;

public class MediaTypeUtils
{
    public static String inferContentType(String fileName)
    {
        // Thumbnails are always jpg
        if (fileName.endsWith("-simple"))
        {
            return "image/jpeg";
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return switch (extension.toLowerCase())
        {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "txt" -> "text/plain";
            default -> "application/octet-stream";
        };
    }
}
