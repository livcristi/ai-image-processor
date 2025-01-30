from pydantic import BaseModel


class RabbitMqConnectionDetails(BaseModel):
    rabbitmq_host: str
    rabbitmq_vhost: str
    rabbitmq_port: int
    rabbitmq_queue: str
    rabbitmq_connection_string: str
