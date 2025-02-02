import azure.functions as func

app = func.FunctionApp()

@app.blob_trigger(arg_name="myblob", path="ai-image-processor/{filename}",
                               connection="aiimageprocessorsa_STORAGE") 
def thumbnail_gen_trigger(myblob: func.InputStream):
    import logging
    import os
    from PIL import Image
    from io import BytesIO
    from azure.storage.blob import BlobServiceClient

    CONNECTION_STRING = os.getenv("AzureWebJobsStorage")

    logging.info(f"Processing new blob "
                f"Name: {myblob.name} "
                f"Blob Size: {myblob.length} bytes")

    # Ensure we process only images
    if (not ("image" in myblob.name.lower())) or ("simple" in myblob.name.lower()) or ("txt" in myblob.name.lower()):
        logging.info("Not an image file. Skipping...")
        return

    # Read image from Azure Blob
    image = Image.open(myblob)

    # Convert image to RGB if it has an alpha channel (RGBA)
    if image.mode == "RGBA":
        image = image.convert("RGB")
    
    # Resize image (create thumbnail)
    image.thumbnail((50, 50))

    # Convert to JPEG format
    output_buffer = BytesIO()
    image.save(output_buffer, format="JPEG")
    output_buffer.seek(0)

    # Generate thumbnail filename (add "-simple" after the name and remove the container id)
    thumbnail_name = f"{myblob.name}-simple"
    thumbnail_name = thumbnail_name.removeprefix("ai-image-processor/")

    # Upload thumbnail to Blob Storage
    blob_service_client = BlobServiceClient.from_connection_string(CONNECTION_STRING)
    container_client = blob_service_client.get_container_client("ai-image-processor")
    blob_client = container_client.get_blob_client(thumbnail_name)
    blob_client.upload_blob(output_buffer, overwrite=True, content_type="image/jpeg")

    logging.info(f"Thumbnail saved as: {thumbnail_name}")
