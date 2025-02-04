# Use official Python image
FROM python:3.9
WORKDIR /app

# Copy and install dependencies
COPY requirements.txt .
RUN pip install -r requirements.txt

# Copy the rest of the files
COPY . .

# Expose port 8000 and run the server
EXPOSE 8000
CMD ["python", "app.py"]
