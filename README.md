# Currency Accounts

## Description
The goal of this project is to implement a service for registering bank accounts with subaccounts in PLN and USD. The service allows for account creation and currency exchange between subaccounts in the PLN <--> USD pair.

## Requirements
- Java 17
- Maven 3.8.1

## Installation
Instructions for installing the project:

1. Clone the repository:
    ```sh
    git clone https://github.com/zkacper97/CurrencyAccounts.git
    ```
2. Navigate to the project directory:
    ```sh
    cd currency-accounts
    ```
3. Install dependencies and build the project:
    ```sh
    mvn clean install
    ```

## Running the Application
Instructions for running the project:

1. Start the application:
    ```sh
    mvn spring-boot:run
    ```

Once started, the application will be available at `http://localhost:8080`.

## API Documentation
The API documentation is available at:
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

## Testing
Instructions for running tests:

1. Run unit tests:
    ```sh
    mvn test
    ```

2. Run integration tests:
    ```sh
    mvn verify
    ```

## Usage
Examples of how to use the API are available in the Swagger UI documentation. The API specification is provided in YAML format located at `src/main/resources/openapi/openapi.yaml`.

## Authors
- Kacper