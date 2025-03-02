# Weather API Service

This project provides weather forecasts based on address input. It uses external services to determine the location of the address and then retrieves the corresponding weather information.

## Prerequisites

* **Java:** Version 21.0.2 (or later) is required.
* **Gradle:** Version 8.12.1 (or later) is recommended.
* **API Keys:** You will need API keys for:
    * Google Geocoding API
    * WeatherAPI

### Key Dependencies

* **Spring Boot:** Used for web application development and management. Version 3.4.1.
* **JSON:** Used for processing JSON data from APIs. Version 20250107.
* **Guava:** Used for caching weather data to improve performance. Version 33.4.0-jre.
* **Lombok:** Used for reducing boilerplate code. Version: latest (1.18.36)

## Environment Setup

1.  **Obtain API Keys:**
    * **Google Maps Platform API Key (for Geocoding API):**
        * For instructions on obtaining your Google Maps Platform API key, please refer to this link: [Google Maps Platform API Key Instructions]
        * Go to the [Google Cloud Console].
        * Create or select a project.
        * Navigate to **Google Maps Platform > Credentials**.
        * On the **Credentials** page, click **Create credentials > API key**.
        * The **API key created** dialog displays your newly created API key.
        * Click **Close**.
        * The new API key is listed on the **Credentials** page under **API keys**.
        * **Remember to restrict the API key before using it in production.**
        * Enable the "Geocoding API" for your project.
    * **WeatherAPI Key:** Sign up for an account at [WeatherAPI.com] to obtain your API key.
2.  **Set Environment Variables:**
    * For Linux/macOS:
        ```bash
        export GOOGLE_API_KEY="your_google_api_key"
        export WEATHER_API_KEY="your_weather_api_key"
        ```
    * For Windows (Command Prompt):
        ```cmd
        set GOOGLE_API_KEY=your_google_api_key
        set WEATHER_API_KEY=your_weather_api_key
        ```
    * For Windows (PowerShell):
        ```powershell
        $env:GOOGLE_API_KEY = "your_google_api_key"
        $env:WEATHER_API_KEY = "your_weather_api_key"
        ```
    * Alternatively, you can add these variables to your system's environment variables for persistent storage.
    * **Configuration Management:**
        * For simplicity, API keys are read from environment variables. For production, consider using more secure methods like secret management tools or cloud provider configuration services.
    * **Application Startup Failure:**
        * The application will **fail to start** if either `GOOGLE_API_KEY` or `WEATHER_API_KEY` environment variables are not set.
        * The application will print an error message to the console indicating the missing API key(s) and then exit.
        * This behavior prevents the application from running without the necessary credentials, ensuring that external API calls are not made without proper authorization.

## Project Structure

Markdown

# Weather API Service

This project is a Java 21 Gradle-based application that provides weather forecasts based on address input. It utilizes Google's Geocoding API to convert addresses into geographical coordinates and then uses the WeatherAPI to fetch the weather forecast.

## Prerequisites

* **Java 21:** Ensure you have Java 21 installed on your system.
* **Gradle:** Gradle is used for building and managing the project.
* **API Keys:** You will need API keys for:
    * Google Geocoding API
    * WeatherAPI

## Environment Setup

1.  **Obtain API Keys:**
    * Sign up for a Google Cloud Platform account and enable the Geocoding API to get your Google API key.
    * Sign up for an account at WeatherAPI.com to get your WeatherAPI key.
2.  **Set Environment Variables:**
    * For Linux/macOS:
        ```bash
        export GOOGLE_API_KEY="your_google_api_key"
        export WEATHER_API_KEY="your_weather_api_key"
        ```
    * For Windows (Command Prompt):
        ```cmd
        set GOOGLE_API_KEY=your_google_api_key
        set WEATHER_API_KEY=your_weather_api_key
        ```
    * For Windows (PowerShell):
        ```powershell
        $env:GOOGLE_API_KEY = "your_google_api_key"
        $env:WEATHER_API_KEY = "your_weather_api_key"
        ```
    * Alternatively, you can add these variables to your system's environment variables for persistent storage.
    * **Configuration Management:**
        * For simplicity, API keys are read from environment variables. For production, consider using more secure methods like secret management tools or cloud provider configuration services.
    * **Application Startup Failure:**
        * The application will **fail to start** if either `GOOGLE_API_KEY` or `WEATHER_API_KEY` environment variables are not set.
        * The application will print an error message to the console indicating the missing API key(s) and then exit.
        * This behavior prevents the application from running without the necessary credentials, ensuring that external API calls are not made without proper authorization.

