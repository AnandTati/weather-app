import React, { useState } from 'react';
import axios from 'axios';
import styles from './Weather.module.css'

function Weather() {
  const [address, setAddress] = useState('');
  const [weatherData, setWeatherData] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  // Function to handle the form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setWeatherData(null);

    try {
      const response = await axios.get(`http://localhost:8080/weather`, {
        params: { address }
      });

      if (response.status === 200) {
        console.log(response.data)
        setWeatherData(response.data);
      } else {
        setError('Failed to fetch weather data. Please try again.');
      }
    } catch (err) {
      console.error(err);
      if (err.response) {
        if (err.response.status === 404) {
          setError('Address not found or ambigious address or invalid address.');
        } else if (err.response.status === 400) {
          setError('Invalid address provided.');
        } else {
          setError('An error occurred while fetching weather data.');
        }
      } else {
        setError('Network error or server is down.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <h1>Weather Information</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
            <label htmlFor='address'>Enter an address</label>
            <input
                type="text"
                className="form-control"
                id="address"
                placeholder="Enter an address"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
            />
        </div>
        <button type="submit" disabled={loading}>Get Weather</button>
      </form>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {weatherData && (
        <div className="card mt-4 shadow">
        <div className="card-body">
          <h5 className="card-title text-primary">Weather Information</h5>
          <p className="card-text font-weight-bold">Address: {weatherData.location.formattedAddress}</p>
      
          <table id="weather-table" className={`table table-striped table-bordered ${styles["weather-table"]}`}>
            <tbody>
              <tr>
                <th scope="row" className={`align-middle ${styles.leftAligned}`}>Temperature</th>
                <td className="align-middle text-start d-flex align-items-center">{weatherData.current.temp_f}°F</td>
              </tr>
              <tr>
                <th scope="row" className="align-middle">Condition</th>
                <td className="align-middle text-start d-flex align-items-center">
                  {weatherData.current.condition.text}
                  <img
                    src={weatherData.current.condition.icon}
                    alt={weatherData.current.condition.text}
                    className="ms-2"
                    style={{ width: "30px", height: "30px" }}
                  />
                </td>
              </tr>
              <tr>
                <th scope="row" className="align-middle text-start">Max Temp</th>
                <td className="align-middle text-start d-flex align-items-center">{weatherData.forecast.maxtemp_f}°F</td>
              </tr>
              <tr>
                <th scope="row" className="align-middle text-start">Min Temp</th>
                <td className="align-middle text-start d-flex align-items-center">{weatherData.forecast.mintemp_f}°F</td>
              </tr>
              <tr>
                <th scope="row" className="align-middle text-start">From Cache</th>
                <td className="align-middle text-start d-flex align-items-center">{weatherData.fromCache ? "Yes" : "No"}</td>
              </tr>
            </tbody>
          </table>

        </div>
      </div>
      )}
    </div>
  );
}

export default Weather;
