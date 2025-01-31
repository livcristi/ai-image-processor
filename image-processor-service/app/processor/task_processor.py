import logging
from typing import Dict

from models.models import OperationType, TaskInfo
from services.base_service import BaseTaskService
from services.css_client import CSSClient


class TaskProcessor:
    def __init__(self, css_client: CSSClient):
        self.__processors: Dict[OperationType, BaseTaskService] = {}
        self.__css_client = css_client

    def add_handler(self, operation_type: OperationType, handler: BaseTaskService):
        self.__processors[operation_type] = handler

    def process_task(self, task_info: TaskInfo):
        try:
            logging.info("Will process task: %s", task_info)
            if task_info is None or task_info.operationType not in self.__processors:
                raise KeyError(
                    "Unable to find processor for task: {}".format(task_info)
                )

            content_stream = self.__css_client.get_object_content(task_info)

            logging.info(
                "Will process task %s with processor for operation %s",
                task_info.taskId,
                task_info.operationType,
            )
            result, content_type = self.__processors[task_info.operationType].process(
                content_stream
            )

            self.__css_client.create_upload_object_content(
                task_info, result, content_type
            )
            self.__css_client.mark_interaction_completed(task_info)

            logging.info("Done processing task: %s", task_info)
        except Exception as e:
            logging.error("Unable to process task, task: %s", task_info, exc_info=e)
            raise
