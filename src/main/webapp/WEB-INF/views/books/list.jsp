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
        <h2><i class="fas fa-book me-2 text-secondary"></i>Livre</h2>
        <a href="${pageContext.request.contextPath}/books/new" class="btn btn-primary">
            <i class="fas fa-plus me-1"></i>Nouveau livre
        </a>
    </div>

    <!-- Barre de recherche multi-critères -->
    <div class="card shadow-sm mb-3">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/books" method="get" class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label for="titre" class="form-label">
                        <i class="fas fa-heading me-1"></i>Titre
                    </label>
                    <input type="text" class="form-control" id="titre" name="titre"
                           placeholder="Rechercher par titre..." value="<c:out value='${titre}'/>">
                </div>
                <div class="col-md-4">
                    <label for="auteur" class="form-label">
                        <i class="fas fa-user-pen me-1"></i>Auteur
                    </label>
                    <input type="text" class="form-control" id="auteur" name="auteur"
                           placeholder="Rechercher par auteur..." value="<c:out value='${auteur}'/>">
                </div>
                <div class="col-md-3">
                    <label for="categoryId" class="form-label">
                        <i class="fas fa-tag me-1"></i>Categorie
                    </label>
                    <select class="form-select" id="categoryId" name="categoryId">
                        <option value="">-- Toutes --</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}" <c:if test="${categoryId == cat.id}">selected</c:if>>
                                <c:out value="${cat.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-1 d-flex gap-1">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search"></i>
                    </button>

                    <c:if test="${not empty titre or not empty auteur or not empty categoryId}">
                        <a href="${pageContext.request.contextPath}/books" class="btn btn-sm btn-outline-danger" title="Effacer">
                            <i class="fas fa-times"></i>
                        </a>
                    </c:if>
                </div>

            </form>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty books}">
            <div class="text-center py-5">
                <i class="fas fa-book fa-4x text-muted mb-3"></i>
                <p class="text-muted fs-5">Aucun livre trouve.</p>
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
                            <th><i class="fas fa-heading me-1"></i>Titre</th>
                            <th><i class="fas fa-user-pen me-1"></i>Auteur</th>
                            <th><i class="fas fa-calendar-alt me-1"></i>Annee</th>
                            <th><i class="fas fa-file-lines me-1"></i>Pages</th>
                            <th><i class="fas fa-tag me-1"></i>Categorie</th>
                            <th class="text-center"><i class="fas fa-gears me-1"></i>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="liv" items="${books}">
                            <tr>
                                <td><c:out value="${liv.isbn}"/></td>
                                <td class="fw-semibold"><c:out value="${liv.title}"/></td>
                                <td><c:out value="${liv.author}"/></td>
                                <td class="text-center"><c:out value="${liv.publicationYear}"/></td>
                                <td class="text-center"><c:out value="${liv.countPages}"/></td>
                                <td><c:out value="${liv.category.name}"/></td>
                                <td class="text-center text-nowrap">
                                    <a href="${pageContext.request.contextPath}/books/${liv.id}/edit"
                                       class="btn btn-sm btn-outline-secondary me-1">
                                        <i class="fas fa-pen"></i>
                                    </a>
                                    <form action="${pageContext.request.contextPath}/books/${liv.id}/delete"
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
            <div class="d-flex justify-content-between align-items-center mt-2">
                <p class="text-muted mb-0">
                    <i class="fas fa-info-circle me-1"></i>
                    <c:choose>
                        <c:when test="${not empty titre or not empty auteur or not empty categoryId}">
                            ${totalBooks} livre(s) trouvé(s)
                        </c:when>
                        <c:otherwise>
                            ${totalBooks} livre(s) — page ${currentPage} / ${totalPages}
                        </c:otherwise>
                    </c:choose>
                </p>

                <c:if test="${totalPages > 1}">
                    <nav>
                        <ul class="pagination mb-0">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="?page=${currentPage - 1}">
                                    <i class="fas fa-chevron-left"></i>
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="p">
                                <li class="page-item ${p == currentPage ? 'active' : ''}">
                                    <a class="page-link"
                                       href="?page=${p}">
                                            ${p}
                                    </a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="?page=${currentPage + 1}">
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>