from abc import ABC, abstractmethod
from io import BytesIO
from typing import Tuple


class BaseTaskService(ABC):
    @abstractmethod
    def process(self, content: BytesIO) -> Tuple[BytesIO, str]:
        """Process the content and return the result and its MIME type."""
        pass
