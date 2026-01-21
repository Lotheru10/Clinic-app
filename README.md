# Clinic-app

**Clinic App** is a team-developed web application for managing a medical clinic.
It supports patient registration, doctor schedules, appointment booking, and core clinic workflows, with a strong focus on clean backend architecture and RESTful API design.
The project was developed primarily from a backend perspective, aiming to reflect real-world backend challenges such as data consistency, clear API boundaries, and maintainable domain logic.

## My Responsibilities
My primary responsibility was backend development, including RESTful API design, business logic implementation, and backend architecture.

## Personal Extension

This repository includes an independent extension developed after the original team project was completed.

The extension introduces a review and moderation system, focusing on backend-driven business rules such as
review eligibility, content validation, and automated user-level enforcement.


## Features

- Patient registration and management  
- Doctor schedules and availability
- Appointment booking and cancellation
- Backend-driven business logic
- RESTful API for frontend communication
  
**Reviews & Moderation** *(personal extension)*
- Patients can leave reviews after completed appointments  
- Automated content moderation prevents the use of prohibited language  
- Repeated violations result in account removal after exceeding a defined threshold

## Tech Stack

**Backend**
- Java  
- Spring Boot  
- Hibernate (JPA)  

**Frontend**
- React (Vite)

## Architecture Overview

The application follows a layered backend architecture:
- Controllers expose REST endpoints  
- Services encapsulate business logic  
- Repositories handle data persistence using JPA  

This separation improves readability, testability, and long-term maintainability.

## Project Structure

This repository is organized as a simple monorepo:

- `src/` – Spring Boot backend application
- `frontend/` – React frontend (Vite)
- Gradle is used for backend build and dependency management


## How to run
Clone the repository:

```bash
git clone https://github.com/Lotheru10/clinic-app.git
```

**Backend**

- ./gradlew bootRun

**Frontend**

- cd frontend
- npm install
- npm run dev
