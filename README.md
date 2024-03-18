# Expense tracker
A simple web-app for recording daily expense and income (referred as **"cash flow entry/entries"** below), with additional features like summary charts and data export (.csv format).

> Disclaimer:\
This is a personal project created for self-educational purposes only. The project is not intended for commercial use and should not be used for any business purposes. The sample data provided in the project is for demonstration purposes only and should not be used in a production environment.

## Current progress
- [x] user authn and authz
- [x] API - entry management (CRUD of cash flow entries)
- [ ] API - category management (CRUD of sub-categories)
- [ ] API - summary
- [ ] API - data export feature
- [ ] Frontend

## Documents and diagrams
* [architecture diagram](https://drive.google.com/file/d/1q8qgHq0GXpBl41hYh5B6d29jk3lLbQMD/view?usp=sharing)
* [flow chart: user authn and authz](https://drive.google.com/file/d/1SFnOpeTubxwxClAftG0mQcrLqShgxnIM/view?usp=sharing)
* [database schema diagram](https://dbdiagram.io/d/expense-tracker-schemas-65e6f6facd45b569fb8f7bc2)
* [user stories and feature docs](https://docs.google.com/document/d/10onw7LpOAhK6mIHp1EcKWzmyNLCif9wRY8lE8zEtit8/edit?usp=sharing)

## Features
* User registration and login with JWT authentication
* Role-based authorization with Spring Security
* CRUD of expense and income entries
* Default main-categories and customizable sub-categories
* (coming soon) Monthly and Quarterly summary with charts
* (coming soon) export data to csv

## Technologies
### Backend:
* Spring Boot 3.0
* Spring Security
* JSON Web Tokens (JWT)
* PostgreSQL
* (coming soon) Redis
* flyway for managing migrations of database
### Frontend:
* HTML5 + CSS3 + TailwindCSS + DaisyUI
* React + TypeScript
* React Router
* Tanstack Query

## Getting Started
**Pre-requisites:**
* JDK 17+
* Maven 3+
* PostgreSQL 15+

**To run this project locally, follow these steps:**
* Clone the repository
* Create a database with name "expense-tracker-dev" in postgres
* Install project dependencies: `mvn clean install`
* Copy {root}/src/main/resources/config/application-example.yml and rename to application.yml, then provide values to the properties
* Run the project: `mvn spring-boot:run`