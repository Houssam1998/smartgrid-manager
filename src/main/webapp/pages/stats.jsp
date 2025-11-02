<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>SmartGrid Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body { background-color: #f8f9fa; }
        .card { border-radius: 15px; }
        h2 { font-weight: 700; color: #0d6efd; }
        .chart-card { height: 360px; }
    </style>
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/index.jsp">⚡ SmartGrid Manager</a>
    </div>
</nav>

<div class="container mt-5">

    <!-- Titre principal -->
    <h2 class="text-center mb-4">📊 SmartGrid Statistical Dashboard</h2>

    <!-- Résumé global -->
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

    <!-- Stats tables -->
    <div class="row mb-4">
        <div class="col-md-6 mb-3">
            <div class="card p-3 shadow">
                <h5>Average / Min / Max by Reading Type</h5>
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-primary">
                    <tr>
                        <th>Type</th>
                        <th>Average</th>
                        <th>Min</th>
                        <th>Max</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="row" items="${stats}">
                        <tr>
                            <td>${row[0]}</td>
                            <td>${row[1]}</td>
                            <td>${row[2]}</td>
                            <td>${row[3]}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="col-md-6 mb-3">
            <div class="card p-3 shadow">
                <h5>Count by Device</h5>
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-primary">
                    <tr>
                        <th>Device</th>
                        <th>Number of Readings</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="row" items="${counts}">
                        <tr>
                            <td>${row[0]}</td>
                            <td>${row[1]}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Graphiques -->
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

    <!-- Alertes -->
    <div class="card p-3 shadow mb-5">
        <h5 class="mb-3 text-danger">⚠️ Active Alerts (Critical Readings)</h5>
        <table class="table table-striped table-hover align-middle">
            <thead class="table-danger">
            <tr>
                <th>Device</th>
                <th>Type</th>
                <th>Value</th>
                <th>Timestamp</th>
            </tr>
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
            </tbody>
        </table>
    </div>

    <div class="text-center mb-4">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-outline-secondary">
            ⬅ Back to Home
        </a>
    </div>

</div>

<!-- ====== Chart.js scripts ====== -->
<script>
    // --- Average Power Chart ---
    const ctxPower = document.getElementById('chartPower');
    new Chart(ctxPower, {
        type: 'bar',
        data: {
            labels: [<c:forEach var="row" items="${avgPower}">'${row[0]}',</c:forEach>],
            datasets: [{
                label: 'Average Power (W)',
                data: [<c:forEach var="row" items="${avgPower}">${row[1]},</c:forEach>],
                backgroundColor: 'rgba(54, 162, 235, 0.7)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: { y: { beginAtZero: true } },
            plugins: { legend: { display: false } }
        }
    });

    // --- Average Temperature Chart ---
    const ctxTemp = document.getElementById('chartTemp');
    new Chart(ctxTemp, {
        type: 'line',
        data: {
            labels: [<c:forEach var="row" items="${avgTemp}">'${row[0]}',</c:forEach>],
            datasets: [{
                label: 'Average Temperature (°C)',
                data: [<c:forEach var="row" items="${avgTemp}">${row[1]},</c:forEach>],
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.3)',
                borderWidth: 2,
                tension: 0.3
            }]
        },
        options: {
            scales: { y: { beginAtZero: true } },
            plugins: { legend: { display: false } }
        }
    });
</script>

</body>
</html>
