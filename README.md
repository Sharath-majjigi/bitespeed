# Identity Reconciliation API

## Overview

This project provides a solution for identity reconciliation, helping Bitespeed link different orders made with different contact information to the same person. This ensures a personalized customer experience by maintaining a consistent customer identity across multiple purchases.

## Features

- Identify and link customer identities based on email and phone numbers.
- Ensure the oldest contact is treated as "primary" and the rest as "secondary".
- Seamless integration with FluxKart.com to enhance their customer experience.

## Technologies Used

- **Programming Language**: Java
- **Framework**: Spring Boot
- **Database**: MySQL
- **Containerization**: Docker
- **Orchestration**: Docker Compose

## API Endpoints

### 1. Identify Customer Contacts
- **URL**: `/identify`
- **Method**: POST
- **Description**: Links customer contacts based on email or phone number.
- **Request Body**:

  ```json
  {
      "email" : "sharath.majjigi@gmail.com",
      "phone_number" : "7654"
  }


## Response:
```json
{
    "contact": {
        "primary_contact_id": 4,
        "emails": [
            "sharath.majjigi@gmail.com"
        ],
        "phone_numbers": [
            "7654"
        ],
        "secondary_contact_ids": []
    }
}
```
## Running the Application

### Prerequisites
- Docker installed on your machine

### Steps

1. **Clone the repository**:
    ```bash
    git clone https://github.com/Sharath-majjigi/bitespeed.git
    cd bitespeed
    ```

2. **Build and run the Docker containers**:
    ```bash
    docker-compose up --build
    ```

3. **Access the API**:
    - The API will be accessible at [http://localhost:5001](http://localhost:5001)

## Conclusion

This application provides a robust solution for identity reconciliation, ensuring a seamless and personalized experience for customers on FluxKart.com. By leveraging Docker and Docker Compose, it is easy to set up and run the application in any environment.
