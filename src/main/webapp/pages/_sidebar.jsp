<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Ce fichier est un "partiel". Il est destiné à être inclus par d'autres pages.
  Il utilise les classes CSS de 'layout.css' et de Bootstrap.
--%>
<nav class="sidebar">
    <a class="sidebar-header" href="${pageContext.request.contextPath}/home">
        <i class="fas fa-bolt"></i>
        <span>SmartGrid</span>
    </a>

    <ul class="nav nav-pills flex-column">
        <li class="nav-item">
            <a class="nav-link ${pageContext.request.servletPath.endsWith('/home') ? 'active' : ''}"
               href="${pageContext.request.contextPath}/home">
                <i class="fas fa-home"></i> Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${pageContext.request.servletPath.endsWith('/stats') ? 'active' : ''}"
               href="${pageContext.request.contextPath}/stats">
                <i class="fas fa-chart-line"></i> Statistiques
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${pageContext.request.servletPath.endsWith('/devices') ? 'active' : ''}"
               href="${pageContext.request.contextPath}/devices">
                <i class="fas fa-microchip"></i> Appareils (Devices)
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${pageContext.request.servletPath.endsWith('/readings') ? 'active' : ''}"
               href="${pageContext.request.contextPath}/readings">
                <i class="fas fa-database"></i> Lectures (Readings)
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${pageContext.request.servletPath.endsWith('/generator') ? 'active' : ''}"
               href="${pageContext.request.contextPath}/generator">
                <i class="fas fa-magic"></i> Générateur
            </a>
        </li>
    </ul>

    <hr style="color: #6c757d;">

    <div class="text-center text-muted small">
        <a href="${pageContext.request.contextPath}/"
           class="nav-link" style="color: #6c757d; font-size: 0.9rem;">
            <i class="fas fa-sign-out-alt"></i> Quitter (Landing Page)
        </a>
    </div>
</nav>