## Project Structure
```
weather-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/weatherapp/
│   │   │       ├── configuration/
│   │   │       │   └── AppConfiguration.java
│   │   │       ├── controller/
│   │   │       │   └── WeatherController.java
│   │   │       ├── models/
│   │   │       │   ├── Location.java
│   │   │       │   └── Weather.java
│   │   │       ├── service/
│   │   │       │   ├── AddressService.java
│   │   │       │   └── WeatherService.java
│   │   │       └── WeatherApiApplication.java
│   │   └── resources/
│   │       └── application.yml
│   ├── test/
│   │   └── java/
│   │       └── com/yourcompany/weatherapi/
│   │           ├── WeatherControllerTest.java
│   │           ├── AddressServiceTest.java
│   │           └── WeatherServiceTest.java
│   └── gradle/
│       └── wrapper/
│           └── gradle-wrapper.properties
├── .gitattributes
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle
```


## Component Details

* **`WeatherController.java`:**
    * Handles HTTP requests to the `/weather` endpoint.
    * Calls `AddressService` and `WeatherService` to retrieve weather data.
    * Returns the weather forecast as a JSON response.
* **`AddressService.java`:**
    * Uses the Google Geocoding API to convert addresses into `Location` objects.
    * Handles API calls and data parsing.
    * Returns `null` if the address does not have a zip code.
* **`WeatherService.java`:**
    * Uses the WeatherAPI to fetch weather forecasts based on latitude and longitude.
    * Creates `Weather` objects from the API response.
    * Handles API calls and data parsing.
* **`Location.java`:**
    * Represents a geographical location with latitude, longitude, and zip code.
* **`Weather.java`:**
    * Represents a weather forecast with relevant details.
* **`WeatherApiApplication.java`:**
    * Spring Boot application entry point.

## Building the Application

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/AnandTati/weather-app.git
    cd <project_directory>/backend/
    ```
2.  **Build with Gradle:**
    ```bash
    ./gradlew clean build
    ```
    * This command will download dependencies, compile the code, and create a JAR file in the `build/libs` directory.

## Running the Application

1.  **Run the JAR File:**
    ```bash
    java -jar build/libs/<your_jar_file_name>.jar
    ```
2.  **Application Details:**
    * The application runs on `localhost` and port `8080` by default.
    * The API endpoint is `/weather?address=xyz`, where `xyz` is the address you want to check.

## API Usage

* **Endpoint:** `/weather?address=<address>`
* **Method:** GET
* **Example:** `http://localhost:8080/weather?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA`

### Request Parameters

* `address` (string, required): The address for which to retrieve weather information.

### Response Scenarios (JSON Format)

**1. Success (200 OK):**

* **Request:**
    * `GET /weather?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA`

* **Response:**

    ```json
    {
      "location": {
        "lat": 37.4220,
        "lng": -122.0841,
        "zip": "94043"
      },
      "current": {
        "last_updated_epoch": 1700000000,
        "temp_c": 20.5,
        "temp_f": 68.9,
        "condition": {
          "text": "Partly cloudy",
          "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png",
          "code": 1003
        }
      },
      "forecast": {
        "mintemp_c": 18.0,
        "mintemp_f": 64.4,
        "maxtemp_c": 25.0,
        "maxtemp_f": 77.0,
        "condition": {
          "text": "Sunny",
          "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
          "code": 1000
        }
      },
      "from_cache": false
    }
    ```

**2. Bad Request (400 Bad Request):**

* **Request:**
    * `GET /weather?address=` (empty address)

* **Response:**
    * Status Code: 400 Bad Request
    * Body: `null`

**3. Not Found (404 Not Found):**

* **Request:**
    * `GET /weather?address=Invalid+Address`

