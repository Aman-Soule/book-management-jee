<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Livres - Bibliotheque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">
<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-book me-2 text-secondary"></i>Livres</h2>
        <a href="${pageContext.request.contextPath}/books/new" class="btn btn-primary">
            <i class="fas fa-plus me-1"></i>Nouveau livre
        </a>
    </div>

    <c:choose>
        <c:when test="${empty books}">
            <div class="text-center py-5">
                <i class="fas fa-book fa-4x text-muted mb-3"></i>
                <p class="text-muted fs-5">Aucun livre enregistre.</p>
                <a href="${pageContext.request.contextPath}/books/new" class="btn btn-primary">
                    <i class="fas fa-plus me-1"></i>Ajouter le premier livre
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th><i class="fas fa-barcode me-1"></i>ISBN</th>
                            <th><i class="fas fa-book me-1"></i>Titre</th>
                            <th><i class="fas fa-user-edit me-1"></i>Auteur</th>
                            <th class="text-center"><i class="fas fa-calendar me-1"></i>Annee</th>
                            <th class="text-center"><i class="fas fa-file me-1"></i>Pages</th>
                            <th><i class="fas fa-tag me-1"></i>Categorie</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="book" items="${books}">
                            <tr>
                                <td><c:out value="${book.isbn}"/></td>
                                <td class="fw-semibold"><c:out value="${book.title}"/></td>
                                <td><c:out value="${book.author}"/></td>
                                <td class="text-center"><c:out value="${book.publicationYear}"/></td>
                                <td class="text-center"><c:out value="${book.countPages}"/></td>
                                <td><c:out value="${book.category.name}"/></td>
                                <td class="text-center text-nowrap">
                                    <a href="${pageContext.request.contextPath}/books/${book.id}/edit"
                                       class="btn btn-sm btn-outline-secondary me-1">
                                        <i class="fas fa-pen"></i>
                                    </a>
                                    <form action="${pageContext.request.contextPath}/books/${book.id}/delete"
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Supprimer ce livre ?')">
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
                <i class="fas fa-info-circle me-1"></i>${books.size()} livre(s) au total
            </p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
