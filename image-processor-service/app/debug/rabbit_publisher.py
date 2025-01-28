import json

import pika

# RabbitMQ configuration
RABBITMQ_QUEUE = "image-processor-task-queue"
RABBITMQ_HOST = "localhost"
RABBITMQ_PORT = 5672

# Example task
task = {
    "taskId": "88635ee7-2790-40ed-aba2-068723eeb0ef",
    "userId": "sampleUserId",
    "objectId": "498e9253-83c6-45f2-bab0-38fece328264",
    "interactionId": "376f38e7-fab0-466c-85b7-e8532ab9afc9",
    "operationType": "OCR",
    "objectName": "myimage.jpg",
}

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host=RABBITMQ_HOST, port=RABBITMQ_PORT)
)
channel = connection.channel()

# Declare the queue
channel.queue_declare(queue=RABBITMQ_QUEUE)

# Publish the task
channel.basic_publish(exchange="", routing_key=RABBITMQ_QUEUE, body=json.dumps(task))
print("Task published to RabbitMQ")

connection.close()
