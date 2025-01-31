import json
import logging

import pika

from config.connection_details import RabbitMqConnectionDetails
from models.models import TaskInfo
from processor.task_processor import TaskProcessor


def rabbitmq_consumer(
    connection_data: RabbitMqConnectionDetails, task_processor: TaskProcessor
):
    """RabbitMQ consumer that listens for tasks."""
    parameters = pika.URLParameters(connection_data.rabbitmq_connection_string)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    # Declare the queue
    channel.queue_declare(queue=connection_data.rabbitmq_queue, durable=True)

    def callback(ch, method, properties, body):
        """Callback to process a RabbitMQ message."""
        try:
            logging.info("Received task from RabbitMQ")
            task_data = json.loads(body)
            task_info = TaskInfo(**task_data)  # Parse JSON into TaskInfo object
            task_processor.process_task(task_info)
            ch.basic_ack(delivery_tag=method.delivery_tag)  # Acknowledge message
        except Exception as e:
            logging.error("Error processing RabbitMQ task: %s", e)

    channel.basic_consume(
        queue=connection_data.rabbitmq_queue,
        on_message_callback=callback,
        auto_ack=False,
    )
    logging.info("RabbitMQ consumer started. Waiting for tasks...")
    channel.start_consuming()
