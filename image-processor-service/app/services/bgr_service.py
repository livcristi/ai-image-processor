import logging
from io import BytesIO
from typing import Tuple

from rembg import remove

from app.services.base_service import BaseTaskService


class BGRService(BaseTaskService):
    def process(self, content: BytesIO) -> Tuple[BytesIO, str]:
        """Perform background removal on the image."""
        logging.info("Will perform background removal on image")
        input_image = content.read()  # Read image content as bytes
        output_image = remove(input_image)  # Remove background
        result = BytesIO(output_image)  # Convert the result to BytesIO
        logging.info("Done background removal process")
        return result, "image/png"
