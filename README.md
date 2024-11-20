# Transportes de Portugal - Digital Platform

## Project Overview
This project is a digital platform developed for **Transportes de Portugal**, a public rail transportation company. The platform aims to provide customers with an efficient and modern experience for purchasing tickets, accessing train schedules, managing profiles, and leaving service reviews.

The platform is built using a **microservices architecture** to ensure scalability, maintainability, and modularity. Each service is implemented as a separate module within a unified Maven project.

## Key Features
- **Customer Management**: Handles user registration, authentication (with Keycloak integration), profile management, and purchase history.
- **Itinerary Management**: Manages train routes, schedules, and seat availability, with frequently accessed data cached using Redis.
- **Ticketline**: Allows users to purchase tickets online, integrates with payment systems, and manages e-tickets.
- **Notifications**: Sends email or SMS alerts for ticket confirmations and itinerary changes.
- **Evaluation Management**: Enables customers to leave reviews and rate the service.

## Architecture
- **Backend Framework**: Spring Boot
- **Database**: PostgreSQL for relational data storage
- **Cache**: Redis for frequently accessed itinerary data
- **Security**: Keycloak for authentication and role-based access control
- **Containerization**: Docker for service containerization
- **Orchestration**: Kubernetes (Minikube) for managing the deployment of microservices
- **Communication**: RESTful APIs for synchronous interactions, RabbitMQ/Kafka for asynchronous messaging (as needed)

## Project Modules
This project uses a multi-module Maven structure:
1. **customer-management**: Handles user-related functionality, including authentication.
2. **itinerary-management**: Manages train schedules and seat availability, integrates Redis caching.
3. **ticketline**: Facilitates ticket purchases and manages payment processing.
4. **notifications**: Sends email/SMS alerts to users.
5. **evaluation-management**: Collects and manages customer reviews.

## Technologies Used
- **Java** (JDK 17)
- **Spring Boot** (3.x)
- **PostgreSQL** for database management
- **Redis** for caching frequently accessed data
- **Keycloak** for security and user management
- **Docker** for containerization
- **Kubernetes (Minikube)** for orchestration

## How to Run the Project
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo/transportes-de-portugal.git
   cd transportes-de-portugal

2. **Build the project**: Open a terminal in the project root directory and execute:
   ```bash
   mvn clean install

3. **Run individual services**: Navigate to the specific module folder (e.g., customer-management) and run:
   ```bash
   mvn spring-boot:run

4. **Dockerize the services**: After ensuring the services are running correctly, create Docker images:
   ```bash
   docker build -t service-name .

5. **Deploy to Kubernetes**: Deploy the services using Kubernetes manifests. Start Minikube, then apply the YAML files:
   ```bash
   kubectl apply -f path/to/your/kubernetes-manifests/
