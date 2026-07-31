<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mon profil - Bibliotheque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">
<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <h2 class="mb-4"><i class="fas fa-id-card me-2 text-secondary"></i>Mon profil</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><i class="fas fa-exclamation-circle me-1"></i><c:out value="${error}"/></div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success"><i class="fas fa-check-circle me-1"></i><c:out value="${success}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/auth/profile" method="post">
        <div class="row g-3 align-items-start">

            <!-- Colonne gauche : informations personnelles -->
            <div class="col-md-6">
                <div class="card shadow-sm h-100">
                    <div class="card-header fw-semibold">
                        <i class="fas fa-user me-2"></i>Informations personnelles
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label text-muted small">Identifiant</label>
                            <input type="text" class="form-control"
                                   value="<c:out value='${sessionScope.currentUser.username}'/>" disabled>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-muted small">Role</label>
                            <input type="text" class="form-control"
                                   value="<c:out value='${sessionScope.currentUser.role.name}'/>" disabled>
                        </div>
                        <div class="row">
                            <div class="col-6 mb-3">
                                <label for="firstName" class="form-label">Prenom</label>
                                <input type="text" class="form-control" id="firstName" name="firstName"
                                       value="<c:out value='${sessionScope.currentUser.firstName}'/>" maxlength="50">
                            </div>
                            <div class="col-6 mb-3">
                                <label for="lastName" class="form-label">Nom</label>
                                <input type="text" class="form-control" id="lastName" name="lastName"
                                       value="<c:out value='${sessionScope.currentUser.lastName}'/>" maxlength="50">
                            </div>
                        </div>
                        <div class="mb-0">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<c:out value='${sessionScope.currentUser.email}'/>" maxlength="100">
                        </div>
                    </div>
                </div>
            </div>

            <!-- Colonne droite : mot de passe + permissions -->
            <div class="col-md-6 d-flex flex-column gap-3">

                <div class="card shadow-sm">
                    <div class="card-header fw-semibold">
                        <i class="fas fa-lock me-2"></i>Changer le mot de passe
                        <span class="text-muted fw-normal small ms-1">(optionnel)</span>
                    </div>
                    <div class="mb-3">
                        <label for="currentPassword" class="form-label">Mot de passe actuel</label>
                        <input type="password" class="form-control" id="currentPassword" name="currentPassword">
                    </div>
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">Nouveau mot de passe</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" minlength="4">
                    </div>
                    <div class="mb-0">
                        <label for="confirmPassword" class="form-label">Confirmer le mot de passe</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
                    </div>
                </div>
            </div>

            <c:if test="${not empty sessionScope.userPermissions}">
                <div class="card shadow-sm">
                    <div class="card-header fw-semibold">
                        <i class="fas fa-key me-2"></i>Mes permissions
                    </div>
                    <div class="card-body">
                        <c:forEach var="perm" items="${sessionScope.userPermissions}">
                            <span class="badge bg-info text-dark me-1 mb-1">
                                <i class="fas fa-check me-1"></i><c:out value="${perm}"/>
                            </span>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

        </div>

        <div class="d-flex gap-2 mt-3">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save me-1"></i>Enregistrer
            </button>
            <a href="${pageContext.request.contextPath}/books" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-1"></i>Retour
            </a>
        </div>
    </form>
</div>
</body>
</html>