<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${category.id != 0}">Modifier</c:when>
            <c:otherwise>Nouvelle</c:otherwise>
        </c:choose> categorie
    </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">
<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container" style="max-width: 500px;">
    <div class="card shadow-sm">
        <div class="card-header">
            <h4 class="mb-0">
                <c:choose>
                    <c:when test="${category.id != 0}">
                        <i class="fas fa-pen me-2"></i>Modifier la categorie
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-plus-circle me-2"></i>Nouvelle categorie
                    </c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${category.id != 0}">
                    <c:url var="formAction" value="/categories/${category.id}/update"/>
                </c:when>
                <c:otherwise>
                    <c:url var="formAction" value="/categories"/>
                </c:otherwise>
            </c:choose>

            <form action="${formAction}" method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">
                        <i class="fas fa-tag me-1"></i>Nom <span class="text-danger">*</span>
                    </label>
                    <input type="text" class="form-control" id="name" name="name"
                           value="<c:out value='${category.name}'/>" required maxlength="100" autofocus>
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="state" name="state"
                           <c:if test="${category.state}">checked</c:if>>
                    <label class="form-check-label" for="state">
<%--                        <i class="fas fa-circle-dot me-1"></i>--%>
                        Categorie active
                    </label>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${category.id != 0}">
                                <i class="fas fa-save me-1"></i>Enregistrer
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-plus me-1"></i>Creer
                            </c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/categories" class="btn btn-outline-secondary">
                        <i class="fas fa-times me-1"></i>Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>