import logging
from io import BytesIO
from typing import Tuple

import numpy as np
from PIL import Image
import easyocr

from app.services.base_service import BaseTaskService


class OCRService(BaseTaskService):
    def __init__(self):
        self.reader = easyocr.Reader(["en"])

    def process(self, content: BytesIO) -> Tuple[BytesIO, str]:
        """Perform OCR on the image and return the text."""
        logging.info("Will perform OCR on image")
        image = Image.open(content).convert(
            "RGB"
        )  # Convert to RGB to ensure compatibility
        image_np = np.array(image)
        text = self.reader.readtext(image_np, detail=0)  # Extract text with EasyOCR
        result_text = "\n".join(text)  # Combine the detected text lines
        result = BytesIO(result_text.encode("utf-8"))  # Encode to BytesIO
        logging.info("Done OCR process")
        return result, "text/plain"
