<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Appareils - SmartGrid Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            /* 🔹 AJOUTÉ : Empêche le contenu d'être caché sous la navbar 🔹 */
            background-color: #f8f9fa;
        }
        .table-scroll-container {
            max-height: 500px;
            overflow-y: auto;
        }
    </style>
</head>
<body class="bg-light">

<jsp:include page="/pages/_navbar.jsp" />

<div class="container mt-5">

    <h2 class="text-center mb-4">📡 Registered Devices</h2>

    <div class="table-scroll-container shadow-sm bg-white">
        <table class="table table-bordered table-hover align-middle">
            <thead class="table-primary text-center sticky-top">
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
    </div>

    <div class="card p-4 mt-4 shadow mb-5">
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

</div> <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>