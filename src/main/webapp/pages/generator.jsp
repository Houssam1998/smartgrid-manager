<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Data Generator - SmartGrid Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .generator-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem;
        }
        .generator-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .card-header-custom {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem;
            text-align: center;
        }
        .card-header-custom h1 {
            margin: 0;
            font-size: 2.5rem;
            font-weight: 700;
        }
        .card-header-custom p {
            margin: 0.5rem 0 0 0;
            opacity: 0.9;
        }
        .card-body-custom {
            padding: 2rem;
        }
        .preset-card {
            border: 2px solid #e9ecef;
            border-radius: 12px;
            padding: 1.5rem;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            height: 100%;
        }
        .preset-card:hover {
            border-color: #667eea;
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.2);
        }
        .preset-card.active {
            border-color: #667eea;
            background: #f8f9ff;
        }
        .preset-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            color: #667eea;
        }
        .preset-title {
            font-weight: 600;
            font-size: 1.1rem;
            margin-bottom: 0.5rem;
        }
        .preset-desc {
            color: #6c757d;
            font-size: 0.9rem;
        }
        .section-title {
            font-size: 1.3rem;
            font-weight: 600;
            color: #667eea;
            margin-bottom: 1.5rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid #e9ecef;
        }
        .form-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 0.5rem;
        }
        .form-control, .form-select {
            border-radius: 8px;
            border: 2px solid #e9ecef;
            padding: 0.75rem;
        }
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .param-toggle-container {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1rem;
        }
        .param-checkbox {
            width: 20px;
            height: 20px;
            cursor: pointer;
        }
        .param-disabled {
            opacity: 0.5;
            pointer-events: none;
        }
        .slider-container {
            padding: 0.5rem;
            background: #f8f9fa;
            border-radius: 8px;
        }
        .alert-value {
            font-size: 1.5rem;
            font-weight: 700;
            color: #667eea;
        }
        .btn-generate {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            color: white;
            padding: 1rem 3rem;
            font-size: 1.2rem;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s;
        }
        .btn-generate:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
        }
        .btn-reset {
            border: 2px solid #667eea;
            color: #667eea;
            padding: 1rem 2rem;
            font-weight: 600;
            border-radius: 50px;
            background: white;
            transition: all 0.3s;
        }
        .btn-reset:hover {
            background: #667eea;
            color: white;
        }
        .stats-badge {
            background: #f8f9ff;
            border: 2px solid #667eea;
            border-radius: 12px;
            padding: 1rem;
            text-align: center;
        }
        .stats-number {
            font-size: 2rem;
            font-weight: 700;
            color: #667eea;
        }
        .stats-label {
            color: #6c757d;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark" style="background: rgba(0,0,0,0.1);">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/landing.jsp">
            <i class="fas fa-bolt"></i> SmartGrid Manager
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <i class="fas fa-home"></i> Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/devices">
                        <i class="fas fa-microchip"></i> Devices
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/stats">
                        <i class="fas fa-chart-line"></i> Stats
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/generator">
                        <i class="fas fa-magic"></i> Generator
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="generator-container">
    <div class="generator-card">
        <!-- Header -->
        <div class="card-header-custom">
            <h1><i class="fas fa-magic"></i> Smart Data Generator</h1>
            <p>Generate realistic smart home data for testing and development</p>
        </div>

        <!-- Body -->
        <div class="card-body-custom">

            <!-- Messages -->
            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle"></i> <strong>Success!</strong> ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> <strong>Error!</strong> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Quick Presets -->
            <div class="mb-5">
                <h3 class="section-title">
                    <i class="fas fa-lightning-bolt"></i> Quick Presets
                </h3>
                <div class="row g-3">
                    <div class="col-md-3">
                        <div class="preset-card" onclick="applyPreset('random')">
                            <div class="preset-icon">🎲</div>
                            <div class="preset-title">Random Mix</div>
                            <div class="preset-desc">20 devices, fully random data</div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="preset-card" onclick="applyPreset('kitchen')">
                            <div class="preset-icon">🍳</div>
                            <div class="preset-title">Kitchen Monitoring</div>
                            <div class="preset-desc">Kitchen appliances only</div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="preset-card" onclick="applyPreset('security')">
                            <div class="preset-icon">🔒</div>
                            <div class="preset-title">Security System</div>
                            <div class="preset-desc">Sensors & cameras</div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="preset-card" onclick="applyPreset('energy')">
                            <div class="preset-icon">⚡</div>
                            <div class="preset-title">Energy Audit</div>
                            <div class="preset-desc">Power consumption focus</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Custom Configuration -->
            <form action="${pageContext.request.contextPath}/generate" method="post" id="generatorForm">

                <!-- Basic Settings -->
                <div class="mb-5">
                    <h3 class="section-title">
                        <i class="fas fa-sliders-h"></i> Basic Settings
                    </h3>
                    <div class="row g-4">
                        <div class="col-md-6">
                            <label class="form-label">
                                <i class="fas fa-microchip"></i> Number of Devices
                            </label>
                            <input type="number" name="deviceCount" id="deviceCount"
                                   value="10" min="1" max="100" class="form-control" required>
                            <small class="text-muted">Between 1 and 100 devices</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">
                                <i class="fas fa-database"></i> Readings per Device
                            </label>
                            <input type="number" name="readingsPerDevice" id="readingsPerDevice"
                                   value="500" min="10" max="5000" class="form-control" required>
                            <small class="text-muted">Between 10 and 5000 readings</small>
                        </div>
                    </div>
                </div>

                <!-- Advanced Parameters -->
                <div class="mb-5">
                    <h3 class="section-title">
                        <i class="fas fa-cogs"></i> Advanced Parameters
                    </h3>

                    <!-- Device Type -->
                    <div class="param-toggle-container">
                        <div class="form-check mb-3">
                            <input class="form-check-input param-checkbox" type="checkbox"
                                   id="useDeviceType" onchange="toggleParam('deviceType')">
                            <label class="form-check-label fw-bold" for="useDeviceType">
                                <i class="fas fa-microchip"></i> Fix Device Type
                            </label>
                        </div>
                        <select name="deviceType" id="deviceType" class="form-select param-disabled" disabled>
                            <option value="">Random</option>
                            <option value="SmartMeter">SmartMeter</option>
                            <option value="Thermostat">Thermostat</option>
                            <option value="AC_Unit">AC Unit</option>
                            <option value="LightSensor">Light Sensor</option>
                            <option value="MotionSensor">Motion Sensor</option>
                            <option value="WaterHeater">Water Heater</option>
                            <option value="Refrigerator">Refrigerator</option>
                            <option value="WashingMachine">Washing Machine</option>
                            <option value="SolarPanel">Solar Panel</option>
                            <option value="SecurityCamera">Security Camera</option>
                            <option value="SmokeDetector">Smoke Detector</option>
                            <option value="HVAC_System">HVAC System</option>
                        </select>
                    </div>

                    <!-- Location -->
                    <div class="param-toggle-container">
                        <div class="form-check mb-3">
                            <input class="form-check-input param-checkbox" type="checkbox"
                                   id="useLocation" onchange="toggleParam('location')">
                            <label class="form-check-label fw-bold" for="useLocation">
                                <i class="fas fa-map-marker-alt"></i> Fix Location
                            </label>
                        </div>
                        <select name="location" id="location" class="form-select param-disabled" disabled>
                            <option value="">Random</option>
                            <option value="Living Room">Living Room</option>
                            <option value="Kitchen">Kitchen</option>
                            <option value="Master Bedroom">Master Bedroom</option>
                            <option value="Guest Bedroom">Guest Bedroom</option>
                            <option value="Bathroom">Bathroom</option>
                            <option value="Garage">Garage</option>
                            <option value="Garden">Garden</option>
                            <option value="Basement">Basement</option>
                            <option value="Attic">Attic</option>
                            <option value="Office">Office</option>
                        </select>
                    </div>

                    <!-- Reading Type -->
                    <div class="param-toggle-container">
                        <div class="form-check mb-3">
                            <input class="form-check-input param-checkbox" type="checkbox"
                                   id="useReadingType" onchange="toggleParam('readingType')">
                            <label class="form-check-label fw-bold" for="useReadingType">
                                <i class="fas fa-chart-line"></i> Fix Reading Type
                            </label>
                        </div>
                        <select name="readingType" id="readingType" class="form-select param-disabled" disabled>
                            <option value="">Random</option>
                            <option value="power">Power (W)</option>
                            <option value="voltage">Voltage (V)</option>
                            <option value="current">Current (A)</option>
                            <option value="temperature">Temperature (°C)</option>
                            <option value="humidity">Humidity (%)</option>
                            <option value="co2">CO2 (ppm)</option>
                            <option value="motion">Motion</option>
                            <option value="luminosity">Luminosity (lux)</option>
                        </select>
                    </div>

                    <!-- Alert Probability -->
                    <div class="param-toggle-container">
                        <label class="form-label">
                            <i class="fas fa-exclamation-triangle"></i> Alert Probability
                        </label>
                        <div class="slider-container">
                            <div class="row align-items-center">
                                <div class="col-9">
                                    <input type="range" name="alertProbability" id="alertProbability"
                                           value="5" min="0" max="30" class="form-range"
                                           oninput="updateAlertLabel()">
                                </div>
                                <div class="col-3 text-center">
                                    <div class="alert-value" id="alertLabel">5%</div>
                                </div>
                            </div>
                        </div>
                        <small class="text-muted">Percentage of readings that will trigger alerts</small>
                    </div>
                </div>

                <!-- Estimated Stats -->
                <div class="mb-4">
                    <h3 class="section-title">
                        <i class="fas fa-calculator"></i> Estimated Output
                    </h3>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <div class="stats-badge">
                                <div class="stats-number" id="totalReadings">5,000</div>
                                <div class="stats-label">Total Readings</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="stats-badge">
                                <div class="stats-number" id="totalDevices">10</div>
                                <div class="stats-label">Total Devices</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="stats-badge">
                                <div class="stats-number" id="estimatedTime">~2s</div>
                                <div class="stats-label">Estimated Time</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="d-flex gap-3 justify-content-center">
                    <button type="submit" class="btn btn-generate">
                        <i class="fas fa-rocket"></i> Generate Data
                    </button>
                    <button type="button" class="btn btn-reset" onclick="resetForm()">
                        <i class="fas fa-redo"></i> Reset
                    </button>
                </div>
            </form>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Toggle parameter enable/disable
    function toggleParam(paramName) {
        const checkbox = document.getElementById('use' + paramName.charAt(0).toUpperCase() + paramName.slice(1));
        const select = document.getElementById(paramName);

        if (checkbox.checked) {
            select.disabled = false;
            select.classList.remove('param-disabled');
        } else {
            select.disabled = true;
            select.classList.add('param-disabled');
            select.value = '';
        }
    }

    // Update alert probability label
    function updateAlertLabel() {
        const slider = document.getElementById('alertProbability');
        const label = document.getElementById('alertLabel');
        label.textContent = slider.value + '%';
        updateStats();
    }

    // Apply preset configurations
    function applyPreset(presetName) {
        resetForm();

        switch(presetName) {
            case 'random':
                document.getElementById('deviceCount').value = 20;
                document.getElementById('readingsPerDevice').value = 500;
                document.getElementById('alertProbability').value = 5;
                break;

            case 'kitchen':
                document.getElementById('deviceCount').value = 8;
                document.getElementById('readingsPerDevice').value = 800;
                document.getElementById('useLocation').checked = true;
                toggleParam('location');
                document.getElementById('location').value = 'Kitchen';
                document.getElementById('alertProbability').value = 8;
                break;

            case 'security':
                document.getElementById('deviceCount').value = 15;
                document.getElementById('readingsPerDevice').value = 1000;
                document.getElementById('useDeviceType').checked = true;
                toggleParam('deviceType');
                document.getElementById('deviceType').value = 'SecurityCamera';
                document.getElementById('alertProbability').value = 3;
                break;

            case 'energy':
                document.getElementById('deviceCount').value = 12;
                document.getElementById('readingsPerDevice').value = 600;
                document.getElementById('useReadingType').checked = true;
                toggleParam('readingType');
                document.getElementById('readingType').value = 'power';
                document.getElementById('alertProbability').value = 10;
                break;
        }

        updateAlertLabel();
        updateStats();
    }

    // Reset form
    function resetForm() {
        document.getElementById('generatorForm').reset();
        document.getElementById('deviceType').disabled = true;
        document.getElementById('location').disabled = true;
        document.getElementById('readingType').disabled = true;
        document.getElementById('deviceType').classList.add('param-disabled');
        document.getElementById('location').classList.add('param-disabled');
        document.getElementById('readingType').classList.add('param-disabled');
        updateAlertLabel();
        updateStats();
    }

    // Update estimated stats
    function updateStats() {
        const devices = parseInt(document.getElementById('deviceCount').value) || 0;
        const readings = parseInt(document.getElementById('readingsPerDevice').value) || 0;
        const total = devices * readings;

        document.getElementById('totalReadings').textContent = total.toLocaleString();
        document.getElementById('totalDevices').textContent = devices;

        // Rough time estimation (ms per reading)
        const estimatedMs = total * 0.5;
        let timeStr;
        if (estimatedMs < 1000) {
            timeStr = '~' + Math.round(estimatedMs) + 'ms';
        } else if (estimatedMs < 60000) {
            timeStr = '~' + Math.round(estimatedMs / 1000) + 's';
        } else {
            timeStr = '~' + Math.round(estimatedMs / 60000) + 'min';
        }
        document.getElementById('estimatedTime').textContent = timeStr;
    }

    // Update stats on input change
    document.getElementById('deviceCount').addEventListener('input', updateStats);
    document.getElementById('readingsPerDevice').addEventListener('input', updateStats);

    // Initial update
    updateStats();
</script>

</body>
</html>