* **Response:**
    * Status Code: 404 Not Found
    * Body: `null`

**4. Service Unavailable (503 Service Unavailable):**

* **Request:**
    * `GET /weather?address=Valid+Address` (but WeatherAPI or Geocoding API fails)

* **Response:**
    * Status Code: 503 Service Unavailable
    * Body: `null`

**5. Client Error (4xx, e.g., 401 Unauthorized, 403 Forbidden):**

* **Request:**
    * `GET /weather?address=Valid+Address` (but external API returns a client error)

* **Response:**
    * Status Code: (e.g., 401, 403, 404 from external API)
    * Body: `null`

**6. Internal Server Error (500 Internal Server Error):**

* **Request:**
    * `GET /weather?address=Valid+Address` (unexpected server-side exception)

* **Response:**
    * Status Code: 500 Internal Server Error
    * Body: `null`

**Notes:**

* The example JSON response is illustrative. Actual values will vary based on the address and weather conditions.
* `from_cache` will be true if the data was retrieved from the cache.
* The 4xx and 5xx error scenarios are general examples. The exact status codes and error messages may vary depending on the specific error.
* The location information will contain lat,lng and zip. If the address does not have a zip code, zip will be null.
* The condition object will contain text, icon, and code.

## Error Handling

* **Missing/Incorrect API Keys:**
    * **Startup Failure:** The application will not start if API keys are missing.
    * If the API keys are present but invalid, `AddressService` or `WeatherService` will throw exceptions during API calls.
    * `WeatherController` catches exceptions related to invalid API keys during API calls.
    * Returns a 500 Internal Server Error with a descriptive message.
* **Google Geocoding API Errors:**
    * `AddressService` handles errors from the Google Geocoding API (e.g., invalid address, API limit exceeded).
    * Returns a 500 Internal Server Error with a detailed error message.
* **WeatherAPI Errors:**
    * `WeatherService` handles errors from the WeatherAPI (e.g., invalid coordinates, API limit exceeded).
    * Returns a 500 Internal Server Error with a detailed error message.
* **Missing Zip Code:**
    * If the address geocoded by google does not have a zip code, the `AddressService` returns null, which the `WeatherController` then turns into a 400 bad request.
* **General Exceptions:**
    * Any other unexpected exceptions are caught and logged, and a 500 Internal Server Error is returned.

## Positive and Negative Scenarios

* **Positive Scenario:**
    * All environment variables are set correctly, and the application starts.
    * A valid address is provided.
    * Both Google Geocoding API and WeatherAPI return successful responses.
    * The API returns the weather forecast as a JSON response.
* **Negative Scenarios:**
    * **Missing API Keys:** The application fails to start due to missing environment variables.
    * **Invalid API Keys:** The application starts, but API calls fail due to invalid keys.
    * **Invalid Address:** The provided address cannot be geocoded.
    * **API Limit Exceeded:** The API usage limit is reached for either API.
    * **Network Issues:** Network connectivity problems prevent API calls.
    * **Invalid Coordinates:** The coordinates returned by the Geocoding API are invalid.
    * **Missing Zip Code:** The geocoded address does not contain a zip code.
    * **Internal Server Error:** Unhandled exceptions during processing.

## Debugging

* You can debug the application using your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
* Set breakpoints and run the application in debug mode.
* **Logging & Debugging:**
    * The application uses SLF4J with a simple console appender. You can configure a more robust logging setup (e.g., Logback) for file-based logging and more detailed log levels.
    * Error messages are logged to the console.

## Deployment

* **JAR File Execution:**
    * You can deploy the application by copying the JAR file (located in `build/libs/<your_jar_file_name>.jar`) to the target server.
    * Ensure Java 21 is installed on the server.
    * Run the application using the following command:
        ```bash
        java -jar <your_jar_file_name>.jar
        ```
    * Remember to set the `GOOGLE_API_KEY` and `WEATHER_API_KEY` environment variables on the server.
