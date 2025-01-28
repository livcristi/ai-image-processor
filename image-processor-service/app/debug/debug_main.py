import logging

import uvicorn
from fastapi import FastAPI, HTTPException

from app.config.config import Config
from app.models.models import OperationType, TaskInfo
from app.processor.task_processor import TaskProcessor
from app.services.bgr_service import BGRService
from app.services.css_client import CSSClient
from app.services.ocr_service import OCRService

logging.basicConfig(level=logging.INFO)

app = FastAPI()
config = Config()
css_client = CSSClient(config.css_base_url)
task_processor: TaskProcessor = TaskProcessor(css_client)
bgr_service = BGRService()
ocr_service = OCRService()
task_processor.add_handler(OperationType.OCR, ocr_service)
task_processor.add_handler(OperationType.BGR, bgr_service)


@app.post("/process")
async def process_task(task_info: TaskInfo):
    try:
        task_processor.process_task(task_info)
        return {
            "status": "success",
            "message": "Task processed and uploaded successfully",
        }
    except Exception as e:
        logging.error("Unable to process task, task: %s", task_info, exc_info=e)
        raise HTTPException(status_code=500, detail=str(e))


def start_debug_server():
    """Start the FastAPI server for debugging."""
    uvicorn.run("app.main:app", host="0.0.0.0", port=7540, reload=False)


if __name__ == "__main__":
    start_debug_server()
