import json
import logging
from io import BytesIO

import requests

from app.models.models import TaskInfo, InteractionRequest


class CSSClient:
    def __init__(self, base_url: str):
        self.base_url = base_url

    def get_object_content(self, task: TaskInfo) -> BytesIO:
        """Retrieve object content from the cloud storage service."""
        logging.info("Will get object content for task: %s", task)
        url = f"{self.base_url}/objects/{task.userId}/{task.objectId}/content"
        response = requests.get(url)
        response.raise_for_status()
        logging.info("Done get object content for task: %s", task)
        return BytesIO(response.content)

    def create_upload_object_content(self, task: TaskInfo, content: BytesIO, content_type: str):
        """Create and upload the processed result to the cloud storage."""
        logging.info("Will create and upload object content for task: %s, content_type: %s", task, content_type)
        url = f"{self.base_url}/objects"
        files = {"file": (self.__get_content_name(content_type), content, content_type)}
        metadata = {
            "userId": task.userId,
            "name": self.__get_content_name(content_type),
            "interactionId": str(task.interactionId),
            "type": content_type,
        }
        metadata_json = json.dumps(metadata)
        response = requests.post(url, files=files, data={"metadata": metadata_json})
        response.raise_for_status()
        logging.info("Done create object content for task: %s, content_type: %s", task, content_type)

    def mark_interaction_completed(self, task_info: TaskInfo):
        """Mark the interaction as completed."""
        logging.info("Will mark interaction as completed: %s", task_info)
        interaction_request: InteractionRequest = InteractionRequest(interactionId=task_info.interactionId,
                                                                     userId=task_info.userId,
                                                                     status="COMPLETED",
                                                                     operationType=task_info.operationType,
                                                                     tags=[])
        url = f"{self.base_url}/interactions/{interaction_request.userId}/{interaction_request.interactionId}"
        headers = {
            'Content-Type': 'application/json'
        }
        response = requests.put(url, data=interaction_request.model_dump_json(), headers=headers)
        response.raise_for_status()
        logging.info("Done mark interaction as completed: %s", task_info)

    @staticmethod
    def __get_content_name(content_type):
        if content_type is None or len(content_type) == 0:
            return "empty"
        match content_type.strip():
            case "image/png":
                return "result.png"
            case "image/jpeg":
                return "result.jpg"
            case "text/plain":
                return "result.txt"
            case _:
                return "result"
