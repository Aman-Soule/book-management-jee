<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Connexion - Bibliotheque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        body { background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%); min-height: 100vh; }
    </style>
</head>
<body class="d-flex align-items-center justify-content-center">
<div class="container" style="max-width: 420px;">
    <div class="text-center mb-4">
        <i class="fas fa-book-open fa-3x text-white"></i>
        <h3 class="mt-2 fw-bold text-white">Bibliotheque</h3>
        <p class="text-white-50">Systeme de gestion</p>
    </div>

    <div class="card shadow-lg border-0">
        <div class="card-body p-4">
            <h5 class="card-title mb-4 text-center">
                <i class="fas fa-sign-in-alt me-2"></i>Connexion
            </h5>

            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    <c:out value="${error}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth/login" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label fw-semibold">
                        <i class="fas fa-user me-1"></i>Identifiant
                    </label>
                    <input type="text" class="form-control form-control-lg" id="username"
                           name="username" value="<c:out value='${username}'/>"
                           required autofocus placeholder="Votre identifiant">
                </div>
                <div class="mb-4">
                    <label for="password" class="form-label fw-semibold">
                        <i class="fas fa-lock me-1"></i>Mot de passe
                    </label>
                    <input type="password" class="form-control form-control-lg" id="password"
                           name="password" required placeholder="Votre mot de passe">
                </div>
                <button type="submit" class="btn btn-dark btn-lg w-100">
                    <i class="fas fa-sign-in-alt me-2"></i>Se connecter
                </button>
            </form>
        </div>
    </div>

    <div class="text-center mt-3">
        <small class="text-white-50">
            Compte par defaut : <strong class="text-white">admin</strong> /
            <strong class="text-white">admin123</strong>
        </small>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>