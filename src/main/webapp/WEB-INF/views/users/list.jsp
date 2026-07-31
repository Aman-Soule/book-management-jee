<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Utilisateurs - Bibliotheque</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">
<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="fas fa-users me-2 text-secondary"></i>Utilisateurs</h2>
    <a href="${pageContext.request.contextPath}/users/new" class="btn btn-primary">
      <i class="fas fa-plus me-1"></i>Nouvel utilisateur
    </a>
  </div>

  <c:choose>
    <c:when test="${empty users}">
      <div class="text-center py-5">
        <i class="fas fa-users fa-4x text-muted mb-3 d-block"></i>
        <p class="text-muted fs-5">Aucun utilisateur enregistre.</p>
      </div>
    </c:when>
    <c:otherwise>
      <div class="card shadow-sm">
        <div class="card-body p-0">
          <table class="table table-hover mb-0">
            <thead class="table-dark">
            <tr>
              <th><i class="fas fa-user me-1"></i>Identifiant</th>
              <th><i class="fas fa-id-card me-1"></i>Nom complet</th>
              <th><i class="fas fa-envelope me-1"></i>Email</th>
              <th><i class="fas fa-shield me-1"></i>Role</th>
              <th class="text-center"><i class="fas fa-circle-dot me-1"></i>Etat</th>
              <th class="text-center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${users}">
              <tr>
                <td class="fw-semibold">
                  <c:out value="${u.username}"/>
                  <c:if test="${u.id == sessionScope.currentUser.id}">
                    <span class="badge bg-primary ms-1">Moi</span>
                  </c:if>
                </td>
                <td><c:out value="${u.firstName} ${u.lastName}"/></td>
                <td><c:out value="${u.email}"/></td>
                <td>
                                        <span class="badge bg-dark">
                                            <c:out value="${u.role != null ? u.role.name : 'Aucun'}"/>
                                        </span>
                </td>
                <td class="text-center">
                  <c:choose>
                    <c:when test="${u.active}">
                                                <span class="badge bg-success">
                                                    <i class="fas fa-check me-1"></i>Actif
                                                </span>
                    </c:when>
                    <c:otherwise>
                                                <span class="badge bg-secondary">
                                                    <i class="fas fa-ban me-1"></i>Inactif
                                                </span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td class="text-center text-nowrap">
                  <a href="${pageContext.request.contextPath}/users/${u.id}/edit"
                     class="btn btn-sm btn-outline-secondary me-1">
                    <i class="fas fa-pen"></i>
                  </a>
                  <c:if test="${u.id != sessionScope.currentUser.id}">
                    <form action="${pageContext.request.contextPath}/users/${u.id}/delete"
                          method="post" class="d-inline"
                          onsubmit="return confirm('Supprimer cet utilisateur ?')">
                      <button type="submit" class="btn btn-sm btn-outline-danger">
                        <i class="fas fa-trash"></i>
                      </button>
                    </form>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
      <p class="text-muted mt-2 small">
        <i class="fas fa-info-circle me-1"></i>${users.size()} utilisateur(s)
      </p>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>