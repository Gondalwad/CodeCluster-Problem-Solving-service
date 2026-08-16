# CodeCluster - Problem Solving Service⚡

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Engine-blue.svg)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/)

Developed by **Sudarshan Gondalwad** ([@Gondalwad](https://github.com/Gondalwad))

**CodeCluster Problem Solving Service** is a high-performance backend microservice engineered to evaluate user-submitted code against dynamic test cases. Built with Spring Boot 3, Java 21, and Docker Engine API, it provides safe, isolated, and resource-bounded execution environments similar to platforms like LeetCode and HackerRank.

---

## 🌟 Key Engineering Highlights

- **Dynamic Sandboxed Execution:** Spawns ephemeral Docker containers on demand to run untrusted code in strict isolation from the host environment.
- **Robust Resource Lifecycle Management:** Implements deterministic container cleanup inside standard execution pipelines (`finally` blocks) to prevent resource leaks or zombie containers, even during crashes or infinite loops.
- **Multi-Language Runtime Engine:** Native compilation and runtime execution pipelines for **C++, Python 3, Java 21, Node.js (JavaScript), and C**.
- **Real-Time Multiplexed I/O:** Intercepts standard streams (`STDOUT`, `STDERR`, and `STDIN`) using `docker-java` SDK callbacks to prevent process blocking and memory exhaustion.
- **Fault Tolerance & Safety Limits:** Enforces strict execution timeouts (**Time Limit Exceeded - TLE**), memory boundaries (**Out of Memory - OOM / MLE**), and runtime exception handling (e.g., Segmentation Faults / RTE).

---

## 🏗️ System Architecture & Execution Flow

```text
[ Client / Calling Service ]
         │
         ▼ (POST /api/v1/submissions/execute)
[ Problem Solving Microservice ]
         │
         ├── 1. Persist Submission Data (PostgreSQL)
         ├── 2. Map Language ID -> Docker Base Image & File System
         ├── 3. Spawn Isolated Ephemeral Docker Container
         ├── 4. Compile Source Code (if compiled language)
         ├── 5. Pipe Test Case Inputs & Capture STDOUT / STDERR
         ├── 6. Evaluate Result (Accepted, TLE, RTE, Compile Error)
         │
         ▼ (Guaranteed in 'finally' block)
[ Destroy Container & Cleanup Mounts ] ──► Return ExecResult Payload
```

---

## 🛠️ Tech Stack & Dependencies

- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.x, Spring Data JPA
- **Database:** PostgreSQL
- **Containerization:** Docker Engine, `docker-java` SDK
- **Build Tool:** Maven

---

## 🚀 Supported Languages & Execution Specs

| Language ID | Language | Compiler / Runtime | Base Docker Image | Default File | Compilation Command | Execution Command |
| :---: | :--- | :--- | :--- | :--- | :--- | :--- |
| **1** | C++ | `g++ 12+` | `gcc:latest` | `solution.cpp` | `g++ -O2 solution.cpp -o solution` | `./solution` |
| **2** | Python | `Python 3.10` | `python:3.10-slim` | `solution.py` | *(Interpreted)* | `python3 solution.py` |
| **3** | Java | `OpenJDK 21` | `eclipse-temurin:21-jdk` | `Main.java` | `javac Main.java` | `java Main` |
| **4** | JavaScript | `Node.js 18+` | `node:18-slim` | `solution.js` | *(Interpreted)* | `node solution.js` |
| **5** | C | `gcc 12+` | `gcc:latest` | `solution.c` | `gcc -O2 solution.c -o solution` | `./solution` |

---

## 🔌 API Specification

### Execute Code Submission

`POST /api/v1/submissions/execute`

#### Sample Request Payload
```json
{
  "userId": "58c87a11-5871-4585-88ed-c89255170eb0",
  "programmingLanguageId": 3,
  "program": "import java.util.HashMap;\nimport java.util.Map;\n\npublic class Solution {\n    public static void main(String[] args) {\n        System.out.println(\"Hello CodeCluster!\");\n    }\n}"
}
```

#### Sample Response Payload
```json
{
  "submissionId": "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d",
  "status": "ACCEPTED",
  "stdout": "Hello CodeCluster!\n",
  "stderr": "",
  "executionTimeMs": 142,
  "exitCode": 0
}
```

---

## ⚙️ Environment Setup & Deployment

### Prerequisites
- Java 21 JDK installed
- Docker Engine daemon running locally
- PostgreSQL Database instance

### 1. Pull Required Execution Base Images
Before running the service, pull the execution images into your local Docker daemon:
```bash
docker pull gcc:latest
docker pull python:3.10-slim
docker pull eclipse-temurin:21-jdk
docker pull node:18-slim
```

### 2. Configure Database Connection
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/codecluster_db?stringtype=unspecified
    username: postgres
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

### 3. Build & Run
```bash
# Clone the repository
git clone [https://github.com/Gondalwad/CodeCluster-Problem-Solving-service.git](https://github.com/Gondalwad/CodeCluster-Problem-Solving-service.git)
cd CodeCluster-Problem-Solving-service

# Build executable JAR
./mvnw clean package -DskipTests

# Run service
java -jar target/problem-solving-service.jar
```

### 4. Docker Compose Setup
```bash
docker-compose up --build -d
```

---

## 🛡️ Security & Sandbox Design

1. **Non-Persistent Filesystem:** Executions occur within temporary container runtimes, removed via `removeContainerCmd` with volume cleanup.
2. **Process Timeout Handling:** Uses `awaitCompletion` timers to interrupt long-running executions or infinite loops, sending standard termination signals (`SIGKILL`/`SIGTERM`).
3. **Safe I/O Interception:** Employs thread-safe stream adapters (`ExecStartResultCallback`) to process standard streams without blocking background execution worker pools.

---

## 👨‍💻 Author

**Sudarshan Gondalwad**
- **GitHub:** [@Gondalwad](https://github.com/Gondalwad)
- **Repository:** [CodeCluster-Problem-Solving-service](https://github.com/Gondalwad/CodeCluster-Problem-Solving-service.git)

## License

This project is currently part of the CodeCluster project and is intended for development and academic/project purposes.