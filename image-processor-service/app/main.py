import logging

from app.config.config import Config
from app.models.models import OperationType
from app.processor.task_processor import TaskProcessor
from app.rabbitmq.rabbitmq_consumer import rabbitmq_consumer
from app.services.bgr_service import BGRService
from app.services.css_client import CSSClient
from app.services.ocr_service import OCRService

logging.basicConfig(level=logging.INFO)

config = Config()
css_client = CSSClient(config.css_base_url)
task_processor: TaskProcessor = TaskProcessor(css_client)
bgr_service = BGRService()
ocr_service = OCRService()
task_processor.add_handler(OperationType.OCR, ocr_service)
task_processor.add_handler(OperationType.BGR, bgr_service)


# RabbitMQ Implementation
def start_rabbitmq_listener():
    """Start RabbitMQ consumer loop."""
    rabbitmq_consumer(config.rabbit_mq_connection_details, task_processor)


if __name__ == "__main__":
    start_rabbitmq_listener()
