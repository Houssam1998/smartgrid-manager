# 🏗️ System Architecture - SmartGrid Manager

Comprehensive architectural documentation for the SmartGrid Manager application.

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [System Architecture](#system-architecture)
- [Application Layers](#application-layers)
- [Technology Stack](#technology-stack)
- [Data Model](#data-model)
- [API Architecture](#api-architecture)
- [Database Design](#database-design)
- [Component Interactions](#component-interactions)
- [Security Architecture](#security-architecture)
- [Performance Considerations](#performance-considerations)
- [Scalability](#scalability)
- [Deployment Architecture](#deployment-architecture)

## Architecture Overview

SmartGrid Manager follows a traditional **3-tier architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                  Presentation Layer                      │
│         (JSP, Bootstrap, Chart.js, JavaScript)          │
└─────────────────────────────────────────────────────────┘
                           ↕
┌─────────────────────────────────────────────────────────┐
│                   Business Layer                         │
│      (Servlets, REST Resources, Business Logic)         │
└─────────────────────────────────────────────────────────┘
                           ↕
┌─────────────────────────────────────────────────────────┐
│                   Data Access Layer                      │
│              (DAOs, JPA, Hibernate)                      │
└─────────────────────────────────────────────────────────┘
                           ↕
┌─────────────────────────────────────────────────────────┐
│                   Persistence Layer                      │
│                   (MySQL Database)                       │
└─────────────────────────────────────────────────────────┘
```

### Design Principles

1. **Separation of Concerns**: Clear boundaries between layers
2. **Single Responsibility**: Each class has one well-defined purpose
3. **DRY (Don't Repeat Yourself)**: Reusable components and utilities
4. **MVC Pattern**: Model-View-Controller for web interface
5. **RESTful Design**: Standard REST principles for APIs
6. **Persistence Layer Abstraction**: JPA for database independence

## System Architecture

### High-Level Architecture Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                        Client Layer                          │
│  ┌────────────┐  ┌────────────┐  ┌────────────────────────┐ │
│  │   Browser  │  │  Mobile    │  │  External Applications │ │
│  │    (UI)    │  │   (Future) │  │     (REST API)         │ │
│  └────────────┘  └────────────┘  └────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
                           │ HTTP/HTTPS
                           ↓
┌──────────────────────────────────────────────────────────────┐
│                    Apache Tomcat 10.x                        │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Web Application (WAR)                     │  │
│  │  ┌──────────────┐           ┌─────────────────────┐   │  │
│  │  │   Servlets   │           │   REST Resources    │   │  │
│  │  │              │           │    (JAX-RS/Jersey)  │   │  │
│  │  │ - HomeServlet│           │ - DeviceResource    │   │  │
│  │  │ - DeviceServ │           │ - ReadingResource   │   │  │
│  │  │ - ReadingServ│           │                     │   │  │
│  │  │ - StatsServlet│          │                     │   │  │
│  │  └──────────────┘           └─────────────────────┘   │  │
│  │         │                              │               │  │
│  │         └──────────┬───────────────────┘               │  │
│  │                    ↓                                    │  │
│  │         ┌────────────────────┐                         │  │
│  │         │   DAO Layer        │                         │  │
│  │         │ - DeviceDao        │                         │  │
│  │         │ - ReadingDao       │                         │  │
│  │         │ - StatsDao         │                         │  │
│  │         └────────────────────┘                         │  │
│  │                    ↓                                    │  │
│  │         ┌────────────────────┐                         │  │
│  │         │   JPA/Hibernate    │                         │  │
│  │         │   EntityManager    │                         │  │
│  │         └────────────────────┘                         │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
                           │ JDBC
                           ↓
┌──────────────────────────────────────────────────────────────┐
│                     MySQL Database 8.0                       │
│  ┌──────────┐  ┌──────────┐  ┌─────────────┐               │
│  │  Device  │  │ Reading  │  │   Indexes   │               │
│  │  Table   │  │  Table   │  │             │               │
│  └──────────┘  └──────────┘  └─────────────┘               │
└──────────────────────────────────────────────────────────────┘
                           │
                           ↓
┌──────────────────────────────────────────────────────────────┐
│                    External Services                         │
│  ┌────────────────────────────────────────────────────────┐  │
│  │          Open-Meteo Weather API                        │  │
│  │          (Real-time weather data)                      │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

## Application Layers

### 1. Presentation Layer

**Technologies**: JSP, JSTL, Bootstrap 5, Chart.js, JavaScript

**Responsibilities**:
- User interface rendering
- Form handling and validation
- Data visualization
- Client-side interactions

**Components**:
- **landing.jsp**: Welcome page
- **dashboard.jsp**: Main dashboard with KPIs and charts
- **devices.jsp**: Device management interface
- **readings.jsp**: Reading management interface
- **stats.jsp**: Statistics and analytics page
- **generator.jsp**: Data generation interface
- **_navbar.jsp**: Reusable navigation component

**Key Features**:
- Responsive design with Bootstrap
- Interactive charts with Chart.js
- Server-side rendering with JSP/JSTL
- Progressive enhancement

### 2. Business Layer

**Technologies**: Jakarta Servlets, JAX-RS (Jersey), Java 11

**Responsibilities**:
- Request handling
- Business logic execution
- Data transformation
- External API integration

**Components**:

#### Servlets (Web MVC)
- **HomeServlet**: Dashboard controller, aggregates KPIs, weather data
- **DeviceServlet**: Device CRUD operations
- **ReadingServlet**: Reading management
- **StatsServlet**: Statistical analysis
- **GeneratorServlet**: Data generation UI controller
- **GenerateDataServlet**: Data generation logic
- **LandingServlet**: Landing page controller

#### REST Resources (API)
- **DeviceResource**: RESTful device endpoints
- **ReadingResource**: RESTful reading endpoints

#### DTOs (Data Transfer Objects)
- **DeviceDTO**: Device data for API responses
- **ReadingDTO**: Reading data for API responses
- **WeatherResponse**: External API response model
- **CurrentWeather**: Current weather data model
- **DailyData**: Forecast data model

**Design Patterns Used**:
- **MVC Pattern**: Separates concerns between Model, View, Controller
- **DTO Pattern**: Prevents circular references, controls API responses
- **Facade Pattern**: Simplifies complex subsystem interactions

### 3. Data Access Layer

**Technologies**: JPA (Jakarta Persistence API), Hibernate 6.4.1

**Responsibilities**:
- Database operations (CRUD)
- Query execution
- Transaction management
- Entity lifecycle management

**Components**:

#### DAOs (Data Access Objects)
- **DeviceDao**: Device persistence operations
- **ReadingDao**: Reading persistence operations
- **StatsDao**: Complex statistical queries
- **JpaUtil**: EntityManager factory and utility

**Key Features**:
- **JPQL Queries**: Type-safe, object-oriented queries
- **JOIN FETCH**: Prevents N+1 query problem
- **Cache Control**: Explicit cache bypass for fresh data
- **Transaction Management**: Proper commit/rollback handling
- **Batch Processing**: Efficient bulk operations
- **Connection Pooling**: HikariCP for performance

### 4. Persistence Layer

**Technologies**: MySQL 8.0, JDBC

**Responsibilities**:
- Data storage and retrieval
- Data integrity
- Query optimization
- Backup and recovery

## Technology Stack

### Backend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11 | Core language |
| Jakarta EE | 10 | Enterprise platform |
| Hibernate | 6.4.1 | ORM framework |
| MySQL | 8.0 | Database |
| HikariCP | 5.1.0 | Connection pooling |
| Jersey | 3.1.5 | REST framework |
| Jackson | 2.16.1 | JSON processing |
| Maven | 3.x | Build tool |
| Tomcat | 10.x | Servlet container |

### Frontend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| JSP/JSTL | 3.0 | Server-side rendering |
| Bootstrap | 5.3.3 | UI framework |
| Chart.js | Latest | Data visualization |
| Font Awesome | 6.4.0 | Icons |
| JavaScript | ES6+ | Client-side logic |

### Development Tools

| Tool | Purpose |
|------|---------|
| IntelliJ IDEA / Eclipse | IDE |
| Git | Version control |
| Maven | Build automation |
| MySQL Workbench | Database management |
| Postman | API testing |

## Data Model

### Entity Relationship Diagram

```
┌─────────────────────────────────────┐
│           Device                     │
│─────────────────────────────────────│
│ PK: id (BIGINT, AUTO_INCREMENT)     │
│     name (VARCHAR)                  │
│     deviceType (VARCHAR)            │
│     location (VARCHAR)              │
└─────────────────────────────────────┘
                │
                │ 1
                │
                │ owns
                │
                │ *
                ↓
┌─────────────────────────────────────┐
│           Reading                    │
│─────────────────────────────────────│
│ PK: id (BIGINT, AUTO_INCREMENT)     │
│     timestamp (DATETIME)            │
│     value (DOUBLE)                  │
│     readingType (VARCHAR)           │
│ FK: device_id (BIGINT)              │
└─────────────────────────────────────┘
```

### Entity Details

#### Device Entity

```java
@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;          // e.g., "SmartMeter_001"
    private String deviceType;    // e.g., "SmartMeter"
    private String location;      // e.g., "Living Room"
    
    @OneToMany(mappedBy = "device", 
               cascade = CascadeType.ALL, 
               fetch = FetchType.LAZY)
    private List<Reading> readings;
}
```

**Relationships**:
- One-to-Many with Reading (one device has many readings)
- Cascade ALL: Deleting a device deletes its readings
- Lazy fetch: Readings loaded on demand

#### Reading Entity

```java
@Entity
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime timestamp;
    private double value;
    private String readingType;
    
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
}
```

**Relationships**:
- Many-to-One with Device (many readings belong to one device)
- Eager fetch by default (device loaded with reading)

### Reading Types and Units

| Reading Type | Unit | Description | Typical Range |
|--------------|------|-------------|---------------|
| power | W (Watts) | Power consumption | 0-10,000 |
| voltage | V (Volts) | Electrical voltage | 220-230 |
| current | A (Amperes) | Electrical current | 0-50 |
| temperature | °C (Celsius) | Temperature | 15-35 |
| humidity | % | Relative humidity | 30-70 |
| luminosity | Lux | Light intensity | 0-1000 |
| motion | 0/1 | Motion detected | 0 or 1 |
| co2 | PPM | CO2 concentration | 400-1000 |

## API Architecture

### REST API Design

**Base Path**: `/api/*`

**Resources**:
1. `/api/devices` - Device management
2. `/api/readings` - Reading management

**Architecture Pattern**: Resource-oriented architecture (ROA)

**Key Components**:

```
DeviceResource.java
├── @Path("/devices")
├── @GET → getAllDevices()
└── Uses: DeviceDao, DeviceDTO

ReadingResource.java
├── @Path("/readings")
├── @GET → getAllReadings()
└── Uses: ReadingDao, ReadingDTO
```

**DTO Pattern**:
- Prevents circular references (Device ↔ Reading)
- Controls API response structure
- Separates internal model from API contract

**JSON Serialization**:
- Jackson with JSR310 module for date/time
- Automatic conversion of entities to JSON
- Configurable serialization rules

## Database Design

### Schema

```sql
-- Device Table
CREATE TABLE Device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    deviceType VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    INDEX idx_deviceType (deviceType),
    INDEX idx_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reading Table
CREATE TABLE Reading (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    timestamp DATETIME NOT NULL,
    value DOUBLE NOT NULL,
    readingType VARCHAR(50) NOT NULL,
    device_id BIGINT NOT NULL,
    FOREIGN KEY (device_id) REFERENCES Device(id) ON DELETE CASCADE,
    INDEX idx_timestamp (timestamp),
    INDEX idx_readingType (readingType),
    INDEX idx_device_timestamp (device_id, timestamp),
    INDEX idx_type_value (readingType, value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Indexing Strategy

**Primary Indexes**:
- `Device.id` (PRIMARY KEY)
- `Reading.id` (PRIMARY KEY)

**Foreign Key Indexes**:
- `Reading.device_id` (for JOIN operations)

**Query Optimization Indexes**:
- `Device.deviceType` - Filter by device type
- `Device.location` - Filter by location
- `Reading.timestamp` - Time-based queries
- `Reading.readingType` - Filter by reading type
- `Reading(device_id, timestamp)` - Device readings over time
- `Reading(readingType, value)` - Alert queries

### Database Optimization

**Connection Pooling** (HikariCP):
```xml
<property name="hibernate.hikari.maximumPoolSize" value="10"/>
```

**Batch Processing**:
```xml
<property name="hibernate.jdbc.batch_size" value="50"/>
<property name="hibernate.order_inserts" value="true"/>
<property name="hibernate.order_updates" value="true"/>
```

**Query Caching**:
- Explicit cache bypass for fresh data
- `BYPASS` mode for statistical queries

## Component Interactions

### Request Flow (Web Interface)

```
User Browser
    ↓ (HTTP GET /home)
HomeServlet
    ↓ (getData)
StatsDao, DeviceDao, ReadingDao
    ↓ (JPQL Queries)
JPA/Hibernate
    ↓ (SQL)
MySQL Database
    ↓ (Result Set)
JPA/Hibernate (Entity mapping)
    ↓ (Java Objects)
HomeServlet (set attributes)
    ↓ (forward)
dashboard.jsp (render)
    ↓ (HTML)
User Browser
```

### Request Flow (REST API)

```
Client Application
    ↓ (HTTP GET /api/devices)
DeviceResource
    ↓ (findAll)
DeviceDao
    ↓ (JPQL Query)
JPA/Hibernate
    ↓ (SQL)
MySQL Database
    ↓ (Result Set)
JPA/Hibernate (Device entities)
    ↓ (Stream mapping)
DeviceDTO List
    ↓ (Jackson serialization)
JSON Response
    ↓ (HTTP)
Client Application
```

### Data Generation Flow

```
User (Web Form)
    ↓ (POST /generate)
GenerateDataServlet
    ↓ (parseParameters)
DataGenerator Logic
    ↓ (begin transaction)
EntityManager
    ↓ (persist entities)
MySQL Database
    ↓ (batch insert)
Transaction Commit
    ↓ (success/failure)
GenerateDataServlet (redirect)
    ↓ (GET /generator)
generator.jsp (show message)
    ↓ (HTML)
User Browser
```

## Security Architecture

### Current Security Measures

1. **SQL Injection Prevention**: 
   - JPQL parameterized queries
   - No string concatenation in queries

2. **Connection Security**:
   - HikariCP secure connection pooling
   - Credential management in persistence.xml

3. **Input Validation**:
   - Server-side validation in servlets
   - Range checks for numeric inputs

### Security Considerations (Future)

1. **Authentication**: JWT or session-based
2. **Authorization**: Role-based access control
3. **HTTPS**: SSL/TLS encryption
4. **CSRF Protection**: Token-based
5. **XSS Prevention**: Output encoding
6. **Rate Limiting**: API throttling
7. **API Keys**: For external access
8. **Audit Logging**: Track user actions

## Performance Considerations

### Database Performance

1. **Connection Pooling**: HikariCP with 10 connections
2. **Batch Processing**: 50 operations per batch
3. **Lazy Loading**: Fetch related entities on demand
4. **Query Optimization**: Proper indexing strategy
5. **JOIN FETCH**: Prevent N+1 query problem

### Application Performance

1. **Stateless Servlets**: No session state
2. **DTO Pattern**: Minimize data transfer
3. **Caching**: Second-level cache support
4. **Efficient Queries**: JPQL with projections

### Frontend Performance

1. **CDN Resources**: Bootstrap, Chart.js from CDN
2. **Lazy Loading**: Charts load on demand
3. **Pagination**: Limit result sets
4. **Compression**: Gzip enabled in Tomcat

## Scalability

### Vertical Scaling

- Increase JVM heap size
- Add more database connections
- Upgrade server hardware

### Horizontal Scaling (Future)

- Load balancer with multiple Tomcat instances
- Database replication (master-slave)
- Session clustering
- Distributed caching (Redis)

### Data Scaling

- **Partitioning**: Partition Reading table by date
- **Archiving**: Move old data to archive tables
- **Sharding**: Distribute data across databases

## Deployment Architecture

### Development Environment

```
Developer Machine
├── IntelliJ IDEA / Eclipse
├── Tomcat 10.x (local)
├── MySQL 8.0 (local)
└── Maven (build)
```

### Production Environment (Recommended)

```
┌─────────────────────────────────────┐
│      Load Balancer (nginx)          │
└─────────────────────────────────────┘
           │              │
           ↓              ↓
┌──────────────┐  ┌──────────────┐
│  Tomcat 1    │  │  Tomcat 2    │
│  (App Server)│  │  (App Server)│
└──────────────┘  └──────────────┘
           │              │
           └──────┬───────┘
                  ↓
        ┌──────────────────┐
        │  MySQL Primary   │
        └──────────────────┘
                  │
                  ↓
        ┌──────────────────┐
        │  MySQL Replica   │
        └──────────────────┘
```

### Deployment Steps

1. Build WAR: `mvn clean package`
2. Deploy to Tomcat webapps
3. Configure database connection
4. Start Tomcat
5. Verify deployment
6. Run smoke tests

## Monitoring and Maintenance

### Logging

- **Application Logs**: Java Util Logging
- **Tomcat Logs**: catalina.out
- **Access Logs**: HTTP request logs
- **Database Logs**: MySQL slow query log

### Monitoring Points

- Server uptime
- Response times
- Database connections
- Memory usage
- Disk space
- Active sessions

### Maintenance Tasks

- Database backups (daily)
- Log rotation
- Performance tuning
- Security updates
- Data archiving

## Future Enhancements

### Architecture Improvements

1. **Microservices**: Split into smaller services
2. **Message Queue**: Async processing with RabbitMQ
3. **Caching Layer**: Redis for performance
4. **API Gateway**: Centralized API management
5. **Service Discovery**: Eureka or Consul
6. **Container Orchestration**: Kubernetes

### Technology Upgrades

1. **Spring Boot**: Modern framework
2. **Docker**: Containerization
3. **GraphQL**: Flexible API queries
4. **WebSocket**: Real-time updates
5. **Elasticsearch**: Advanced search
6. **Kafka**: Event streaming

---

**Architecture Version**: 1.0  
**Last Updated**: January 2024  
**Maintained By**: Development Team
