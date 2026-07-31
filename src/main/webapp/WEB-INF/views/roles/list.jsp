<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Roles - Bibliotheque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">
<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-shield-halved me-2 text-secondary"></i>Roles</h2>
        <a href="${pageContext.request.contextPath}/roles/new" class="btn btn-primary">
            <i class="fas fa-plus me-1"></i>Nouveau role
        </a>
    </div>

    <c:choose>
        <c:when test="${empty roles}">
            <div class="text-center py-5">
                <i class="fas fa-shield fa-4x text-muted mb-3 d-block"></i>
                <p class="text-muted fs-5">Aucun role enregistre.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th><i class="fas fa-shield me-1"></i>Nom</th>
                            <th><i class="fas fa-info-circle me-1"></i>Description</th>
                            <th><i class="fas fa-key me-1"></i>Permissions</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="role" items="${roles}">
                            <tr>
                                <td class="fw-semibold"><c:out value="${role.name}"/></td>
                                <td class="text-muted"><c:out value="${role.description}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty role.permissions}">
                                            <span class="text-muted small">Aucune</span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="perm" items="${role.permissions}">
                                                    <span class="badge bg-info text-dark me-1 mb-1">
                                                        <c:out value="${perm.name}"/>
                                                    </span>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center text-nowrap">
                                    <a href="${pageContext.request.contextPath}/roles/${role.id}/edit"
                                       class="btn btn-sm btn-outline-secondary me-1">
                                        <i class="fas fa-pen"></i>
                                    </a>
                                    <form action="${pageContext.request.contextPath}/roles/${role.id}/delete"
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Supprimer ce role ?')">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <p class="text-muted mt-2 small">
                <i class="fas fa-info-circle me-1"></i>${roles.size()} role(s)
            </p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>