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
    cd CurrencyAccounts
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
The API documentation (and UI testing tool) is available at:
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- If the link does not work, you can directly access it using the following URL: `http://localhost:8080/swagger-ui/index.html`

## Class Generation
The classes in this project are generated from a Openapi YAML file. This ensures that the client and server models stay in sync with the API definitions.

## Usage
Examples of how to use the API are available in the Swagger UI documentation. The API specification is provided in YAML format located at `src/main/resources/openapi/openapi.yaml`.

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

## Authors
- Kacper
