from pydantic import BaseModel


class RabbitMqConnectionDetails(BaseModel):
    rabbitmq_host: str
    rabbitmq_port: int
    rabbitmq_queue: str
