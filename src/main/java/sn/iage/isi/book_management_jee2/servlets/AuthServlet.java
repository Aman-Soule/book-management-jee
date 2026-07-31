package sn.iage.isi.book_management_jee2.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sn.iage.isi.book_management_jee2.models.User;
import sn.iage.isi.book_management_jee2.models.Permission;
import sn.iage.isi.book_management_jee2.repositories.UserRepository;
import sn.iage.isi.book_management_jee2.utils.Tools;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/login")) {
            showLogin(req, resp);
        } else if (path.equals("/logout")) {
            logout(req, resp);
        } else if (path.equals("/profile")) {
            req.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if ("/login".equals(path)) {
            login(req, resp);
        } else if ("/profile".equals(path)) {
            updateProfile(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/books");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    private void login(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password  = req.getParameter("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Identifiant et mot de passe requis.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        User user = userRepository.findByUsername(username.trim());
        if (user == null || !user.isActive()
                || !user.getPassword().equals(Tools.hashPassword(password))) {
            req.setAttribute("error", "Identifiant ou mot de passe incorrect.");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        Set<String> permNames = user.getRole() != null
                ? user.getRole().getPermissions().stream()
                .map(Permission::getName).collect(Collectors.toSet())
                : Collections.emptySet();

        HttpSession session = req.getSession(true);
        session.setAttribute("currentUser", user);
        session.setAttribute("userPermissions", permNames);

        resp.sendRedirect(req.getContextPath() + "/books");
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/auth/login");
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User sessionUser = (User) session.getAttribute("currentUser");

        User user = userRepository.findById(sessionUser.getId());
        if (user == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        user.setFirstName(emptyToNull(req.getParameter("firstName")));
        user.setLastName(emptyToNull(req.getParameter("lastName")));
        user.setEmail(emptyToNull(req.getParameter("email")));

        String currentPwd = req.getParameter("currentPassword");
        String newPwd     = req.getParameter("newPassword");
        String confirmPwd = req.getParameter("confirmPassword");

        if (newPwd != null && !newPwd.isBlank()) {
            if (currentPwd == null || !user.getPassword().equals(Tools.hashPassword(currentPwd))) {
                req.setAttribute("error", "Mot de passe actuel incorrect.");
                req.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(req, resp);
                return;
            }
            if (!newPwd.equals(confirmPwd)) {
                req.setAttribute("error", "Les nouveaux mots de passe ne correspondent pas.");
                req.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(req, resp);
                return;
            }
            if (newPwd.length() < 4) {
                req.setAttribute("error", "Le mot de passe doit contenir au moins 4 caracteres.");
                req.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(req, resp);
                return;
            }
            user.setPassword(Tools.hashPassword(newPwd));
        }

        userRepository.updateProfile(user);

        // Mettre à jour la session
        User refreshed = userRepository.findByUsername(user.getUsername());
        session.setAttribute("currentUser", refreshed);

        req.setAttribute("success", "Profil mis a jour avec succes.");
        req.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(req, resp);
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}

