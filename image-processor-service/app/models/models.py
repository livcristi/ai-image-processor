from enum import Enum
from typing import List, Dict
from uuid import UUID

from pydantic import BaseModel


class OperationType(str, Enum):
    BGR = "BGR"
    OCR = "OCR"


class TaskInfo(BaseModel):
    taskId: UUID
    userId: str
    objectId: UUID
    interactionId: UUID
    operationType: OperationType
    objectName: str


class InteractionRequest(BaseModel):
    interactionId: UUID
    userId: str
    status: str
    operationType: OperationType
    tags: List[Dict[str, str]]
