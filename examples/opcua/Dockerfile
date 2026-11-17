FROM python:3.8-slim

WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY example-opcua-server.py .

# Advertised endpoint. The host name must resolve to this container.
ENV OPCUA_ENDPOINT=opc.tcp://opcua:12345/lablink-test

EXPOSE 12345
ENTRYPOINT ["python", "-u", "example-opcua-server.py"]
