<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:choose><c:when test="${user.id != 0}">Modifier</c:when><c:otherwise>Nouvel</c:otherwise></c:choose> utilisateur</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">
<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container" style="max-width: 600px;">
    <div class="card shadow-sm">
        <div class="card-header">
            <h4 class="mb-0">
                <c:choose>
                    <c:when test="${user.id != 0}">
                        <i class="fas fa-pen me-2"></i>Modifier l'utilisateur
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-plus-circle me-2"></i>Nouvel utilisateur
                    </c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle me-1"></i><c:out value="${error}"/>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${user.id != 0}">
                    <c:url var="formAction" value="/users/${user.id}/update"/>
                </c:when>
                <c:otherwise>
                    <c:url var="formAction" value="/users"/>
                </c:otherwise>
            </c:choose>

            <form action="${formAction}" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="firstName" class="form-label">Prenom</label>
                        <input type="text" class="form-control" id="firstName" name="firstName"
                               value="<c:out value='${user.firstName}'/>" maxlength="50">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastName" class="form-label">Nom</label>
                        <input type="text" class="form-control" id="lastName" name="lastName"
                               value="<c:out value='${user.lastName}'/>" maxlength="50">
                    </div>
                </div>
                <div class="mb-3">
                    <label for="username" class="form-label">
                        <i class="fas fa-user me-1"></i>Identifiant <span class="text-danger">*</span>
                    </label>
                    <input type="text" class="form-control" id="username" name="username"
                           value="<c:out value='${user.username}'/>"
                           required maxlength="50" autofocus
                           <c:if test="${user.id != 0}">readonly class="form-control bg-light"</c:if>>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">
                        <i class="fas fa-envelope me-1"></i>Email
                    </label>
                    <input type="email" class="form-control" id="email" name="email"
                           value="<c:out value='${user.email}'/>" maxlength="100">
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">
                        <i class="fas fa-lock me-1"></i>Mot de passe
                        <c:choose>
                            <c:when test="${user.id == 0}">
                                <span class="text-danger">*</span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted fw-normal small">(laisser vide pour conserver)</span>
                            </c:otherwise>
                        </c:choose>
                    </label>
                    <input type="password" class="form-control" id="password" name="password"
                           minlength="4" <c:if test="${user.id == 0}">required</c:if>>
                </div>
                <div class="mb-3">
                    <label for="roleId" class="form-label">
                        <i class="fas fa-shield me-1"></i>Role <span class="text-danger">*</span>
                    </label>
                    <select class="form-select" id="roleId" name="roleId" required>
                        <option value="">-- Choisir un role --</option>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.id}"
                                    <c:if test="${user.role != null && user.role.id == role.id}">selected</c:if>>
                                <c:out value="${role.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="active" name="active"
                           <c:if test="${user.id == 0 || user.active}">checked</c:if>>
                    <label class="form-check-label" for="active">
                        <i class="fas fa-circle-dot me-1"></i>Compte actif
                    </label>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${user.id != 0}">
                                <i class="fas fa-save me-1"></i>Enregistrer
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-plus me-1"></i>Creer
                            </c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/users" class="btn btn-outline-secondary">
                        <i class="fas fa-times me-1"></i>Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>