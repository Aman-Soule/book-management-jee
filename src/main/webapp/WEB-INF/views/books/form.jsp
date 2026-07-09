<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${book.id != 0}">Modifier</c:when>
            <c:otherwise>Nouveau</c:otherwise>
        </c:choose> livre
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
                    <c:when test="${book.id != 0}">
                        <i class="fas fa-pen me-2"></i>Modifier le livre
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-plus-circle me-2"></i>Nouveau livre
                    </c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${book.id != 0}">
                    <c:url var="formAction" value="/books/${book.id}/update"/>
                </c:when>
                <c:otherwise>
                    <c:url var="formAction" value="/books"/>
                </c:otherwise>
            </c:choose>

            <form action="${formAction}" method="post">
                <div class="mb-3">
                    <label for="isbn" class="form-label">
                        <i class="fas fa-barcode me-1"></i>ISBN <span class="text-danger">*</span>
                    </label>
                    <input type="text" class="form-control" id="isbn" name="isbn"
                           value="<c:out value='${book.isbn}'/>" required maxlength="20" autofocus>
                </div>
                <div class="mb-3">
                    <label for="title" class="form-label">
                        <i class="fas fa-book me-1"></i>Titre <span class="text-danger">*</span>
                    </label>
                    <input type="text" class="form-control" id="title" name="title"
                           value="<c:out value='${book.title}'/>" required maxlength="200">
                </div>
                <div class="mb-3">
                    <label for="author" class="form-label">
                        <i class="fas fa-user-edit me-1"></i>Auteur <span class="text-danger">*</span>
                    </label>
                    <input type="text" class="form-control" id="author" name="author"
                           value="<c:out value='${book.author}'/>" required maxlength="150">
                </div>
                <div class="mb-3">
                    <label for="publicationYear" class="form-label">
                        <i class="fas fa-calendar me-1"></i>Annee de publication <span class="text-danger">*</span>
                    </label>
                    <input type="number" class="form-control" id="publicationYear" name="publicationYear"
                           value="<c:out value='${book.publicationYear}'/>" required>
                </div>
                <div class="mb-3">
                    <label for="countPages" class="form-label">
                        <i class="fas fa-file me-1"></i>Nombre de pages <span class="text-danger">*</span>
                    </label>
                    <input type="number" class="form-control" id="countPages" name="countPages"
                           value="<c:out value='${book.countPages}'/>" required>
                </div>
                <div class="mb-3">
                    <label for="categoryId" class="form-label">
                        <i class="fas fa-tag me-1"></i>Categorie <span class="text-danger">*</span>
                    </label>
                    <select class="form-select" id="categoryId" name="categoryId" required>
                        <option value="">-- Choisir une categorie --</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}" <c:if test="${book.category != null && book.category.id == cat.id}">selected</c:if>>
                                <c:out value="${cat.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${book.id != 0}">
                                <i class="fas fa-save me-1"></i>Enregistrer
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-plus me-1"></i>Creer
                            </c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/books" class="btn btn-outline-secondary">
                        <i class="fas fa-times me-1"></i>Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
