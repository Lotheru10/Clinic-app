# Clinic-app

**Clinic App** is a team-developed web application for managing a medical clinic.
It supports patient registration, doctor schedules, appointment booking, and core clinic workflows, with a strong focus on clean backend architecture and RESTful API design.
The project was developed primarily from a backend perspective, aiming to reflect real-world backend challenges such as data consistency, clear API boundaries, and maintainable domain logic.

## Features

- Patient registration and management  
- Doctor schedules and availability
- Appointment booking and cancellation  
- Backend-driven business logic
- RESTful API for frontend communication

## My Responsibilities
My primary responsibility was backend development, including RESTful API design, business logic implementation, and backend architecture.

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
clone a repository

**Backend**

- ./gradlew bootRun

**Frontend**

- cd frontend
- npm install
- npm run dev
