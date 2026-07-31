<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:choose><c:when test="${role.id != 0}">Modifier</c:when><c:otherwise>Nouveau</c:otherwise></c:choose> role</title>
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
                    <c:when test="${role.id != 0}">
                        <i class="fas fa-pen me-2"></i>Modifier le role
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-plus-circle me-2"></i>Nouveau role
                    </c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${role.id != 0}">
                    <c:url var="formAction" value="/roles/${role.id}/update"/>
                </c:when>
                <c:otherwise>
                    <c:url var="formAction" value="/roles"/>
                </c:otherwise>
            </c:choose>

            <form action="${formAction}" method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">
                        <i class="fas fa-shield me-1"></i>Nom <span class="text-danger">*</span>
                    </label>
                    <input type="text" class="form-control" id="name" name="name"
                           value="<c:out value='${role.name}'/>" required maxlength="50" autofocus>
                </div>
                <div class="mb-3">
                    <label for="description" class="form-label">
                        <i class="fas fa-info-circle me-1"></i>Description
                    </label>
                    <input type="text" class="form-control" id="description" name="description"
                           value="<c:out value='${role.description}'/>" maxlength="255">
                </div>
                <div class="mb-3">
                    <label class="form-label">
                        <i class="fas fa-key me-1"></i>Permissions
                    </label>
                    <div class="border rounded p-3 bg-white">
                        <c:choose>
                            <c:when test="${empty allPermissions}">
                                <p class="text-muted mb-0 small">Aucune permission disponible.</p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="perm" items="${allPermissions}">
                                    <%-- Détecter si cette permission est assignée au role --%>
                                    <c:set var="checked" value="false"/>
                                    <c:forEach var="rp" items="${role.permissions}">
                                        <c:if test="${rp.id == perm.id}">
                                            <c:set var="checked" value="true"/>
                                        </c:if>
                                    </c:forEach>
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="checkbox"
                                               id="perm_${perm.id}" name="permissionIds"
                                               value="${perm.id}" ${checked ? 'checked' : ''}>
                                        <label class="form-check-label" for="perm_${perm.id}">
                                            <strong class="text-info">
                                                <c:out value="${perm.name}"/>
                                            </strong>
                                            <c:if test="${not empty perm.description}">
                                                <span class="text-muted small ms-1">
                                                    — <c:out value="${perm.description}"/>
                                                </span>
                                            </c:if>
                                        </label>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${role.id != 0}">
                                <i class="fas fa-save me-1"></i>Enregistrer
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-plus me-1"></i>Creer
                            </c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/roles" class="btn btn-outline-secondary">
                        <i class="fas fa-times me-1"></i>Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>