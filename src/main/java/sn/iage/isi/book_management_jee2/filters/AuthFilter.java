package sn.iage.isi.book_management_jee2.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sn.iage.isi.book_management_jee2.models.User;
import sn.iage.isi.book_management_jee2.utils.SessionContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) // Filter les requests
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI().substring(request.getContextPath().length());

        // Racine → rediriger selon l'état de la session
        if (uri.equals("/") || uri.equals("/index.jsp")) {
            HttpSession s = request.getSession(false);
            if (s != null && s.getAttribute("currentUser") != null) {
                response.sendRedirect(request.getContextPath() + "/books");
            } else {
                response.sendRedirect(request.getContextPath() + "/auth/login");
            }
            return;
        }

        // Page de connexion : publique
        if (uri.startsWith("/auth/login")) {
            chain.doFilter(req, res);
            return;
        }

        // Vérification de la session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Contrôle d'accès par permission
        @SuppressWarnings("unchecked")
        Set<String> perms = (Set<String>) session.getAttribute("userPermissions");
        if (perms == null) perms = Collections.emptySet();

        if (uri.startsWith("/users") && !perms.contains("MANAGE_USERS")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Acces refuse : permission MANAGE_USERS requise.");
            return;
        }
        if (uri.startsWith("/roles") && !perms.contains("MANAGE_ROLES")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Acces refuse : permission MANAGE_ROLES requise.");
            return;
        }

        // Positionner le ThreadLocal pour l'audit (BaseEntity)
        SessionContext.setCurrentUser(user);
        try {
            chain.doFilter(req, res);
        } finally {
            SessionContext.clear();
        }
    }

    @Override public void init(FilterConfig fc) {}
    @Override public void destroy() {}
}