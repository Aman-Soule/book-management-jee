<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/books">
            <i class="fas fa-book-open me-2"></i>Bibliotheque
        </a>
        <ul class="navbar-nav me-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/books">
                    <i class="fas fa-book me-1"></i>Livres
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/categories">
                    <i class="fas fa-tags me-1"></i>Categories
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/users">
                    <i class="fas fa-users me-1"></i>Utilisateurs
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/roles">
                    <i class="fas fa-shield-halved me-1"></i>Roles
                </a>
            </li>
        </ul>
        <ul class="navbar-nav">
            <li class="nav-item">
                <span class="nav-link text-white-50 small">
                    <i class="fas fa-user-circle me-1"></i>
                    <span class="badge bg-secondary ms-1">
                        Admin
                    </span>
                </span>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/auth/profile">
                    <i class="fas fa-id-card me-1"></i>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-danger" href="${pageContext.request.contextPath}/auth/logout">
                    <i class="fas fa-sign-out-alt me-1"></i>
                </a>
            </li>
        </ul>
    </div>
</nav>