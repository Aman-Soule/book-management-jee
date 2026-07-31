package sn.iage.isi.book_management_jee2.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.iage.isi.book_management_jee2.models.User;
import sn.iage.isi.book_management_jee2.repositories.RoleRepository;
import sn.iage.isi.book_management_jee2.repositories.UserRepository;
import sn.iage.isi.book_management_jee2.utils.Tools;

import java.io.IOException;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepository();
    private final RoleRepository roleRepository  = new RoleRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            listUsers(req, resp);
        } else if (path.equals("/new")) {
            showForm(req, resp, new User());
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3 && parts[2].equals("edit")) {
                User user = userRepository.findById(Integer.parseInt(parts[1]));
                if (user != null) showForm(req, resp, user);
                else resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            createUser(req, resp);
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[1]);
                switch (parts[2]) {
                    case "update": updateUser(req, resp, id); break;
                    case "delete":
                        userRepository.delete(id);
                        resp.sendRedirect(req.getContextPath() + "/users");
                        break;
                    default: resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("users", userRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/views/users/list.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.setAttribute("user",  user);
        req.setAttribute("roles", roleRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/views/users/form.jsp").forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Identifiant et mot de passe requis.");
            showForm(req, resp, new User());
            return;
        }
        if (userRepository.findByUsername(username.trim()) != null) {
            req.setAttribute("error", "Cet identifiant est deja utilise.");
            showForm(req, resp, new User());
            return;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(Tools.hashPassword(password));
        user.setFirstName(req.getParameter("firstName"));
        user.setLastName(req.getParameter("lastName"));
        user.setEmail(req.getParameter("email"));
        user.setActive("on".equals(req.getParameter("active")));

        int roleId = parseId(req.getParameter("roleId"));
        userRepository.save(user, roleId);
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {
        User user = new User();
        user.setId(id);
        user.setFirstName(req.getParameter("firstName"));
        user.setLastName(req.getParameter("lastName"));
        user.setEmail(req.getParameter("email"));
        user.setActive("on".equals(req.getParameter("active")));

        String pwd = req.getParameter("password");
        if (pwd != null && !pwd.isBlank()) {
            user.setPassword(Tools.hashPassword(pwd));
        }

        int roleId = parseId(req.getParameter("roleId"));
        userRepository.update(user, roleId);
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    private int parseId(String s) {
        try { return (s != null && !s.isBlank()) ? Integer.parseInt(s) : 0; }
        catch (NumberFormatException e) { return 0; }
    }
}