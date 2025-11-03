<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Statistiques - SmartGrid Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            /* 🔹 AJOUTÉ : Empêche le contenu d'être caché sous la navbar 🔹 */
            background-color: #f8f9fa;
        }
        .chart-card { height: 360px; }
        .table-scroll-container { max-height: 350px; overflow-y: auto; }
    </style>
</head>
<body>

<jsp:include page="/pages/_navbar.jsp" />

<div class="container-fluid mt-4 p-4">

    <h2 class="text-center mb-4">📊 SmartGrid Statistical Dashboard</h2>

    <div class="row text-center mb-4">
        <div class="col-md-3 mb-3">
            <div class="card bg-success text-white p-3 shadow">
                <h6>Total Devices</h6>
                <h3>${deviceCount}</h3>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card bg-info text-white p-3 shadow">
                <h6>Total Readings</h6>
                <h3>${readingCount}</h3>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card bg-warning text-white p-3 shadow">
                <h6>Average Power (W)</h6>
                <h3>${avgPowerGlobal}</h3>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card bg-danger text-white p-3 shadow">
                <h6>Active Alerts</h6>
                <h3>${alertCount}</h3>
            </div>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-6 mb-3">
            <div class="card p-3 shadow">
                <h5>Average / Min / Max by Reading Type</h5>
                <div class="table-scroll-container">
                    <table class="table table-striped table-hover align-middle">
                        <thead class="table-primary sticky-top">
                        <tr><th>Type</th><th>Average</th><th>Min</th><th>Max</th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="row" items="${stats}">
                            <tr>
                                <td>${row[0]}</td>
                                <td><c:out value="${String.format('%.2f', row[1])}"/></td>
                                <td><c:out value="${String.format('%.2f', row[2])}"/></td>
                                <td><c:out value="${String.format('%.2f', row[3])}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-md-6 mb-3">
            <div class="card p-3 shadow">
                <h5>Count by Device</h5>
                <div class="table-scroll-container">
                    <table class="table table-striped table-hover align-middle">
                        <thead class="table-primary sticky-top">
                        <tr><th>Device</th><th>Number of Readings</th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="row" items="${counts}">
                            <tr><td>${row[0]}</td><td>${row[1]}</td></tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-6 mb-3">
            <div class="card p-3 shadow chart-card">
                <h5 class="mb-3">Average Power by Device (W)</h5>
                <canvas id="chartPower"></canvas>
            </div>
        </div>
        <div class="col-md-6 mb-3">
            <div class="card p-3 shadow chart-card">
                <h5 class="mb-3">Average Temperature by Device (°C)</h5>
                <canvas id="chartTemp"></canvas>
            </div>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-12">
            <div class="card p-3 shadow">
                <h5 class="mb-3">Reading Evolution over Time</h5>
                <form method="get" action="${pageContext.request.contextPath}/stats" class="row g-3 mb-3">
                    <div class="col-md-5">
                        <label class="form-label">Device:</label>
                        <select name="chartDeviceId" class="form-select" required>
                            <option value="">-- Select Device --</option>
                            <c:forEach var="d" items="${devices}">
                                <option value="${d.id}" <c:if test="${selectedChartDeviceId == d.id}">selected</c:if>>
                                        ${d.name} (${d.location})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-5">
                        <label class="form-label">Reading Type:</label>
                        <select name="chartReadingType" class="form-select" required>
                            <option value="">-- Select Type --</option>
                            <c:set var="type" value="${selectedChartReadingType}" />
                            <option value="power" ${type == 'power' ? 'selected' : ''}>Power (W)</option>
                            <option value="voltage" ${type == 'voltage' ? 'selected' : ''}>Voltage (V)</option>
                            <option value="current" ${type == 'current' ? 'selected' : ''}>Current (A)</option>
                            <option value="temperature" ${type == 'temperature' ? 'selected' : ''}>Temperature (°C)</option>
                            <option value="humidity" ${type == 'humidity' ? 'selected' : ''}>Humidity (%)</option>
                            <option value="co2" ${type == 'co2' ? 'selected' : ''}>CO2 (ppm)</option>
                            <option value="motion" ${type == 'motion' ? 'selected' : ''}>Motion</option>
                            <option value="luminosity" ${type == 'luminosity' ? 'selected' : ''}>Luminosity (lux)</option>
                        </select>
                    </div>
                    <div class="col-md-2 align-self-end">
                        <button type="submit" class="btn btn-primary w-100">Load Chart</button>
                    </div>
                </form>
                <div class="chart-card">
                    <canvas id="chartEvolution"></canvas>
                </div>
            </div>
        </div>
    </div>

    <div class="card p-3 shadow mb-5">
        <h5 class="mb-3 text-danger">⚠️ Active Alerts (Critical Readings)</h5>
        <div class="table-scroll-container">
            <table class="table table-striped table-hover align-middle">
                <thead class="table-danger sticky-top">
                <tr><th>Device</th><th>Type</th><th>Value</th><th>Timestamp</th></tr>
                </thead>
                <tbody>
                <c:forEach var="a" items="${alerts}">
                    <tr>
                        <td>${a[0]}</td>
                        <td>${a[1]}</td>
                        <td class="fw-bold text-danger">${a[2]}</td>
                        <td>${a[3]}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty alerts}">
                    <tr><td colspan="4" class="text-center">No active alerts found.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>

