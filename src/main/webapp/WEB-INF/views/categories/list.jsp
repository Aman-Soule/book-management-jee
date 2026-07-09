<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Categories - Bibliotheque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">
<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-tags me-2 text-secondary"></i>Categories</h2>
        <a href="${pageContext.request.contextPath}/categories/new" class="btn btn-primary">
            <i class="fas fa-plus me-1"></i>Nouvelle categorie
        </a>
    </div>

    <c:choose>
        <c:when test="${empty categories}">
            <div class="text-center py-5">
                <i class="fas fa-tags fa-4x text-muted mb-3"></i>
                <p class="text-muted fs-5">Aucune categorie enregistree.</p>
                <a href="${pageContext.request.contextPath}/categories/new" class="btn btn-primary">
                    <i class="fas fa-plus me-1"></i>Ajouter la premiere categorie
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th><i class="fas fa-tag me-1"></i>Nom</th>
                            <th class="text-center"><i class="fas fa-circle-dot me-1"></i>Etat</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cat" items="${categories}">
                            <tr>
                                <td class="fw-semibold"><c:out value="${cat.name}"/></td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${cat.state}">
                                                <span class="badge bg-success">
                                                    <i class="fas fa-check me-1"></i>Active
                                                </span>
                                        </c:when>
                                        <c:otherwise>
                                                <span class="badge bg-secondary">
                                                    <i class="fas fa-ban me-1"></i>Inactive
                                                </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center text-nowrap">
                                    <a href="${pageContext.request.contextPath}/categories/${cat.id}/edit"
                                       class="btn btn-sm btn-outline-secondary me-1">
                                        <i class="fas fa-pen"></i>
                                    </a>
                                    <form action="${pageContext.request.contextPath}/categories/${cat.id}/delete"
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Supprimer cette categorie ?')">
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
            <p class="text-muted mt-2">
                <i class="fas fa-info-circle me-1"></i>${categories.size()} categorie(s) au total
            </p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>