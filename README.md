# Weather Application (Root Project)

This project is a full-stack weather application that retrieves and displays detailed weather forecasts based on user-entered addresses.

The Java-based backend API fetches current weather data (including current temperature and condition) and today's forecast data (including today's high/low temperatures and predicted condition) using external APIs (Google Geocoding API and WeatherAPI).

The React frontend provides the user interface for interacting with the API.

## Further Implementation

The backend can be extended to include hourly or multi-day forecasts by parsing additional data from the WeatherAPI's `/forecast` endpoint and incorporating it into the weather object. The UI can then be updated to display this extended forecast data in separate tables.


## Project Structure
```
weather-app/
├── backend/
│   ├── (Backend files and folders)
│   └── README.md (Backend README)
├── frontend/
│   ├── (Frontend files and folders)
│   └── README.md (Frontend README)
└── README.md (This file)
```

* **`backend/`:** Contains the Java 21 Gradle-based backend API that fetches weather data.
* **`frontend/`:** Contains the React frontend application that displays weather information.

## Overview

The application allows users to enter an address and retrieve corresponding weather data. The backend API handles the retrieval of weather information using external APIs (Google Geocoding API and WeatherAPI), while the frontend application provides a user interface for interacting with the API.

## Getting Started

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/AnandTati/weather-app.git
    cd <root_project_directory>
    ```
2.  **Backend Setup:**
    * Navigate to the `backend` directory: `cd backend`
    * Follow the instructions in the [backend README](backend/README.md) to build and run the backend application.
    * Ensure the backend API is running on `http://localhost:8080`.
3.  **Frontend Setup:**
    * Navigate to the `frontend` directory: `cd ../frontend`
    * Follow the instructions in the [frontend README](frontend/README.md) to install dependencies and run the frontend application.
    * The frontend application will run on `http://localhost:3000`.
4.  **Access the Application:**
    * Open your browser and navigate to `http://localhost:3000` to access the frontend application.

## Detailed Information

* **Backend Details:** For detailed information about the backend API, including build instructions, API usage, and deployment, please refer to the [backend README](backend/README.md).
* **Frontend Details:** For detailed information about the React frontend application, including installation, usage, and deployment, please refer to the [frontend README](frontend/README.md).

## Notes

* Ensure the backend API is running before starting the frontend application.
* The backend API must be configured to allow CORS requests from `http://localhost:3000`.
* API keys for Google Geocoding API and WeatherAPI are required for the backend application.
* Node.js and Java 21 are required for the frontend and backend applications, respectively.