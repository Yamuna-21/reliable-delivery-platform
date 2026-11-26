# Reliable Delivery Platform

An end-to-end DevOps portfolio project demonstrating Java application delivery, CI/CD automation, containerization, vulnerability scanning, and application observability.

## What this project demonstrates

- Java 17 and Spring Boot REST service
- Maven build and automated tests
- Jenkins declarative pipeline
- GitHub Actions CI workflow
- Multi-stage Docker build and non-root runtime
- Docker Compose deployment
- Trivy container vulnerability scanning
- Spring Boot Actuator health and Prometheus metrics
- Prometheus scraping and Grafana dashboard provisioning
- Smoke testing and optional load testing

## Architecture

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

## Prerequisites

- Windows 11 with WSL2 + Ubuntu, or Linux/macOS
- Docker Desktop with the WSL2 backend (Windows) and Docker Compose v2
- Java 17 and Maven 3.9+ for local Maven commands (optional if using Docker build)
- Git
- For Jenkins: a Jenkins controller/agent with Java 17, Maven, Docker CLI/daemon access, `curl`, and Trivy installed

> Docker and cloud resources can consume disk, memory, and money. This project runs locally and does not require AWS or any paid cloud service.

## Run locally with Maven

```bash
mvn clean verify
mvn spring-boot:run
```

Open `http://localhost:8080/api/status`, `http://localhost:8080/api/version`, and `http://localhost:8080/actuator/health`.

## Run the complete local stack

```bash
cp .env.example .env
# Edit .env and set a strong Grafana password before sharing/exposing the service.
docker compose up --build -d
docker compose ps
```

Endpoints:

| Service | URL | Local demo login |
|---|---|---|
| Application | http://localhost:8080/api/status | None |
| Health | http://localhost:8080/actuator/health | None |
| Prometheus metrics | http://localhost:8080/actuator/prometheus | None |
| Prometheus UI | http://localhost:9090 | None |
| Grafana | http://localhost:3000 | admin / value in `.env` |

Grafana should automatically provision the Prometheus data source and the Reliability Overview dashboard. Change the sample Grafana password before exposing the service. Never commit `.env`.

Stop the stack:

```bash
docker compose down
```

Remove the Grafana data volume too (this deletes saved Grafana state):

```bash
docker compose down -v
```

## Test and smoke-check

```bash
mvn clean verify
bash scripts/smoke-test.sh
```

Optional load test: install [`hey`](https://github.com/rakyll/hey), then run:

```bash
bash scripts/load-test.sh http://localhost:8080/api/status 500 20
```

Do not interpret a local load test as a production capacity claim. Record the machine specifications, test duration, concurrency, and results if you publish measurements.

## CI/CD pipelines

### GitHub Actions

The workflow at `.github/workflows/ci.yml` runs on pushes and pull requests to `main`. It tests the code, builds the Docker image, and scans it with Trivy. Review the workflow run in the GitHub Actions tab after pushing the repository.

### Jenkins

The `Jenkinsfile` uses a Jenkins agent with Maven, Docker, Trivy, and curl installed. Create a Pipeline job pointing to this GitHub repository and configure the agent with Docker daemon access. The `Deploy Locally` and `Smoke Test` stages run only on `main`.

For a beginner-friendly setup guide, see [docs/JENKINS_SETUP.md](docs/JENKINS_SETUP.md).

## Suggested GitHub repository description

`End-to-end DevOps project: Spring Boot, Jenkins CI/CD, GitHub Actions, Docker, Trivy security scanning, Prometheus and Grafana monitoring.`

Suggested topics: `devops`, `cicd`, `jenkins`, `docker`, `spring-boot`, `prometheus`, `grafana`, `trivy`, `github-actions`, `sre`, `observability`.

## Portfolio evidence to add after running

1. GitHub Actions green workflow screenshot.
2. Jenkins successful pipeline screenshot (after you configure Jenkins).
3. `docker compose ps` showing app, Prometheus, and Grafana running.
4. Application health endpoint response.
5. Grafana dashboard with live metrics.
6. Trivy scan output. If findings exist, document what you fixed; do not hide or misrepresent vulnerabilities.

## Important honesty note

This repository is a self-built portfolio project. Describe it as a project you implemented and tested. Do not claim that it was deployed to Verizon or handled real production traffic. Add measured performance or reliability numbers only after running documented tests and collecting evidence.

## License

MIT. See [LICENSE](LICENSE).
