import os

from pydantic_settings import BaseSettings

from config.connection_details import RabbitMqConnectionDetails


class Config(BaseSettings):
    css_base_url: str = os.getenv("CSS_BASE_URL", "http://localhost:8080/api")
    rabbit_mq_connection_details: RabbitMqConnectionDetails = RabbitMqConnectionDetails(
        rabbitmq_host=os.getenv("RABBITMQ_HOST", "localhost"),
        rabbitmq_vhost=os.getenv("RABBITMQ_VHOST", "/"),
        rabbitmq_port=int(os.getenv("RABBITMQ_PORT", "5672")),
        rabbitmq_queue=os.getenv("RABBITMQ_QUEUE", "image-processor-task-queue"),
        rabbitmq_connection_string=os.getenv("RABBITMQ_CONNECTION_STRING", None),
    )
