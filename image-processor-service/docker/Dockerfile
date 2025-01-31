# Use a slim python base image
FROM python:3.10-slim

WORKDIR /app

COPY ../requirements.txt /app/requirements.txt

# Install the dependencies, without cache
RUN pip install --no-cache-dir -r /app/requirements.txt

COPY ../app /app

CMD ["python", "main.py"]