* **Docker:**
    * For containerized deployment, create a Dockerfile in the project's root directory:

        ```dockerfile
        FROM openjdk:21-jdk-slim
        COPY build/libs/<your_jar_file_name>.jar app.jar
        ENV GOOGLE_API_KEY="your_google_api_key"
        ENV WEATHER_API_KEY="your_weather_api_key"
        EXPOSE 8080
        ENTRYPOINT ["java", "-jar", "app.jar"]
        ```

    * Build the Docker image:
        ```bash
        docker build -t weather-api .
        ```
    * Run the Docker container, replacing `<your_google_api_key>` and `<your_weather_api_key>`:
        ```bash
        docker run -p 8080:8080 -e GOOGLE_API_KEY="your_google_api_key" -e WEATHER_API_KEY="your_weather_api_key" weather-api
        ```
    * For production, consider using Docker Compose or Kubernetes for orchestration.


## Additional Considerations

* **Logging and Monitoring (Already Implemented):**
    * **Logging (Logback):** This application uses Logback for logging. Logs are written to both the console and a file located at `/logs/app/log`. The log file rolls over after 30 days or when it reaches 100MB in size, whichever comes first. For production, consider configuring Logback to write logs to a centralized logging system (e.g., ELK stack, Splunk) and adjust rolling policies as needed.
    * **Health Checks and Metrics (Spring Boot Actuator):** This application includes Spring Boot Actuator, which provides built-in health checks and metrics.
        * Access the health status at `/actuator/health`.
        * Access detailed metrics at `/actuator/metrics`.
        * The base actuator endpoint is located at `/actuator` and provides links to all available actuator endpoints.
        * Example response of base actuator endpoint:
            ```json
            {
              "_links": {
                "self": {
                  "href": "http://localhost:8080/actuator",
                  "templated": false
                },
                "health-path": {
                  "href": "http://localhost:8080/actuator/health/{*path}",
                  "templated": true
                },
                "health": {
                  "href": "http://localhost:8080/actuator/health",
                  "templated": false
                }
              }
            }
            ```
        * These endpoints can be used for monitoring application health and performance in production environments.
* **Considerations for Larger Scopes or Production:**
    * **Caching:**
        * As of now, the application uses Guava for caching weather data locally. Since this is a demo application and for smaller usage.
        * We can consider implementing more advanced caching strategies (e.g., Redis, Memcached) for production deployments.
        * Configure cache expiration policies to balance performance and data freshness.
    * **Security:**
        * Implement proper authentication and authorization for the API.
        * Use HTTPS for all communication.
        * Restrict API key usage to specific IP addresses or domains.
        * Regularly update dependencies to patch security vulnerabilities.
    * **Health Checks:**
        * Configure load balancers and container orchestrators to use health checks.
    * **API Rate Limiting:** Implement rate limiting to prevent abuse of the API.
    * **API Documentation (Swagger/OpenAPI):** Generate API documentation using Swagger or OpenAPI to provide a comprehensive guide for developers.
    * **Continuous Integration/Continuous Deployment (CI/CD):** Set up a CI/CD pipeline to automate the build, test, and deployment process.
    * **Load Testing:** Conduct load testing to ensure the application can handle expected traffic.
    * **Disaster Recovery:** Plan for disaster recovery and implement backup and restore procedures.
    * **Database (If Applicable):** If you plan to store data (e.g., historical weather data), choose an appropriate database (e.g., PostgreSQL, MySQL, MongoDB, DynamoDB). Configure database connection pools and manage database migrations.
    * **Configuration Management:** Use configuration management tools (e.g., Spring Cloud Config, Consul, etcd) to manage application configurations in different environments. Avoid storing sensitive information (e.g., API keys, database credentials) directly in configuration files.
    * **Internationalization/Localization:** If you plan to support multiple languages, implement internationalization and localization features.
    * **Accessibility:** Ensure the API and any related documentation are accessible to users with disabilities.
    * **Cost Optimization:** Consider cost optimization strategies when deploying to cloud platforms.
    * **Communication:** create a communication strategy for any API changes.


[Google Maps Platform API Key Instructions]: https://developers.google.com/maps/documentation/geocoding/get-api-key
[Google Cloud Console]: https://console.cloud.google.com/
[WeatherAPI.com]: https://www.weatherapi.com/
