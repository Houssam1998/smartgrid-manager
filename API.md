# 🔌 API Reference - SmartGrid Manager

Complete API documentation for the SmartGrid Manager RESTful services.

## Table of Contents

- [API Overview](#api-overview)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [Response Formats](#response-formats)
- [Error Handling](#error-handling)
- [Device API](#device-api)
- [Reading API](#reading-api)
- [Code Examples](#code-examples)

## API Overview

The SmartGrid Manager exposes RESTful APIs for device and reading management. All APIs follow REST conventions and return JSON-formatted responses.

### Key Features

- **RESTful Design**: Standard HTTP methods (GET, POST, PUT, DELETE)
- **JSON Format**: All requests and responses use JSON
- **No Authentication**: Currently open API (suitable for internal networks)
- **CORS Enabled**: Cross-origin requests supported

### Technology Stack

- **JAX-RS (Jersey 3.1.5)**: REST framework
- **Jackson 2.16.1**: JSON serialization/deserialization
- **JSR310 Support**: Java 8 Date/Time API serialization

## Base URL

All API endpoints are relative to the base URL:

```
http://localhost:8080/smartgridmanager/api
```

**Example full endpoint:**
```
http://localhost:8080/smartgridmanager/api/devices
```

## Authentication

Currently, the API does not implement authentication. All endpoints are publicly accessible.

### Future Implementation

Authentication mechanisms planned for future releases:
- JWT token-based authentication
- API key authentication
- OAuth 2.0 integration

## Response Formats

### Success Response

All successful API calls return HTTP 200 (OK) with JSON data:

```json
{
  "id": 1,
  "name": "SmartMeter_001",
  "deviceType": "SmartMeter",
  "location": "Living Room"
}
```

### Error Response

Error responses include appropriate HTTP status codes and error messages:

```json
{
  "error": "Resource not found",
  "message": "Device with ID 999 does not exist",
  "status": 404,
  "timestamp": "2024-01-15T10:30:00"
}
```

## Error Handling

### HTTP Status Codes

| Status Code | Meaning |
|-------------|---------|
| 200 | OK - Request succeeded |
| 201 | Created - Resource successfully created |
| 400 | Bad Request - Invalid request parameters |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server-side error |

## Device API

The Device API manages smart home devices in the system.

### Data Model

**Device Entity:**

```java
{
  "id": Long,              // Unique device identifier
  "name": String,          // Device name (e.g., "SmartMeter_001")
  "deviceType": String,    // Device type (e.g., "SmartMeter")
  "location": String       // Device location (e.g., "Living Room")
}
```

**Device Types:**

- `SmartMeter` - Monitors power, voltage, current
- `Thermostat` - Measures temperature and humidity
- `AC_Unit` - Air conditioning control
- `LightSensor` - Luminosity measurement
- `MotionSensor` - Motion detection
- `WaterHeater` - Water heating monitoring
- `Refrigerator` - Refrigerator monitoring
- `WashingMachine` - Washing machine monitoring
- `SolarPanel` - Solar power generation
- `SecurityCamera` - Security and motion detection
- `SmokeDetector` - Smoke and CO2 detection
- `HVAC_System` - Complete HVAC monitoring

### Endpoints

#### 1. Get All Devices

Retrieves a list of all registered devices.

**Request:**

```http
GET /api/devices
Accept: application/json
```

**Response:**

```json
[
  {
    "id": 1,
    "name": "SmartMeter_001",
    "deviceType": "SmartMeter",
    "location": "Living Room"
  },
  {
    "id": 2,
    "name": "Thermostat_002",
    "deviceType": "Thermostat",
    "location": "Master Bedroom"
  },
  {
    "id": 3,
    "name": "SecurityCamera_003",
    "deviceType": "SecurityCamera",
    "location": "Front Door"
  }
]
```

**Response Fields:**

- `id` (Long): Unique device identifier
- `name` (String): Device name
- `deviceType` (String): Type of device
- `location` (String): Physical location

**Status Codes:**

- `200 OK`: Success
- `500 Internal Server Error`: Server error

**Example with cURL:**

```bash
curl -X GET http://localhost:8080/smartgridmanager/api/devices \
  -H "Accept: application/json"
```

**Example with JavaScript (Fetch API):**

```javascript
fetch('http://localhost:8080/smartgridmanager/api/devices')
  .then(response => response.json())
  .then(devices => console.log(devices))
  .catch(error => console.error('Error:', error));
```

**Example with Python (requests):**

```python
import requests

response = requests.get('http://localhost:8080/smartgridmanager/api/devices')
devices = response.json()
print(devices)
```

#### 2. Get Device by ID (Future)

*Not yet implemented. Planned for future release.*

```http
GET /api/devices/{id}
```

#### 3. Create Device (Future)

*Not yet implemented. Planned for future release.*

```http
POST /api/devices
Content-Type: application/json

{
  "name": "SmartMeter_100",
  "deviceType": "SmartMeter",
  "location": "Garage"
}
```

#### 4. Update Device (Future)

*Not yet implemented. Planned for future release.*

```http
PUT /api/devices/{id}
Content-Type: application/json

{
  "name": "SmartMeter_100_Updated",
  "deviceType": "SmartMeter",
  "location": "Basement"
}
```

#### 5. Delete Device (Future)

*Not yet implemented. Planned for future release.*

```http
DELETE /api/devices/{id}
```

## Reading API

The Reading API manages sensor readings and measurements from devices.

### Data Model

**Reading Entity:**

```java
{
  "id": Long,                    // Unique reading identifier
  "timestamp": LocalDateTime,    // Reading timestamp (ISO 8601)
  "value": Double,              // Measurement value
  "readingType": String,        // Type of reading
  "deviceId": Long              // Associated device ID
}
```

**Reading Types:**

- `power` - Power consumption in Watts (W)
- `voltage` - Voltage in Volts (V)
- `current` - Current in Amperes (A)
- `temperature` - Temperature in Celsius (°C)
- `humidity` - Humidity percentage (%)
- `luminosity` - Light level in Lux
- `motion` - Motion detection (0 or 1)
- `co2` - CO2 level in PPM

### Alert Thresholds

Readings that exceed these thresholds trigger alerts:

| Reading Type | Threshold | Condition |
|--------------|-----------|-----------|
| power | 5000.0 W | > 5000 |
| temperature | 35.0 °C | > 35 |
| voltage | 250.0 V | > 250 |
| current | 40.0 A | > 40 |
| humidity | 10% or 90% | < 10 or > 90 |
| co2 | 1000.0 PPM | > 1000 |

### Endpoints

#### 1. Get All Readings

Retrieves a list of all sensor readings.

**Request:**

```http
GET /api/readings
Accept: application/json
```

**Response:**

```json
[
  {
    "id": 1,
    "timestamp": "2024-01-15T14:30:00",
    "value": 1250.5,
    "readingType": "power",
    "deviceId": 1
  },
  {
    "id": 2,
    "timestamp": "2024-01-15T14:30:00",
    "value": 22.5,
    "readingType": "temperature",
    "deviceId": 2
  },
  {
    "id": 3,
    "timestamp": "2024-01-15T14:30:00",
    "value": 1,
    "readingType": "motion",
    "deviceId": 3
  }
]
```

**Response Fields:**

- `id` (Long): Unique reading identifier
- `timestamp` (String): ISO 8601 formatted timestamp
- `value` (Double): Measurement value
- `readingType` (String): Type of measurement
- `deviceId` (Long): ID of the device that produced this reading

**Status Codes:**

- `200 OK`: Success
- `500 Internal Server Error`: Server error

**Example with cURL:**

```bash
curl -X GET http://localhost:8080/smartgridmanager/api/readings \
  -H "Accept: application/json"
```

**Example with JavaScript (Fetch API):**

```javascript
fetch('http://localhost:8080/smartgridmanager/api/readings')
  .then(response => response.json())
  .then(readings => console.log(readings))
  .catch(error => console.error('Error:', error));
```

**Example with Python (requests):**

```python
import requests

response = requests.get('http://localhost:8080/smartgridmanager/api/readings')
readings = response.json()
print(readings)
```

#### 2. Get Reading by ID (Future)

*Not yet implemented. Planned for future release.*

```http
GET /api/readings/{id}
```

#### 3. Get Readings by Device (Future)

*Not yet implemented. Planned for future release.*

```http
GET /api/readings?deviceId={deviceId}
```

#### 4. Get Readings by Type (Future)

*Not yet implemented. Planned for future release.*

```http
GET /api/readings?type={readingType}
```

#### 5. Get Readings by Date Range (Future)

*Not yet implemented. Planned for future release.*

```http
GET /api/readings?from={startDate}&to={endDate}
```

#### 6. Create Reading (Future)

*Not yet implemented. Planned for future release.*

```http
POST /api/readings
Content-Type: application/json

{
  "timestamp": "2024-01-15T14:30:00",
  "value": 1250.5,
  "readingType": "power",
  "deviceId": 1
}
```

#### 7. Delete Reading (Future)

*Not yet implemented. Planned for future release.*

```http
DELETE /api/readings/{id}
```

## Code Examples

### JavaScript Integration

#### Fetch All Devices and Their Latest Readings

```javascript
async function getDevicesWithReadings() {
  try {
    // Fetch devices
    const devicesResponse = await fetch('http://localhost:8080/smartgridmanager/api/devices');
    const devices = await devicesResponse.json();
    
    // Fetch readings
    const readingsResponse = await fetch('http://localhost:8080/smartgridmanager/api/readings');
    const readings = await readingsResponse.json();
    
    // Group readings by device
    const deviceReadings = devices.map(device => ({
      ...device,
      readings: readings.filter(r => r.deviceId === device.id)
    }));
    
    return deviceReadings;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw error;
  }
}

// Usage
getDevicesWithReadings().then(data => {
  console.log('Devices with readings:', data);
});
```

#### Monitor Power Consumption

```javascript
async function monitorPowerConsumption() {
  const response = await fetch('http://localhost:8080/smartgridmanager/api/readings');
  const readings = await response.json();
  
  // Filter power readings
  const powerReadings = readings.filter(r => r.readingType === 'power');
  
  // Calculate statistics
  const values = powerReadings.map(r => r.value);
  const avgPower = values.reduce((a, b) => a + b, 0) / values.length;
  const maxPower = Math.max(...values);
  const minPower = Math.min(...values);
  
  console.log(`Average Power: ${avgPower.toFixed(2)} W`);
  console.log(`Max Power: ${maxPower.toFixed(2)} W`);
  console.log(`Min Power: ${minPower.toFixed(2)} W`);
  
  // Find alerts (power > 5000W)
  const alerts = powerReadings.filter(r => r.value > 5000);
  console.log(`Power Alerts: ${alerts.length}`);
}
```

### Python Integration

#### Fetch and Analyze Data

```python
import requests
import pandas as pd
from datetime import datetime

class SmartGridAPI:
    def __init__(self, base_url='http://localhost:8080/smartgridmanager/api'):
        self.base_url = base_url
    
    def get_devices(self):
        """Fetch all devices"""
        response = requests.get(f'{self.base_url}/devices')
        response.raise_for_status()
        return response.json()
    
    def get_readings(self):
        """Fetch all readings"""
        response = requests.get(f'{self.base_url}/readings')
        response.raise_for_status()
        return response.json()
    
    def analyze_power_consumption(self):
        """Analyze power consumption"""
        readings = self.get_readings()
        
        # Convert to DataFrame
        df = pd.DataFrame(readings)
        
        # Filter power readings
        power_df = df[df['readingType'] == 'power']
        
        # Statistics
        stats = {
            'mean': power_df['value'].mean(),
            'median': power_df['value'].median(),
            'std': power_df['value'].std(),
            'min': power_df['value'].min(),
            'max': power_df['value'].max()
        }
        
        # Count alerts (power > 5000)
        alerts = len(power_df[power_df['value'] > 5000])
        stats['alerts'] = alerts
        
        return stats

# Usage
api = SmartGridAPI()
devices = api.get_devices()
print(f"Total devices: {len(devices)}")

stats = api.analyze_power_consumption()
print(f"Power consumption statistics: {stats}")
```

### Java Integration

#### Consume API from Another Java Application

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SmartGridClient {
    
    private static final String BASE_URL = "http://localhost:8080/smartgridmanager/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public SmartGridClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    public List<DeviceDTO> getAllDevices() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/devices"))
            .GET()
            .header("Accept", "application/json")
            .build();
        
        HttpResponse<String> response = httpClient.send(
            request, 
            HttpResponse.BodyHandlers.ofString()
        );
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(
                response.body(),
                objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, DeviceDTO.class)
            );
        } else {
            throw new RuntimeException("Failed to fetch devices: " + response.statusCode());
        }
    }
    
    public List<ReadingDTO> getAllReadings() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/readings"))
            .GET()
            .header("Accept", "application/json")
            .build();
        
        HttpResponse<String> response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(
                response.body(),
                objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ReadingDTO.class)
            );
        } else {
            throw new RuntimeException("Failed to fetch readings: " + response.statusCode());
        }
    }
    
    public static void main(String[] args) {
        try {
            SmartGridClient client = new SmartGridClient();
            
            List<DeviceDTO> devices = client.getAllDevices();
            System.out.println("Total devices: " + devices.size());
            
            List<ReadingDTO> readings = client.getAllReadings();
            System.out.println("Total readings: " + readings.size());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## API Testing

### Using cURL

```bash
# Test Devices API
curl -v http://localhost:8080/smartgridmanager/api/devices

# Test Readings API
curl -v http://localhost:8080/smartgridmanager/api/readings

# Save response to file
curl http://localhost:8080/smartgridmanager/api/devices -o devices.json

# Pretty print JSON (with jq)
curl http://localhost:8080/smartgridmanager/api/devices | jq '.'
```

### Using Postman

1. Create new request
2. Set method to GET
3. Enter URL: `http://localhost:8080/smartgridmanager/api/devices`
4. Add header: `Accept: application/json`
5. Click Send

### Using HTTPie

```bash
# Install HTTPie
pip install httpie

# Test endpoints
http GET http://localhost:8080/smartgridmanager/api/devices
http GET http://localhost:8080/smartgridmanager/api/readings
```

## Rate Limiting

Currently, there is no rate limiting implemented. The API can handle as many requests as the server resources allow.

### Future Implementation

Planned rate limiting:
- 1000 requests per hour per IP address
- 10000 requests per day per IP address
- Configurable limits per API key

## CORS Configuration

Cross-Origin Resource Sharing (CORS) is enabled by default, allowing the API to be consumed from web applications hosted on different domains.

## API Versioning

Currently, the API does not implement versioning. Future releases will include version numbers in the URL:

```
/api/v1/devices
/api/v2/devices
```

## Webhooks (Future)

Planned webhook functionality for real-time notifications:
- Alert triggers
- New device registration
- Threshold violations
- System status changes

## WebSocket Support (Future)

Real-time data streaming planned for future releases:
- Live sensor readings
- Real-time alerts
- Dashboard updates

## GraphQL Support (Future)

GraphQL endpoint planned for more flexible data querying.

## Deprecation Policy

When endpoints are deprecated:
- 6 months notice will be provided
- Deprecated endpoints will continue to work during the notice period
- Documentation will be updated with migration guides

## Support

For API support:
- Open an issue on GitHub
- Check the FAQ in the main README
- Review code examples in this document

---

**API Documentation Version**: 1.0  
**Last Updated**: January 2024  
**Status**: Active Development
