# 🔧 Detailed Setup Guide - SmartGrid Manager

This guide provides comprehensive step-by-step instructions for setting up the SmartGrid Manager application from scratch.

## Table of Contents

- [System Requirements](#system-requirements)
- [Step 1: Java Installation](#step-1-java-installation)
- [Step 2: Maven Installation](#step-2-maven-installation)
- [Step 3: MySQL Installation and Configuration](#step-3-mysql-installation-and-configuration)
- [Step 4: Tomcat Installation](#step-4-tomcat-installation)
- [Step 5: Project Setup](#step-5-project-setup)
- [Step 6: Initial Data Population](#step-6-initial-data-population)
- [Verification Steps](#verification-steps)
- [Environment-Specific Configuration](#environment-specific-configuration)

## System Requirements

### Minimum Requirements
- **CPU**: 2 cores, 2.0 GHz
- **RAM**: 4 GB
- **Disk Space**: 2 GB free space
- **Operating System**: 
  - Linux (Ubuntu 20.04+, CentOS 7+, Debian 10+)
  - Windows 10/11
  - macOS 10.14+

### Recommended Requirements
- **CPU**: 4 cores, 2.5 GHz or higher
- **RAM**: 8 GB or more
- **Disk Space**: 5 GB free space
- **Operating System**: Latest LTS versions

## Step 1: Java Installation

### Linux (Ubuntu/Debian)

```bash
# Update package index
sudo apt update

# Install OpenJDK 11
sudo apt install openjdk-11-jdk -y

# Verify installation
java -version
javac -version

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### Linux (CentOS/RHEL)

```bash
# Install OpenJDK 11
sudo yum install java-11-openjdk-devel -y

# Verify installation
java -version

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### macOS

```bash
# Using Homebrew
brew install openjdk@11

# Add to PATH
echo 'export PATH="/usr/local/opt/openjdk@11/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify installation
java -version
```

### Windows

1. Download OpenJDK 11 from [Adoptium](https://adoptium.net/)
2. Run the installer
3. Set environment variables:
   - Open System Properties → Environment Variables
   - Add `JAVA_HOME`: `C:\Program Files\Eclipse Adoptium\jdk-11.x.x`
   - Add to `Path`: `%JAVA_HOME%\bin`
4. Verify in Command Prompt:
   ```cmd
   java -version
   ```

## Step 2: Maven Installation

### Linux (Ubuntu/Debian)

```bash
# Install Maven
sudo apt install maven -y

# Verify installation
mvn -version

# Alternative: Manual installation for latest version
wget https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz
sudo tar -xvf apache-maven-3.9.5-bin.tar.gz -C /opt
sudo ln -s /opt/apache-maven-3.9.5 /opt/maven

# Set Maven environment variables
echo 'export M2_HOME=/opt/maven' >> ~/.bashrc
echo 'export PATH=$M2_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### macOS

```bash
# Using Homebrew
brew install maven

# Verify installation
mvn -version
```

### Windows

1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Set environment variables:
   - Add `M2_HOME`: `C:\Program Files\Apache\maven`
   - Add to `Path`: `%M2_HOME%\bin`
4. Verify in Command Prompt:
   ```cmd
   mvn -version
   ```

## Step 3: MySQL Installation and Configuration

### Linux (Ubuntu/Debian)

```bash
# Update package index
sudo apt update

# Install MySQL Server
sudo apt install mysql-server -y

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure MySQL installation
sudo mysql_secure_installation

# Log in to MySQL
sudo mysql -u root -p
```

### macOS

```bash
# Using Homebrew
brew install mysql

# Start MySQL service
brew services start mysql

# Secure installation
mysql_secure_installation

# Log in to MySQL
mysql -u root -p
```

### Windows

1. Download MySQL Installer from [MySQL Downloads](https://dev.mysql.com/downloads/installer/)
2. Run the installer and select "Developer Default"
3. Configure MySQL Server:
   - Port: 3306 (default)
   - Set root password
4. Complete installation
5. Open MySQL Command Line Client

### Database Setup (All Platforms)

Once MySQL is running, execute the following SQL commands:

```sql
-- Create the database
CREATE DATABASE smartgrid CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create application user
CREATE USER 'smartgrid_user'@'localhost' IDENTIFIED BY 'smartgrid_pwd';

-- Grant privileges
GRANT ALL PRIVILEGES ON smartgrid.* TO 'smartgrid_user'@'localhost';
FLUSH PRIVILEGES;

-- Verify database creation
SHOW DATABASES;

-- Use the database
USE smartgrid;

-- Verify user privileges
SHOW GRANTS FOR 'smartgrid_user'@'localhost';

-- Exit MySQL
EXIT;
```

### Test Database Connection

```bash
# Test connection with new user
mysql -u smartgrid_user -p smartgrid

# If successful, you should see the MySQL prompt
# Type 'EXIT;' to leave
```

### Configure MySQL for Production (Optional)

Edit MySQL configuration file:

**Linux**: `/etc/mysql/mysql.conf.d/mysqld.cnf`
**macOS**: `/usr/local/etc/my.cnf`
**Windows**: `C:\ProgramData\MySQL\MySQL Server 8.0\my.ini`

Recommended settings:

```ini
[mysqld]
# Character set
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci

# Performance tuning
max_connections=200
innodb_buffer_pool_size=1G
innodb_log_file_size=256M
innodb_flush_log_at_trx_commit=2

# Query cache (if MySQL 5.7)
# query_cache_type=1
# query_cache_size=128M

# Logging
log_error=/var/log/mysql/error.log
slow_query_log=1
slow_query_log_file=/var/log/mysql/slow.log
long_query_time=2
```

Restart MySQL after configuration changes:

```bash
# Linux
sudo systemctl restart mysql

# macOS
brew services restart mysql

# Windows (Command Prompt as Administrator)
net stop MySQL80
net start MySQL80
```

## Step 4: Tomcat Installation

### Linux (Ubuntu/Debian)

```bash
# Download Tomcat 10.1
cd /tmp
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.18/bin/apache-tomcat-10.1.18.tar.gz

# Extract to /opt
sudo tar -xvf apache-tomcat-10.1.18.tar.gz -C /opt
sudo ln -s /opt/apache-tomcat-10.1.18 /opt/tomcat

# Set ownership
sudo useradd -r tomcat
sudo chown -R tomcat:tomcat /opt/tomcat
sudo chmod +x /opt/tomcat/bin/*.sh

# Set CATALINA_HOME
echo 'export CATALINA_HOME=/opt/tomcat' >> ~/.bashrc
source ~/.bashrc
```

### macOS

```bash
# Download Tomcat 10.1
cd ~/Downloads
curl -O https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.18/bin/apache-tomcat-10.1.18.tar.gz

# Extract
tar -xvf apache-tomcat-10.1.18.tar.gz
sudo mv apache-tomcat-10.1.18 /usr/local/tomcat

# Set permissions
chmod +x /usr/local/tomcat/bin/*.sh

# Set CATALINA_HOME
echo 'export CATALINA_HOME=/usr/local/tomcat' >> ~/.zshrc
source ~/.zshrc
```

### Windows

1. Download Tomcat 10.1 from [Apache Tomcat](https://tomcat.apache.org/download-10.cgi)
2. Download the "32-bit/64-bit Windows Service Installer"
3. Run the installer
4. Install to: `C:\Program Files\Apache Software Foundation\Tomcat 10.1`
5. Set environment variable:
   - `CATALINA_HOME`: `C:\Program Files\Apache Software Foundation\Tomcat 10.1`

### Configure Tomcat

#### 1. Set Memory Options

**Linux/macOS** - Create/edit `$CATALINA_HOME/bin/setenv.sh`:

```bash
#!/bin/bash
export CATALINA_OPTS="$CATALINA_OPTS -Xms512m -Xmx2048m"
export CATALINA_OPTS="$CATALINA_OPTS -XX:MaxPermSize=512m"
export JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=true"
```

Make it executable:
```bash
chmod +x $CATALINA_HOME/bin/setenv.sh
```

**Windows** - Create/edit `%CATALINA_HOME%\bin\setenv.bat`:

```batch
set CATALINA_OPTS=%CATALINA_OPTS% -Xms512m -Xmx2048m
set CATALINA_OPTS=%CATALINA_OPTS% -XX:MaxPermSize=512m
```

#### 2. Configure Admin User

Edit `$CATALINA_HOME/conf/tomcat-users.xml`:

```xml
<tomcat-users>
  <role rolename="manager-gui"/>
  <role rolename="admin-gui"/>
  <user username="admin" password="admin123" roles="manager-gui,admin-gui"/>
</tomcat-users>
```

#### 3. Enable Remote Manager (Optional)

Edit `$CATALINA_HOME/webapps/manager/META-INF/context.xml`:

Comment out the Valve that restricts access:

```xml
<!--
<Valve className="org.apache.catalina.valves.RemoteAddrValve"
       allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" />
-->
```

### Start Tomcat

**Linux/macOS:**
```bash
$CATALINA_HOME/bin/startup.sh

# Verify logs
tail -f $CATALINA_HOME/logs/catalina.out
```

**Windows:**
```cmd
%CATALINA_HOME%\bin\startup.bat
```

**Or use Windows Service:**
```cmd
net start Tomcat10
```

### Verify Tomcat Installation

Open a browser and navigate to:
```
http://localhost:8080
```

You should see the Tomcat welcome page.

## Step 5: Project Setup

### Clone the Repository

```bash
# Clone the project
git clone https://github.com/Houssam1998/smartgrid-manager.git
cd smartgrid-manager
```

### Configure Database Connection

Edit `src/main/resources/META-INF/persistence.xml` if you changed the default credentials:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/smartgrid?serverTimezone=UTC"/>
<property name="jakarta.persistence.jdbc.user" value="smartgrid_user"/>
<property name="jakarta.persistence.jdbc.password" value="smartgrid_pwd"/>
```

### Build the Project

```bash
# Clean and build
mvn clean package

# The WAR file will be created at:
# target/smartgridmanager.war
```

### Deploy to Tomcat

#### Option 1: Copy WAR File

```bash
# Linux/macOS
cp target/smartgridmanager.war $CATALINA_HOME/webapps/

# Windows
copy target\smartgridmanager.war "%CATALINA_HOME%\webapps\"
```

Tomcat will automatically deploy the application.

#### Option 2: Use Tomcat Manager

1. Open http://localhost:8080/manager/html
2. Login with the admin credentials configured earlier
3. Scroll to "WAR file to deploy"
4. Choose `target/smartgridmanager.war`
5. Click "Deploy"

#### Option 3: IDE Deployment

**IntelliJ IDEA:**
1. Go to Run → Edit Configurations
2. Add new Tomcat Server configuration
3. Configure Tomcat home directory
4. Add deployment artifact (WAR exploded)
5. Click Run

**Eclipse:**
1. Go to Servers view
2. Right-click → New → Server
3. Select Apache Tomcat v10.1
4. Add the project
5. Start the server

### Verify Deployment

Check Tomcat logs:

```bash
tail -f $CATALINA_HOME/logs/catalina.out
```

Look for messages like:
```
INFO: Deployment of web application directory [smartgridmanager] has finished
```

### Access the Application

Open browser and navigate to:
```
http://localhost:8080/smartgridmanager/
```

You should see the landing page.

## Step 6: Initial Data Population

### Using the Web Interface

1. Access the dashboard:
   ```
   http://localhost:8080/smartgridmanager/home
   ```

2. Navigate to "Data Generator":
   ```
   http://localhost:8080/smartgridmanager/generator
   ```

3. Configure data generation:
   - **Devices**: 20
   - **Readings per Device**: 1000
   - **Alert Probability**: 5%
   - Leave other fields empty for random data

4. Click "Generate Data"

5. Wait for completion message

6. Return to dashboard to see the populated data

### Using the DataGenerator Class

Alternatively, you can run the data generator directly:

```bash
# Navigate to project directory
cd smartgrid-manager

# Compile the project if not already done
mvn compile

# Run the DataGenerator main method
mvn exec:java -Dexec.mainClass="org.smartgrid.smartgridmanager.util.DataGenerator"
```

Edit the `main()` method in `DataGenerator.java` to customize data generation.

## Verification Steps

### 1. Check Database Tables

```sql
-- Connect to MySQL
mysql -u smartgrid_user -p smartgrid

-- List tables (should show Device and Reading tables)
SHOW TABLES;

-- Check device count
SELECT COUNT(*) FROM Device;

-- Check reading count
SELECT COUNT(*) FROM Reading;

-- View sample devices
SELECT * FROM Device LIMIT 5;

-- View sample readings
SELECT * FROM Reading LIMIT 5;
```

### 2. Test API Endpoints

```bash
# Test Device API
curl -X GET http://localhost:8080/smartgridmanager/api/devices

# Test Reading API
curl -X GET http://localhost:8080/smartgridmanager/api/readings
```

### 3. Check Application Logs

```bash
# Tomcat logs
tail -f $CATALINA_HOME/logs/catalina.out

# Application logs (if configured)
tail -f $CATALINA_HOME/logs/smartgridmanager.log
```

### 4. Test All Pages

- Landing Page: http://localhost:8080/smartgridmanager/
- Dashboard: http://localhost:8080/smartgridmanager/home
- Devices: http://localhost:8080/smartgridmanager/devices
- Readings: http://localhost:8080/smartgridmanager/readings
- Statistics: http://localhost:8080/smartgridmanager/stats
- Generator: http://localhost:8080/smartgridmanager/generator

## Environment-Specific Configuration

### Development Environment

For development, consider these additional configurations:

#### Enable Hot Reload

Add to `$CATALINA_HOME/conf/context.xml`:

```xml
<Context reloadable="true">
  <WatchedResource>WEB-INF/web.xml</WatchedResource>
  <WatchedResource>WEB-INF/classes/</WatchedResource>
</Context>
```

#### Enable SQL Logging

In `persistence.xml`, set:

```xml
<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.format_sql" value="true"/>
```

### Production Environment

For production deployment:

#### 1. Disable SQL Logging

In `persistence.xml`:

```xml
<property name="hibernate.show_sql" value="false"/>
<property name="hibernate.format_sql" value="false"/>
```

#### 2. Change Database Mode

In `persistence.xml`:

```xml
<property name="hibernate.hbm2ddl.auto" value="validate"/>
```

#### 3. Use Strong Passwords

Change all default passwords:
- Database password
- Tomcat admin password

#### 4. Configure SSL/TLS

Edit `$CATALINA_HOME/conf/server.xml` to enable HTTPS.

#### 5. Set Production Tomcat Heap

In `setenv.sh` or `setenv.bat`:

```bash
export CATALINA_OPTS="-Xms2g -Xmx4g"
```

#### 6. Enable Access Logs

In `$CATALINA_HOME/conf/server.xml`, ensure AccessLogValve is enabled:

```xml
<Valve className="org.apache.catalina.valves.AccessLogValve"
       directory="logs"
       prefix="smartgrid_access_log" suffix=".txt"
       pattern="%h %l %u %t &quot;%r&quot; %s %b" />
```

## Troubleshooting

### Database Connection Issues

```bash
# Test MySQL connection
mysql -u smartgrid_user -p

# Check if MySQL is running
sudo systemctl status mysql   # Linux
brew services list            # macOS
net start | findstr MySQL     # Windows

# Check firewall settings
sudo ufw status              # Linux (UFW)
```

### Tomcat Issues

```bash
# Check if Tomcat is running
ps aux | grep tomcat         # Linux/macOS
netstat -ano | findstr 8080  # Windows

# Check port conflicts
sudo lsof -i :8080          # Linux/macOS
netstat -ano | findstr 8080  # Windows

# View full error logs
cat $CATALINA_HOME/logs/catalina.out
```

### Build Issues

```bash
# Clean Maven cache
mvn clean

# Update dependencies
mvn dependency:purge-local-repository

# Force update
mvn clean install -U
```

## Next Steps

After successful setup:

1. Review the [README.md](README.md) for usage instructions
2. Check [API.md](API.md) for API documentation
3. Read [ARCHITECTURE.md](ARCHITECTURE.md) for system architecture
4. Start customizing for your needs

---

**Need Help?** Open an issue on GitHub or check the main README for support options.
