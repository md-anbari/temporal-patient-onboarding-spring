# Temporal Patient Onboarding System (Spring Boot Edition)

A Spring Boot adaptation of the [original Quarkus-based Temporal Patient Onboarding Demo](https://github.com/tsurdilo/temporal-patient-onboarding), enhanced with PostgreSQL persistence. This is a proof-of-concept project that demonstrates basic Temporal workflow capabilities in a Spring Boot environment.

## Key Differences from Original Demo

1. **Framework Migration**: 
   - Migrated from Quarkus to Spring Boot
   - Leverages Spring's robust ecosystem and enterprise features
   - Uses Spring Data JPA for data persistence

## What is Temporal?

Temporal is a distributed, scalable workflow orchestration platform that helps developers build reliable applications. It's particularly useful for:

- Long-running processes that need to be resilient to failures
- Complex business processes with multiple steps
- Distributed system coordination
- Microservices orchestration

## Temporal Components in Detail

### 1. Workflows
In this project, workflows orchestrate the entire patient onboarding process:

```java
@WorkflowInterface
public interface OnboardingWorkflow {
    @WorkflowMethod
    void onboardNewPatient(Patient patient);
}
```

Key workflow features used:
- **State Management**: Maintains patient state throughout the onboarding process
- **Error Handling**: Handles failures in hospital/doctor assignment
- **Compensation Logic**: Rolls back changes if onboarding fails
- **Retry Policies**: Automatically retries failed activities
- **Timeouts**: Manages timeouts for long-running operations

### 2. Activities
Activities are the individual tasks that make up our workflow:

```java
@ActivityInterface
public interface OnboardingActivities {
    void assignHospital(Patient patient);
    void assignDoctor(Patient patient);
    void notifyPatient(Patient patient);
}
```

Our key activities:
- **Hospital Assignment**: Matches patients with hospitals based on location
- **Doctor Assignment**: Assigns appropriate doctors based on medical condition
- **Patient Notification**: Sends confirmation to patients
- **Data Persistence**: Handles database operations

### 3. Workers
Workers execute the workflow and activity code:

```java
@Component
public class OnboardingWorker {
    private final Worker worker;
    
    public OnboardingWorker(WorkflowClient client) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        worker = WorkerFactory.newWorker(client, "OnboardingTaskQueue");
        worker.registerWorkflowImplementationTypes(OnboardingWorkflowImpl.class);
        worker.registerActivitiesImplementations(new OnboardingActivitiesImpl());
    }
}
```

Worker responsibilities:
- Polls task queues for work
- Executes workflow logic
- Performs activities
- Reports results back to Temporal server

### 4. Task Queues
Task queues coordinate work distribution:

- **OnboardingTaskQueue**: Main queue for onboarding workflows
- **NotificationTaskQueue**: Handles patient notifications
- **DataTaskQueue**: Manages database operations

### 5. Workflow Client
The client initiates workflows:

```java
@Service
public class OnboardingService {
    private final WorkflowClient workflowClient;
    
    public void startOnboarding(Patient patient) {
        OnboardingWorkflow workflow = workflowClient.newWorkflowStub(
            OnboardingWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("OnboardingTaskQueue")
                .build());
        workflow.onboardNewPatient(patient);
    }
}
```

### Implementation Flow

1. **Request Handling**:
   ```
   HTTP Request → Controller → Workflow Client → Temporal Server
   ```

2. **Workflow Execution**:
   ```
   Temporal Server → Worker → Workflow Implementation → Activities
   ```

3. **Activity Execution**:
   ```
   Activity → Business Logic → Database/External Services → Result
   ```

4. **Error Handling**:
   ```
   Error → Retry Policy → Compensation Logic → Final Status
   ```

### Resilience Features

1. **Automatic Retries**:
   - Hospital service failures
   - Database connection issues
   - Network timeouts

2. **State Recovery**:
   - Workflow state persisted
   - Recovers from worker crashes
   - Maintains consistency

3. **Compensation**:
   - Rollback hospital assignment
   - Cancel doctor appointments
   - Notify patient of failures

## Project Structure

```
onboard-patient-temporal/
├── docker-compose.yml           # PostgreSQL configuration
├── onboarding-app/             # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── org/acme/patient/onboarding/
│   │   │   │       ├── controller/    # REST endpoints
│   │   │   │       ├── workflow/      # Workflow definitions
│   │   │   │       └── model/         # Data models
│   │   │   └── resources/
│   │   │       └── application.yml    # App configuration
│   └── pom.xml
├── onboarding-service/         # Service module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── org/acme/patient/onboarding/
│   │   │   │       ├── service/       # Business logic
│   │   │   │       ├── repository/    # Data access
│   │   │   │       └── model/         # Domain models
│   │   │   └── resources/
│   │   │       └── application.yml    # Service configuration
│   └── pom.xml
└── pom.xml                     # Parent POM
```

## Migration from Quarkus to Spring Boot

This project has been migrated from Quarkus to Spring Boot while maintaining the core Temporal workflow concepts. Here are the key differences and improvements:

1. **Framework Migration**:
   - Original: Used Quarkus with its reactive and cloud-native features
   - Current: Uses Spring Boot with its robust ecosystem and extensive integration capabilities

2. **Configuration**:
   - Original: Used `application.properties` with Quarkus-specific settings
   - Current: Uses `application.yml` with Spring Boot configuration format

3. **Dependency Management**:
   - Original: Used Quarkus BOM and extensions
   - Current: Uses Spring Boot starters and dependencies

4. **Database Access**:
   - Original: Used Panache for simplified entity management
   - Current: Uses Spring Data JPA for database operations

5. **API Documentation**:
   - Original: Used Quarkus Swagger UI
   - Current: Uses SpringDoc OpenAPI for API documentation

6. **Core Workflow Logic**:
   The core Temporal workflow logic remains similar, demonstrating the framework-agnostic nature of Temporal:
   - Workflow Definition
   - Activity Implementation
   - Task Queue Management
   - Error Handling and Retries
   - Compensation Logic

## Communication Flow

1. Client sends request to `onboarding-app` REST endpoint
2. `onboarding-app` initiates Temporal workflow
3. Workflow orchestrates activities through workers
4. Activities communicate with `onboarding-service` for business operations
5. `onboarding-service` handles database operations and business logic
6. Results flow back through the chain to the client

## Prerequisites

- Java 21
- Maven
- Docker and Docker Compose
- PostgreSQL

## Setup and Running

1. **Start Temporal Server**:
   ```bash
   # Clone Temporal docker-compose repository
   git clone https://github.com/temporalio/docker-compose.git temporal
   cd temporal
   
   # Start Temporal server
   docker-compose up -d
   ```

2. **Start PostgreSQL**:
   ```bash
   # From the project root directory
   docker-compose up -d
   ```

3. **Start Onboarding Service**:
   ```bash
   cd onboarding-service
   mvn spring-boot:run
   ```

4. **Start Onboarding App**:
   ```bash
   cd onboarding-app
   mvn spring-boot:run
   ```

## Testing the Application

1. Access Swagger UI: http://localhost:8082/swagger-ui.html

2. Send a test request to start onboarding:
   ```json
   {
     "id": "123",
     "name": "John Doe",
     "email": "john@example.com",
     "phone": "123-456-7890",
     "zip": "12345",
     "condition": "Diabetes",
     "contactMethod": "EMAIL"
   }
   ```

3. Monitor workflow execution in Temporal UI: http://localhost:8233

## Ports

- Temporal Server: 7233
- Temporal Web UI: 8233
- Onboarding Service: 8081
- Onboarding App: 8082
- PostgreSQL: 5432

## Database Schema

The system uses PostgreSQL with the following key tables:

```sql
-- Patients table
CREATE TABLE patients (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    zip VARCHAR(10),
    condition VARCHAR(255),
    contact_method VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Hospitals table
CREATE TABLE hospitals (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    zip VARCHAR(10),
    created_at TIMESTAMP
);

-- Doctors table
CREATE TABLE doctors (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255),
    hospital_id UUID REFERENCES hospitals(id),
    created_at TIMESTAMP
);

-- Onboarding_records table
CREATE TABLE onboarding_records (
    id UUID PRIMARY KEY,
    patient_id UUID REFERENCES patients(id),
    hospital_id UUID REFERENCES hospitals(id),
    doctor_id UUID REFERENCES doctors(id),
    status VARCHAR(50),
    started_at TIMESTAMP,
    completed_at TIMESTAMP
);
```

## Configuration

### Database Configuration (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/patient_onboarding
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

temporal:
  service-address: localhost:7233
  namespace: default
```

## Quick Start

1. **Start Required Services**:
   ```bash
   # Start Temporal Server and PostgreSQL
   docker-compose up -d
   ```

2. **Initialize Database**:
   ```bash
   # Database migrations will run automatically on startup
   # Initial data can be loaded via:
   curl -X POST http://localhost:8082/api/v1/admin/init-data
   ```

3. **Start Applications**:
   ```bash
   # Start Onboarding Service (handles business logic and data)
   cd onboarding-service
   ./mvnw spring-boot:run

   # Start Onboarding App (handles workflows and API)
   cd onboarding-app
   ./mvnw spring-boot:run
   ```

4. **Access Services**:
   - Temporal UI: http://localhost:8233
   - API Documentation: http://localhost:8082/swagger-ui.html
   - Health Check: http://localhost:8082/actuator/health

## API Examples

### Start Patient Onboarding
```bash
curl -X POST http://localhost:8082/api/v1/patients/onboard \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "123-456-7890",
    "zip": "30041",
    "condition": "Diabetes",
    "contactMethod": "EMAIL"
  }'
```

### Check Onboarding Status
```bash
curl http://localhost:8082/api/v1/patients/{patientId}/onboarding-status
```
