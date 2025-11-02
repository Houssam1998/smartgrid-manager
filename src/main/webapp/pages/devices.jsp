<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Devices</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">⚡ SmartGrid Manager</a>
    </div>
</nav>

<div class="container mt-5">
    <h2 class="text-center mb-4">📡 Registered Devices</h2>

    <table class="table table-bordered table-hover bg-white shadow-sm">
        <thead class="table-primary text-center">
        <tr><th>ID</th><th>Name</th><th>Type</th><th>Location</th><th>Actions</th></tr>
        </thead>
        <tbody>
        <c:forEach var="d" items="${devices}">
            <tr>
                <td>${d.id}</td>
                <td>${d.name}</td>
                <td>${d.deviceType}</td>
                <td>${d.location}</td>
                <td class="text-center">
                    <form action="${pageContext.request.contextPath}/devices" method="post" style="display:inline;">
                        <input type="hidden" name="deleteId" value="${d.id}">
                        <button class="btn btn-sm btn-danger">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="card p-4 mt-4 shadow">
        <h4>Add new device</h4>
        <form method="post" action="${pageContext.request.contextPath}/devices" class="row g-3">
            <div class="col-md-4">
                <input name="name" class="form-control" placeholder="Name" required />
            </div>
            <div class="col-md-4">
                <input name="type" class="form-control" placeholder="Type (e.g. SmartMeter)" />
            </div>
            <div class="col-md-4">
                <input name="location" class="form-control" placeholder="Location" />
            </div>
            <div class="col-md-12 text-center">
                <button type="submit" class="btn btn-success mt-2">Add Device</button>
            </div>
        </form>
    </div>

    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-outline-secondary">⬅ Back to Home</a>
    </div>
</div>

</body>
</html>
