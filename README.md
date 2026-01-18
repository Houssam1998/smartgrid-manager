# 🏠 SmartGrid Manager

A comprehensive web-based Smart Home and Energy Management System built with Java EE, designed to monitor, analyze, and manage smart home devices and their energy consumption in real-time.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Building and Deployment](#building-and-deployment)
- [Usage Guide](#usage-guide)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

SmartGrid Manager is an enterprise-grade smart home energy management platform that provides:

- **Real-time Monitoring**: Track energy consumption and sensor readings from various smart home devices
- **Alert System**: Automated detection of anomalies and threshold violations
- **Data Visualization**: Interactive charts and dashboards for energy consumption analysis
- **Weather Integration**: Real-time weather data integration from Open-Meteo API for Fès, Morocco
- **RESTful API**: Complete REST API for device and reading management
- **Data Generation**: Comprehensive test data generation for development and testing

## ✨ Features

### Core Functionality

- **Multi-Device Support**: Manage various smart home devices including:
  - Smart Meters (power, voltage, current monitoring)
  - Thermostats (temperature, humidity control)
  - AC Units and HVAC Systems
  - Light Sensors
  - Motion Sensors
  - Security Cameras
  - Solar Panels
  - Smoke Detectors
  - Water Heaters
  - Appliances (Refrigerator, Washing Machine)

- **Real-time Dashboard**:
  - Total devices and readings count
  - Active alerts monitoring
  - Real-time weather information
  - Average power consumption by device
  - 7-day weather forecast
  - Security intrusion alerts
  - Recent system alerts
  - Latest sensor readings

- **Advanced Analytics**:
  - Power consumption trends
  - Temperature monitoring
  - Alert history and analysis
  - Device performance metrics

- **Smart Alert System**:
  - Power alerts (>5000W)
  - Temperature alerts (>35°C)
  - Voltage alerts (>250V)
  - Current alerts (>40A)
  - Humidity alerts (<10% or >90%)
  - CO2 alerts (>1000ppm)
  - Motion detection alerts

### RESTful API

- Device management endpoints
- Reading management endpoints
- JSON-based data exchange
- Jackson serialization with JSR310 support

## 🛠️ Technology Stack

### Backend
- **Java 11**: Core programming language
- **Jakarta EE 10**: Enterprise Java specifications
  - Jakarta Servlet API 6.1.0
  - Jakarta Persistence API (JPA) 3.0
  - Jakarta RESTful Web Services (JAX-RS)
- **Hibernate 6.4.1**: ORM framework
- **MySQL 8.0**: Relational database
- **HikariCP 5.1.0**: High-performance JDBC connection pooling
- **Jersey 3.1.5**: JAX-RS implementation
- **Jackson**: JSON processing

### Frontend
- **JSP & JSTL**: Server-side rendering
- **Bootstrap 5.3.3**: UI framework
- **Chart.js**: Data visualization
- **Font Awesome 6.4.0**: Icons

### Build & Deployment
- **Maven 3.x**: Project management and build tool
- **Apache Tomcat 10.x**: Servlet container (Jakarta EE 10 compatible)

### External APIs
- **Open-Meteo API**: Weather data integration

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 11 or higher**
  ```bash
  java -version
  ```

- **Apache Maven 3.6 or higher**
  ```bash
  mvn -version
  ```

- **MySQL Server 8.0 or higher**
  ```bash
  mysql --version
  ```

- **Apache Tomcat 10.x** (Jakarta EE 10 compatible)
  - Download from: https://tomcat.apache.org/download-10.cgi
  - Note: Tomcat 9.x will not work as it uses javax.* packages instead of jakarta.*

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Houssam1998/smartgrid-manager.git
cd smartgrid-manager
```

### 2. Database Setup

#### Create Database and User

```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE smartgrid CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'smartgrid_user'@'localhost' IDENTIFIED BY 'smartgrid_pwd';

-- Grant privileges
GRANT ALL PRIVILEGES ON smartgrid.* TO 'smartgrid_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Configure Database Connection

The database configuration is located in `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/smartgrid?serverTimezone=UTC"/>
<property name="jakarta.persistence.jdbc.user" value="smartgrid_user"/>
<property name="jakarta.persistence.jdbc.password" value="smartgrid_pwd"/>
```

**Note**: Hibernate is configured with `hibernate.hbm2ddl.auto=update`, so tables will be created automatically on first run.

### 3. Build the Project

```bash
# Clean and package the application
mvn clean package

# The WAR file will be created at: target/smartgridmanager.war
```

### 4. Deploy to Tomcat

#### Option A: Manual Deployment
1. Copy the WAR file to Tomcat's webapps directory:
   ```bash
   cp target/smartgridmanager.war $TOMCAT_HOME/webapps/
   ```

2. Start Tomcat:
   ```bash
   $TOMCAT_HOME/bin/startup.sh  # Linux/Mac
   $TOMCAT_HOME/bin/startup.bat # Windows
   ```

#### Option B: IDE Deployment (IntelliJ IDEA / Eclipse)
1. Configure Tomcat server in your IDE
2. Deploy the project as an exploded WAR
3. Start the server from the IDE

### 5. Verify Installation

Access the application at:
```
http://localhost:8080/smartgridmanager/
```

You should see the landing page with the "SmartGrid Manager" title.

## 🗄️ Database Setup

### Automatic Schema Creation

On first run, Hibernate will automatically create the following tables:
- `Device`: Stores smart home device information
- `Reading`: Stores sensor readings and measurements

### Generate Test Data

The application includes a powerful data generation tool accessible via:
```
http://localhost:8080/smartgridmanager/generator
```

#### Using the Web Interface:
1. Navigate to the "Data Generator" page
2. Configure parameters:
   - **Device Count**: Number of devices to create (1-100)
   - **Readings per Device**: Number of readings per device (10-5000)
   - **Device Type**: Optional - specify device type or leave empty for random
   - **Location**: Optional - specify location or leave empty for random
   - **Reading Type**: Optional - specify reading type or leave empty for random
   - **Alert Probability**: Percentage of readings that should trigger alerts (0-100%)
3. Click "Generate Data"

#### Programmatic Data Generation:

You can also generate data using the `DataGenerator` utility class:

```java
// Example 1: Random generation
DataGenerator.generateRandomData(20, 500); // 20 devices, 500 readings each

// Example 2: Specific device type and location
DataGenerator.generateData(5, 1000, "SmartMeter", "Kitchen", null, 0.08);

// Example 3: Thermostats with temperature only
DataGenerator.generateData(10, 800, "Thermostat", null, "temperature", 0.03);
```

## 🎮 Usage Guide

### Main Dashboard

Access: `http://localhost:8080/smartgridmanager/home`

The dashboard displays:
- **KPI Cards**: Total devices, readings, active alerts, current weather
- **Power Consumption Chart**: Top 10 devices by average power consumption
- **Weather Forecast**: 7-day forecast for Fès, Morocco
- **Security Alerts**: Recent motion detection from security cameras
- **System Alerts**: Recent threshold violations
- **Latest Readings**: Most recent sensor measurements

### Device Management

Access: `http://localhost:8080/smartgridmanager/devices`

Features:
- View all registered devices
- Filter by device type and location
- Add new devices
- View device details
- Delete devices

### Readings Management

Access: `http://localhost:8080/smartgridmanager/readings`

Features:
- View all sensor readings
- Filter by device, reading type, and time range
- Add manual readings
- Export reading data

### Statistics & Analytics

Access: `http://localhost:8080/smartgridmanager/stats`

Features:
- Statistical analysis by reading type
- Average, minimum, and maximum values
- Historical trends
- Alert frequency analysis

## 🔌 API Documentation

The application exposes RESTful APIs at the base path `/api/*`.

### Device API

#### Get All Devices
```http
GET /api/devices
Content-Type: application/json
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "SmartMeter_001",
    "deviceType": "SmartMeter",
    "location": "Living Room"
  }
]
```

### Reading API

#### Get All Readings
```http
GET /api/readings
Content-Type: application/json
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
  }
]
```

### Weather Integration

The application automatically fetches weather data from Open-Meteo API:
- **Location**: Fès, Morocco (34.04°N, 5.00°W)
- **Update Frequency**: Every dashboard refresh
- **Data Included**: Current temperature and 7-day forecast

## 📁 Project Structure

```
smartgrid-manager/
├── src/
│   └── main/
│       ├── java/org/smartgrid/smartgridmanager/
│       │   ├── api/                      # REST API Resources
│       │   │   ├── DeviceResource.java   # Device API endpoint
│       │   │   ├── DeviceDTO.java        # Device data transfer object
│       │   │   ├── ReadingResource.java  # Reading API endpoint
│       │   │   └── ReadingDTO.java       # Reading data transfer object
│       │   ├── dao/                      # Data Access Objects
│       │   │   ├── DeviceDao.java        # Device database operations
│       │   │   ├── ReadingDao.java       # Reading database operations
│       │   │   ├── StatsDao.java         # Statistics queries
│       │   │   └── JpaUtil.java          # JPA EntityManager utility
│       │   ├── model/                    # JPA Entities
│       │   │   ├── Device.java           # Device entity
│       │   │   ├── Reading.java          # Reading entity
│       │   │   └── api_external/         # External API models
│       │   │       ├── WeatherResponse.java
│       │   │       ├── CurrentWeather.java
│       │   │       └── DailyData.java
│       │   ├── web/                      # Servlets
│       │   │   ├── HomeServlet.java      # Dashboard controller
│       │   │   ├── DeviceServlet.java    # Device management
│       │   │   ├── ReadingServlet.java   # Reading management
│       │   │   ├── StatsServlet.java     # Statistics controller
│       │   │   ├── GeneratorServlet.java # Data generator UI
│       │   │   ├── GenerateDataServlet.java # Data generation logic
│       │   │   └── LandingServlet.java   # Landing page
│       │   └── util/                     # Utilities
│       │       └── DataGenerator.java    # Test data generation
│       ├── resources/
│       │   └── META-INF/
│       │       └── persistence.xml       # JPA configuration
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml               # Web application configuration
│           ├── pages/                    # JSP pages
│           │   ├── devices.jsp           # Device management UI
│           │   ├── readings.jsp          # Reading management UI
│           │   ├── stats.jsp             # Statistics UI
│           │   ├── generator.jsp         # Data generator UI
│           │   └── _navbar.jsp           # Navigation component
│           ├── dashboard.jsp             # Main dashboard
│           └── landing.jsp               # Landing page
├── pom.xml                               # Maven configuration
├── .gitignore                            # Git ignore rules
└── README.md                             # This file
```

## ⚙️ Configuration

### Persistence Configuration

Edit `src/main/resources/META-INF/persistence.xml` to configure:

- **Database connection**: URL, username, password
- **Hibernate settings**: Dialect, DDL auto mode, SQL logging
- **Connection pooling**: HikariCP settings
- **Batch processing**: Batch size and ordering

### Web Application Configuration

Edit `src/main/webapp/WEB-INF/web.xml` to configure:

- **Jersey REST API**: Package scanning for REST resources
- **Servlet mappings**: URL patterns for servlets
- **Welcome files**: Default landing pages

### Alert Thresholds

Alert thresholds are defined in `GenerateDataServlet` and `StatsDao`:

```java
static final double POWER_ALERT = 5000.0;      // > 5000W
static final double TEMP_ALERT = 35.0;         // > 35°C
static final double VOLTAGE_ALERT = 250.0;     // > 250V
static final double CURRENT_ALERT = 40.0;      // > 40A
static final double HUMIDITY_LOW = 10.0;       // < 10%
static final double HUMIDITY_HIGH = 90.0;      // > 90%
static final double CO2_ALERT = 1000.0;        // > 1000ppm
```

## 🔧 Development

### Building for Development

```bash
# Compile without running tests
mvn clean compile

# Package with custom finalName
mvn clean package

# Install to local Maven repository
mvn clean install
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=YourTestClass
```

### IDE Setup

#### IntelliJ IDEA
1. Import project as Maven project
2. Configure Tomcat 10.x server
3. Set language level to Java 11
4. Deploy as exploded WAR for hot reload

#### Eclipse
1. Import as Maven project
2. Configure Server Runtime (Tomcat 10.x)
3. Add project to server
4. Configure build path to Java 11

## 🐛 Troubleshooting

### Common Issues

1. **ClassNotFoundException: jakarta.servlet.**
   - Solution: Ensure you're using Tomcat 10.x or higher
   - Tomcat 9.x uses javax.* packages instead of jakarta.*

2. **Database Connection Errors**
   - Verify MySQL is running: `sudo systemctl status mysql`
   - Check credentials in `persistence.xml`
   - Ensure database exists: `SHOW DATABASES;`

3. **Port Already in Use**
   - Change Tomcat port in `$TOMCAT_HOME/conf/server.xml`
   - Default HTTP port is 8080

4. **JAR Not Found Errors**
   - Run: `mvn clean install` to download all dependencies
   - Check Maven repository: `~/.m2/repository`

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Submit a pull request

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and concise

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **Houssam** - Initial work - [Houssam1998](https://github.com/Houssam1998)

## 🙏 Acknowledgments

- Open-Meteo API for weather data
- Bootstrap team for the UI framework
- Chart.js for data visualization
- Hibernate ORM team
- Jakarta EE community

## 📞 Support

For support and questions:
- Open an issue on GitHub
- Check existing documentation
- Review the troubleshooting section

## 🗺️ Roadmap

Future enhancements planned:
- [ ] User authentication and authorization
- [ ] Multi-tenant support
- [ ] Advanced analytics and machine learning predictions
- [ ] Mobile application
- [ ] Email/SMS alert notifications
- [ ] Data export in multiple formats (CSV, Excel, PDF)
- [ ] Integration with more smart home platforms
- [ ] Scheduled reports generation
- [ ] Energy cost calculation and billing
- [ ] Carbon footprint tracking

---

**Built with ❤️ for Smart Home Energy Management**
