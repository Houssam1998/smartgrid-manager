<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bienvenue - SmartGrid Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body, html { height: 100%; margin: 0; }
        .hero-section {
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            height: 100vh;
            color: white;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            position: relative;
            overflow: hidden;
        }
        .hero-content {
            position: relative;
            z-index: 1;
            padding: 2rem;
            background: rgba(0, 0, 0, 0.2);
            border-radius: 20px;
            backdrop-filter: blur(5px);
        }
        .hero-content h1 { font-size: 4.5rem; font-weight: 700; }
        .hero-content p { font-size: 1.5rem; font-weight: 300; opacity: 0.9; margin: 1rem 0 2rem 0; }
        .btn-dashboard {
            font-size: 1.25rem;
            padding: 0.75rem 2.5rem;
            border-radius: 50px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-dashboard:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
    </style>
</head>
<body>
<div class="hero-section">
    <div class="hero-content">
        <h1><i class="fas fa-bolt"></i> SmartGrid Manager</h1>
        <p>Votre solution centralisée pour la gestion et l'analyse des données énergétiques.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-light btn-lg btn-dashboard">
            Accéder au Tableau de Bord <i class="fas fa-arrow-right ms-2"></i>
        </a>
    </div>
</div>
</body>
</html>