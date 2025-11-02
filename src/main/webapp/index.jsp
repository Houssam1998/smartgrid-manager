<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // Si on accède directement à index.jsp, rediriger vers le servlet
    if (request.getAttribute("deviceCount") == null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>SmartGrid Data Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { background-color: #f8f9fa; }
        .card { border-radius: 12px; }
        .summary-num { font-size: 1.8rem; font-weight: 700; }
        .mini-chart-container {
            height: 120px;
            width: 100%;
            position: relative;
        }
        .alerts-list { max-height: 220px; overflow-y: auto; }
        #miniPowerChart {
            max-width: 100%;
            display: block;
        }
    </style>
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">⚡ SmartGrid Manager</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/devices">Devices</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/readings">Readings</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/stats">Stats</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/generator">
                        <i class="fas fa-magic"></i> Generator
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container mt-5">

    <!-- Titre -->
    <h2 class="text-center mb-4">🏠 Welcome to SmartGrid Data Manager</h2>

    <!-- Navigation principale -->
    <div class="d-flex justify-content-center gap-3 mb-4">
        <a href="${pageContext.request.contextPath}/devices" class="btn btn-outline-primary btn-lg">Manage Devices</a>
        <a href="${pageContext.request.contextPath}/readings" class="btn btn-outline-success btn-lg">Manage Readings</a>
        <a href="${pageContext.request.contextPath}/stats" class="btn btn-outline-info btn-lg">View Stats</a>
        <a href="${pageContext.request.contextPath}/generator" class="btn btn-outline-warning btn-lg">
            ⚡ Data Generator
        </a>
    </div>

    <!-- Résumé (cartes) -->
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
                <div class="text-muted">Avg Power (W)</div>
                <div class="summary-num">${avgPowerGlobal}</div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card p-3 shadow-sm">
                <div class="text-muted">Active Alerts</div>
                <div class="summary-num text-danger">${alertCount}</div>
            </div>
        </div>
    </div>

    <!-- Mini chart + Alerts récents + Last readings -->
    <div class="row mb-4">
        <!-- mini chart consommation -->
        <div class="col-md-6 mb-3">
            <div class="card p-3 shadow-sm">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h5 class="mb-0">Top 10 Avg Power (by device)</h5>
                    <small class="text-muted">Live snapshot</small>
                </div>
                <div class="mini-chart-container">
                    <canvas id="miniPowerChart"></canvas>
                </div>
            </div>
        </div>

        <!-- Alerts récentes -->
        <div class="col-md-3 mb-3">
            <div class="card p-3 shadow-sm">
                <h6 class="mb-2">Recent Alerts</h6>
                <div class="alerts-list">
                    <c:if test="${not empty recentAlerts}">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="a" items="${recentAlerts}">
                                <li class="list-group-item">
                                    <strong>${a[0]}</strong> — ${a[1]} = <span class="text-danger">${a[2]}</span>
                                    <div class="text-muted small">${a[3]}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                    <c:if test="${empty recentAlerts}">
                        <div class="text-muted">No alerts</div>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Last readings -->
        <div class="col-md-3 mb-3">
            <div class="card p-3 shadow-sm">
                <h6 class="mb-2">Latest Readings</h6>
                <div style="max-height:220px; overflow-y:auto;">
                    <c:if test="${not empty latestReadings}">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="r" items="${latestReadings}">
                                <li class="list-group-item">
                                    <div><strong>${r.device.name}</strong> — ${r.readingType} = ${r.value}</div>
                                    <div class="text-muted small">${r.timestamp}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                    <c:if test="${empty latestReadings}">
                        <div class="text-muted">No recent readings</div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick link to generator -->
    <div class="card shadow-sm p-4 mb-5 text-center" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
        <h4 class="mb-3">Need to generate test data?</h4>
        <p class="mb-3">Use our advanced data generator to create realistic smart home scenarios</p>
        <a href="${pageContext.request.contextPath}/generator" class="btn btn-light btn-lg">
            <i class="fas fa-magic"></i> Open Generator
        </a>
    </div>

    <div class="text-center mb-5">
        <small class="text-muted">SmartGrid Manager • Smart Home Edition</small>
    </div>

</div>

<!-- Chart.js logic -->
<script>
    const labels = [
        <c:choose>
        <c:when test="${not empty avgPower}">
        <c:forEach var="row" items="${avgPower}" varStatus="status">
        '<c:out value="${row[0]}" />'<c:if test="${!status.last}">,</c:if>
        </c:forEach>
        </c:when>
        <c:otherwise>'No data'</c:otherwise>
        </c:choose>
    ];

    const dataPoints = [
        <c:choose>
        <c:when test="${not empty avgPower}">
        <c:forEach var="row" items="${avgPower}" varStatus="status">
        ${row[1]}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
        </c:when>
        <c:otherwise>0</c:otherwise>
        </c:choose>
    ];

    const ctx = document.getElementById('miniPowerChart');

    if (ctx) {
        const hasRealData = labels.length > 0 &&
            !(labels.length === 1 && labels[0] === 'No data') &&
            dataPoints.length > 0 &&
            !(dataPoints.length === 1 && dataPoints[0] === 0);

        if (hasRealData) {
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Avg Power (W)',
                        data: dataPoints,
                        backgroundColor: 'rgba(54,162,235,0.7)',
                        borderColor: 'rgba(54,162,235,1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: { beginAtZero: true, ticks: { maxTicksLimit: 5 }, grid: { display: true } },
                        x: { ticks: { maxRotation: 45, minRotation: 0, autoSkip: true, maxTicksLimit: 10 }, grid: { display: false } }
                    },
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return `Power: ${context.parsed.y.toFixed(2)}W`;
                                }
                            }
                        }
                    },
                    layout: { padding: { left: 5, right: 5, top: 5, bottom: 5 } }
                }
            });
        } else {
            const ctx2d = ctx.getContext('2d');
            ctx2d.font = '14px Arial';
            ctx2d.fillStyle = '#6c757d';
            ctx2d.textAlign = 'center';
            ctx2d.fillText('No power data available', ctx.width / 2, ctx.height / 2);
        }
    }
</script>

</body>
</html>