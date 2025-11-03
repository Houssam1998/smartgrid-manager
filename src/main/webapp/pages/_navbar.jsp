<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Barre de navigation sticky Bootstrap 5 avec highlight de l'onglet actif automatiquement.
--%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/landing.jsp">
            <i class="fas fa-bolt"></i> SmartGrid
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNavbar">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/dashboard') ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/dashboard.jsp">
                        <i class="fas fa-home"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/stats') ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/stats">
                        <i class="fas fa-chart-line"></i> Statistiques
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/devices') ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/devices">
                        <i class="fas fa-microchip"></i> Appareils
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/readings') ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/readings">
                        <i class="fas fa-database"></i> Lectures
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/generator') ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/generator">
                        <i class="fas fa-magic"></i> Générateur
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/">
                        <i class="fas fa-sign-out-alt"></i> Quitter
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
