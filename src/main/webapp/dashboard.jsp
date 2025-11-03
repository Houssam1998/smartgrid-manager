<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (request.getAttribute("deviceCount") == null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Dashboard - SmartGrid Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">

    <style>
        .card { border-radius: 12px; }
        .summary-num { font-size: 1.8rem; font-weight: 700; }
        .alerts-list { max-height: 220px; overflow-y: auto; }
        .chart-container { height: 280px; width: 100%; position: relative; }
    </style>
</head>
<body>

<jsp:include page="/pages/_sidebar.jsp" />

<div class="main-content">

    <div class="row mb-4 text-center">
        <div class="col-md-3 mb-3">
            <div class="card p-3 shadow-sm">
                <div class="text-muted">Total Devices</div>
                <div class="summary-num">${deviceCount}</div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card p-3 shadow-sm">
                <div class="text-muted">Total Readings</div>
                <div class="summary-num">${readingCount}</div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card p-3 shadow-sm">
                <div class="text-muted">Active Alerts</div>
                <div class="summary-num text-danger">${alertCount}</div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card p-3 shadow-sm bg-info text-white">
                <div class="text-white-50">Météo (Fès)</div>
                <div class="summary-num">
                    <c:choose>
                        <c:when test="${not empty externalTemperature}">${externalTemperature}°C</c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <h2 class="text-center mb-4">🏠 Dashboard</h2>

    <div class="row mb-4">
        <div class="col-md-12 mb-3">
            <div class="card p-3 shadow-sm">
                <h5 class="mb-0">Top 10 Avg Power (by device)</h5>
                <div class="chart-container">
                    <canvas id="miniPowerChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-4 mb-3">
            <div class="card p-3 shadow-sm">
                <h6 class="mb-2">Prévisions Météo (7 Jours)</h6>
                <div class="chart-container" style="height: 220px;">
                    <canvas id="weatherForecastChart"></canvas>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card p-3 shadow-sm border border-danger">
                <h6 class="mb-2 text-danger">🚨 Intrusion Alerts</h6>
                <div class="alerts-list">
                    <c:if test="${not empty securityAlerts}">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="sa" items="${securityAlerts}">
                                <li class="list-group-item px-1">
                                    <strong>${sa[0]}</strong>
                                    <div class="text-muted small">Location: <span class="fw-bold">${sa[2]}</span></div>
                                    <div class="text-muted small">${sa[1]}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                    <c:if test="${empty securityAlerts}">
                        <div class="text-muted text-center p-3">No intrusion alerts detected.</div>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card p-3 shadow-sm">
                <h6 class="mb-2">Latest Readings</h6>
                <div class="alerts-list">
                    <c:if test="${not empty latestReadings}">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="r" items="${latestReadings}">
                                <li class="list-group-item px-1">
                                    <div><strong>${r.device.name}</strong> — ${r.readingType} = ${r.value}</div>
                                    <div class="text-muted small">${r.timestamp}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                    <c:if test="${empty latestReadings}">
                        <div class="text-muted text-center p-3">No recent readings.</div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <div class="card shadow-sm p-4 mb-5 text-center" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
        <h4 class="mb-3">Need to generate test data?</h4>
        <p class="mb-3">Use our advanced data generator to create realistic smart home scenarios</p>
        <a href="${pageContext.request.contextPath}/generator" class="btn btn-light btn-lg">
            <i class="fas fa-magic"></i> Open Generator
        </a>
    </div>

</div> <script>
    // --- Graphique 1 : Top 10 Power ---
    const labels = [<c:choose><c:when test="${not empty avgPower}"><c:forEach var="row" items="${avgPower}" varStatus="status">'<c:out value="${row[0]}" />'<c:if test="${!status.last}">,</c:if></c:forEach></c:when><c:otherwise>'No data'</c:otherwise></c:choose>];
    const dataPoints = [<c:choose><c:when test="${not empty avgPower}"><c:forEach var="row" items="${avgPower}" varStatus="status">${row[1]}<c:if test="${!status.last}">,</c:if></c:forEach></c:when><c:otherwise>0</c:otherwise></c:choose>];
    const ctx = document.getElementById('miniPowerChart');
    if (ctx) {
        const hasRealData = labels.length > 0 && !(labels.length === 1 && labels[0] === 'No data');
        if (hasRealData) {
            new Chart(ctx, {
                type: 'bar',
                data: { labels: labels, datasets: [{ label: 'Avg Power (W)', data: dataPoints, backgroundColor: 'rgba(54,162,235,0.7)', borderColor: 'rgba(54,162,235,1)', borderWidth: 1 }] },
                options: {
                    responsive: true, maintainAspectRatio: false,
                    scales: { y: { beginAtZero: true, ticks: { maxTicksLimit: 5 } }, x: { ticks: { maxRotation: 45, minRotation: 0, autoSkip: true, maxTicksLimit: 10 }, grid: { display: false } } },
                    plugins: { legend: { display: false }, tooltip: { callbacks: { label: function(context) { return `Power: ${context.parsed.y.toFixed(2)}W`; } } } },
                    layout: { padding: { left: 5, right: 5, top: 5, bottom: 5 } }
                }
            });
        } else {
            const ctx2d = ctx.getContext('2d'); ctx2d.font = '14px Arial'; ctx2d.fillStyle = '#6c757d'; ctx2d.textAlign = 'center'; ctx2d.fillText('No power data available', ctx.width / 2, ctx.height / 2);
        }
    }

    // --- Graphique 2 : Prévisions Météo ---
    const ctxWeather = document.getElementById('weatherForecastChart');
    if (ctxWeather && '${not empty dailyForecast}') {
        const forecastLabels = [<c:forEach var="dateStr" items="${dailyForecast.time}">'${dateStr.substring(8, 10)}/${dateStr.substring(5, 7)}',</c:forEach>];
        const forecastMax = [ <c:forEach var="temp" items="${dailyForecast.temperatureMax}"> ${temp}, </c:forEach> ];
        const forecastMin = [ <c:forEach var="temp" items="${dailyForecast.temperatureMin}"> ${temp}, </c:forEach> ];
        new Chart(ctxWeather, {
            type: 'line',
            data: {
                labels: forecastLabels,
                datasets: [
                    { label: 'Max T°C', data: forecastMax, borderColor: 'rgba(255, 99, 132, 1)', backgroundColor: 'rgba(255, 99, 132, 0.3)', tension: 0.3 },
                    { label: 'Min T°C', data: forecastMin, borderColor: 'rgba(54, 162, 235, 1)', backgroundColor: 'rgba(54, 162, 235, 0.3)', tension: 0.3 }
                ]
            },
            options: {
                responsive: true, maintainAspectRatio: false,
                scales: { y: { ticks: { callback: function(value) { return value + '°C' } } }, x: { grid: { display: false } } },
                plugins: { legend: { display: true, position: 'bottom', labels: { boxWidth: 12 } } }
            }
        });
    }
</script>

</body>
</html>