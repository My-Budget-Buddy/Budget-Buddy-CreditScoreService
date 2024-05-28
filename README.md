## Credit Score Microservice

## Project Description

This microservice is designed to manage and calculate credit scores for users within a larger financial application. It provides endpoints for saving and updating credit data, calculating FICO scores, retrieving credit history, generating credit reports, and offering personalized credit improvement tips.

## Technologies Used
![](https://img.shields.io/badge/-Java-007396?style=flat-square&logo=java&logoColor=white)
![](https://img.shields.io/badge/-Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/-Spring_Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white)
![](https://img.shields.io/badge/-PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![JUnit](https://img.shields.io/badge/-JUnit-25A162?style=flat-square&logo=junit5&logoColor=white)
![Docker](https://img.shields.io/badge/-Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/-AWS-232F3E?style=flat-square&logo=amazon-aws&logoColor=white)
![Maven](https://img.shields.io/badge/-Maven-C71A36?style=flat-square&logo=apache-maven&logoColor=white)
![Eureka](https://img.shields.io/badge/-Eureka-239D60?style=flat-square&logo=spring&logoColor=white)
![Microservices](https://img.shields.io/badge/-Microservices-000000?style=flat-square&logo=cloud&logoColor=white)

## Features

Save and Update Credit Data: Store and modify user credit information, including payment history, credit utilization, debt, and account details.
Calculate FICO Scores: Compute FICO credit scores based on user credit data using a simplified model.
Retrieve Credit History: Access historical records of calculated credit scores for a user.
Generate Credit Reports: Create comprehensive credit reports summarizing user credit data and history.
Credit Improvement Tips: Provide personalized suggestions for improving credit scores based on individual credit profiles.

##Getting Started

Clone the Repository: git clone <repository_url>
Configure Database: Set up a PostgreSQL database and update connection details in application.properties.
Build and Run: Use Maven or your preferred build tool to compile and run the application.
Usage

## The microservice exposes the following RESTful API endpoints:

GET /api/credit/score: Calculate and retrieve the FICO credit score for a user. Requires a User-ID header.
POST /api/credit/data: Save new credit data for a user.
GET /api/credit/history: Get the credit score history for a user. Requires a User-ID header.
PUT /api/credit/data: Update existing credit data for a user. Requires a User-ID header.
GET /api/credit/report: Generate a detailed credit report for a user. Requires a User-ID header.
GET /api/credit/tips: Get personalized credit improvement tips for a user. Requires a User-ID header.
 Note: Replace placeholders like <repository_url> with actual values.

## Contributors

Anish Murthy

## License

 This project is licensed under the MIT License.
