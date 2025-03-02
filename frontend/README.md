# Weather Frontend Application

This is a React application that displays weather information based on an address entered by the user. It communicates with a backend API to fetch weather data.

## Prerequisites

* **Node.js:** Ensure Node.js (version 23.7.0 or later) is installed on your system.
* **npm or Yarn:** You'll need npm (Node Package Manager) or Yarn to manage dependencies.

## Installation

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/AnandTati/weather-app.git
    cd <frontend_project_directory>/frontend/
    ```
2.  **Install Dependencies:**
    * Using npm:
        ```bash
        npm install
        ```
    * Using Yarn:
        ```bash
        yarn install
        ```

## Running the Application

1.  **Start the Development Server:**
    * Using npm:
        ```bash
        npm start
        ```
    * Using Yarn:
        ```bash
        yarn start
        ```
2.  **Open the Application:**
    * The application will open in your default browser at `http://localhost:3000`.

## Application Details

* **Component:** `Weather.js`
    * This is the main component that handles user input, API calls, and displaying weather data.
    * It uses `axios` to make HTTP requests to the backend API.
    * It manages state for the address, weather data, errors, and loading status.
* **API Interaction:**
    * The application makes a GET request to `http://localhost:8080/weather` with the address as a query parameter.
    * It displays weather data in a table format upon successful API response.
    * It displays error messages when API call fails.
* **Styling:**
    * Uses bootstrap classes for styling.

## Usage

1.  **Enter an Address:** Type an address into the input field.
2.  **Get Weather:** Click the "Get Weather" button.
3.  **View Weather Data:** The weather information will be displayed in a table if the address is valid.
4.  **Error Handling:** If there's an error (e.g., invalid address, API failure), an error message will be displayed.
5.  **Loading State:** A "Loading..." message is shown while the API request is in progress.

## Error Handling

* **400 Bad Request:** "Invalid address provided."
* **404 Not Found:** "Address not found or ambigious address or invalid address."
* **Other Errors:** "An error occurred while fetching weather data." or "Network error or server is down."
* Error messages are displayed in red text below the form.

## Deployment

* **Build the Application:**
    * Using npm:
        ```bash
        npm run build
        ```
    * Using Yarn:
        ```bash
        yarn build
        ```
    * This will create a `build` directory containing the production-ready application.
* **Serve the Build:**
    * You can use a static file server (e.g., `serve`, Nginx) to serve the contents of the `build` directory.
    * Example using `serve`:
        ```bash
        npm install -g serve
        serve -s build
        ```
    * Deploy the contents of the `build` folder to your preferred hosting platform.

## Backend API Dependency

* This frontend application depends on a backend API running at `http://localhost:8080/weather`.
* Ensure the backend API is running before using the frontend application.
* The backend API must be configured to allow CORS requests from `http://localhost:3000`.

## Notes

* This application uses `axios` for making HTTP requests.
* Basic error handling is implemented to display user-friendly error messages.
* The application assumes the backend API returns weather data in a specific JSON format.
* The weather data is displayed in a table format.
* This application does not contain unit tests.
* This application contains only one component.