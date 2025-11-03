<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Lectures - SmartGrid Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">

    <style>
        .table-scroll-container {
            max-height: 400px;
            overflow-y: auto;
        }
    </style>
</head>
<body>

<jsp:include page="/pages/_sidebar.jsp" />

<div class="main-content">

    <div class="container-fluid">
        <h2 class="text-center mb-4">📊 Manage Readings</h2>

        <div class="card p-4 shadow-sm mb-4">
            <h4 class="mb-3">➕ Add Reading</h4>
            <form method="post" action="${pageContext.request.contextPath}/readings" class="row g-3">
                <div class="col-md-4">
                    <label class="form-label">Device:</label>
                    <select name="deviceId" class="form-select" required>
                        <c:forEach var="d" items="${devices}">
                            <option value="${d.id}">${d.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Type:</label>
                    <select name="type" class="form-select" required>
                        <option value="power">Power</option>
                        <option value="temperature">Temperature</option>
                        <option value="motion">Motion</option>
                        <option value="luminosity">Luminosity</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Value:</label>
                    <input name="value" type="number" step="0.01" class="form-control" required/>
                </div>
                <div class="col-12 text-center">
                    <button type="submit" class="btn btn-success px-4">Add</button>
                </div>
            </form>
        </div>

        <div class="card p-4 shadow-sm mb-4">
            <h4 class="mb-3">🔍 Filter Readings</h4>
            <form method="get" action="${pageContext.request.contextPath}/readings" class="row g-3">
                <div class="col-md-4">
                    <label class="form-label">Device:</label>
                    <select name="deviceId" class="form-select">
                        <option value="">All</option>
                        <c:forEach var="d" items="${devices}">
                            <option value="${d.id}" <c:if test="${selectedDevice == d.id}">selected</c:if>>${d.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Type:</label>
                    <select name="type" class="form-select" required>
                        <option value="power">Power (W)</option>
                        <option value="voltage">Voltage (V)</option>
                        <option value="current">Current (A)</option>
                        <option value="temperature">Temperature (°C)</option>
                        <option value="humidity">Humidity (%)</option>
                        <option value="luminosity">Luminosity (lux)</option>
                        <option value="motion">Motion</option>
                        <option value="co2">CO₂ Level (ppm)</option>
                        <option value="energy">Energy (kWh)</option>
                        <option value="water_flow">Water Flow (L/min)</option>
                    </select>
                </div>
                <div class="col-md-4 align-self-end text-center">
                    <button type="submit" class="btn btn-primary px-4">Filter</button>
                </div>
            </form>
        </div>

        <div class="card p-3 shadow-sm">
            <h5 class="mb-3">📈 Readings List</h5>
            <div class="table-scroll-container">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-primary sticky-top">
                    <tr><th>Timestamp</th><th>Device</th><th>Type</th><th>Value</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="r" items="${readings}">
                        <tr>
                            <td>${r.timestamp}</td>
                            <td>${r.device.name}</td>
                            <td>${r.readingType}</td>
                            <td>${r.value}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div> </div> </body>
</html>