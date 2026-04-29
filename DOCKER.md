# Docker Setup for Ballot Buddy

This document explains how to build and run Ballot Buddy using Docker.

## Prerequisites

- Docker Desktop installed (or Docker Engine + Docker Compose)
- Your Gemini API key

## Quick Start

### Option 1: Using Docker Compose (Recommended)

1. **Create environment file:**
   ```bash
   cp .env.template .env
   ```

2. **Edit `.env` and add your Gemini API key:**
   ```bash
   GEMINI_API_KEY=your_actual_gemini_key_here
   ```

3. **Build and run:**
   ```bash
   docker-compose up --build
   ```

4. **Access the application:**
   - Open browser: http://localhost:8080
   - Health check: http://localhost:8080/api/health

5. **Stop the application:**
   ```bash
   docker-compose down
   ```

### Option 2: Using Docker Commands

1. **Build the Docker image:**
   ```bash
   docker build -t ballot-buddy:latest .
   ```

2. **Run the container:**
   ```bash
   docker run -d \
     --name ballot-buddy \
     -p 8080:8080 \
     -e GEMINI_API_KEY=your_gemini_api_key_here \
     -e GCP_PROJECT_ID=ballot-buddy-494805 \
     -e GCP_STORAGE_BUCKET=ballot-buddy-494805-analytics \
     ballot-buddy:latest
   ```

3. **View logs:**
   ```bash
   docker logs -f ballot-buddy
   ```

4. **Stop the container:**
   ```bash
   docker stop ballot-buddy
   docker rm ballot-buddy
   ```

## Docker Image Details

- **Base Image:** Eclipse Temurin 17 JRE Alpine (lightweight)
- **Image Size:** ~200-250 MB (optimized multi-stage build)
- **Security:** Runs as non-root user
- **Health Check:** Automated health checks every 30 seconds

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `GEMINI_API_KEY` | Google Gemini API key | `REPLACE_ME` |
| `GCP_PROJECT_ID` | GCP Project ID | `ballot-buddy-494805` |
| `GCP_STORAGE_BUCKET` | GCP Storage bucket name | `ballot-buddy-494805-analytics` |
| `SPRING_PROFILES_ACTIVE` | Spring profile | `prod` |
| `JAVA_OPTS` | JVM options | `-Xmx512m -Xms256m` |

## Useful Commands

### View running containers:
```bash
docker ps
```

### View all containers (including stopped):
```bash
docker ps -a
```

### Check container health:
```bash
docker inspect --format='{{.State.Health.Status}}' ballot-buddy
```

### Execute command in running container:
```bash
docker exec -it ballot-buddy sh
```

### View container resource usage:
```bash
docker stats ballot-buddy
```

### Remove all stopped containers:
```bash
docker container prune
```

### Remove unused images:
```bash
docker image prune -a
```

## Production Deployment

For production deployment, consider:

1. **Use environment-specific configurations**
2. **Enable HTTPS/TLS**
3. **Use secrets management** (Docker Secrets, AWS Secrets Manager, etc.)
4. **Set up monitoring** (Prometheus, Grafana)
5. **Configure log aggregation** (ELK Stack, CloudWatch)
6. **Use orchestration** (Kubernetes, Docker Swarm)

## Troubleshooting

### Container won't start
- Check logs: `docker logs ballot-buddy`
- Verify environment variables are set correctly
- Ensure port 8080 is not already in use

### Health check failing
- Check if application is running: `docker logs ballot-buddy`
- Verify health endpoint is accessible inside container:
  ```bash
  docker exec ballot-buddy wget -O- http://localhost:8080/api/health
  ```

### Out of memory errors
- Increase JVM memory:
  ```bash
  docker run -e JAVA_OPTS="-Xmx1024m -Xms512m" ...
  ```

## Build Arguments

You can customize the build process using build arguments:

```bash
docker build \
  --build-arg GRADLE_VERSION=8.10 \
  --build-arg JAVA_VERSION=17 \
  -t ballot-buddy:latest .
```