</div> <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // --- Graphique 1 : Average Power ---
    const ctxPower = document.getElementById('chartPower');
    new Chart(ctxPower, {
        type: 'bar',
        data: {
            labels: [<c:forEach var="row" items="${avgPower}">'${row[0]}',</c:forEach>],
            datasets: [{ label: 'Average Power (W)', data: [<c:forEach var="row" items="${avgPower}">${row[1]},</c:forEach>], backgroundColor: 'rgba(54, 162, 235, 0.7)', borderColor: 'rgba(54, 162, 235, 1)', borderWidth: 1 }]
        },
        options: {
            responsive: true, maintainAspectRatio: false,
            scales: { y: { beginAtZero: true }, x: { ticks: { maxRotation: 45, minRotation: 30, font: { size: 10 }, padding: 5 }, grid: { display: false } } },
            plugins: { legend: { display: false } },
            layout: { padding: { left: 5, right: 5, top: 0, bottom: 20 } }
        }
    });

    // --- Graphique 2 : Average Temperature ---
    const ctxTemp = document.getElementById('chartTemp');
    new Chart(ctxTemp, {
        type: 'line',
        data: {
            labels: [<c:forEach var="row" items="${avgTemp}">'${row[0]}',</c:forEach>],
            datasets: [{ label: 'Average Temperature (°C)', data: [<c:forEach var="row" items="${avgTemp}">${row[1]},</c:forEach>], borderColor: 'rgba(255, 99, 132, 1)', backgroundColor: 'rgba(255, 99, 132, 0.3)', borderWidth: 2, tension: 0.3 }]
        },
        options: {
            responsive: true, maintainAspectRatio: false,
            scales: { y: { beginAtZero: true }, x: { ticks: { maxRotation: 45, minRotation: 30, font: { size: 10 }, padding: 5 }, grid: { display: false } } },
            plugins: { legend: { display: false } },
            layout: { padding: { left: 5, right: 5, top: 0, bottom: 20 } }
        }
    });

    // --- Graphique 3 : Évolution ---
    const ctxEvo = document.getElementById('chartEvolution');
    const evoLabels = [<c:forEach var="r" items="${chartReadings}" varStatus="status">'${r.timestamp}'<c:if test="${!status.last}">,</c:if></c:forEach>];
    const evoData = [<c:forEach var="r" items="${chartReadings}" varStatus="status">${r.value}<c:if test="${!status.last}">,</c:if></c:forEach>];
    if (evoData.length > 0) {
        new Chart(ctxEvo, {
            type: 'line',
            data: {
                labels: evoLabels,
                datasets: [{ label: 'Evolution of ${selectedChartReadingType}', data: evoData, borderColor: 'rgba(75, 192, 192, 1)', backgroundColor: 'rgba(75, 192, 192, 0.3)', borderWidth: 2, tension: 0.1, pointRadius: 1 }]
            },
            options: {
                responsive: true, maintainAspectRatio: false,
                scales: { y: { beginAtZero: false }, x: { ticks: { display: evoLabels.length < 100, autoSkip: true, maxTicksLimit: 20 } } },
                plugins: { legend: { display: true }, tooltip: { mode: 'index', intersect: false } }
            }
        });
    } else if ('${selectedChartDeviceId}' != '') {
        const ctx = ctxEvo.getContext('2d'); ctx.textAlign = 'center'; ctx.font = '16px Arial'; ctx.fillStyle = '#6c757d'; ctx.fillText('No data found for this device and reading type.', ctxEvo.width / 2, ctxEvo.height / 2);
    }
</script>

</body>
</html>