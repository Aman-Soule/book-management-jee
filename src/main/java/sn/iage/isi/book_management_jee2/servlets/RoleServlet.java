package sn.iage.isi.book_management_jee2.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.iage.isi.book_management_jee2.models.Role;
import sn.iage.isi.book_management_jee2.repositories.PermissionRepository;
import sn.iage.isi.book_management_jee2.repositories.RoleRepository;

import java.io.IOException;

@WebServlet("/roles/*")
public class RoleServlet extends HttpServlet {

    private final RoleRepository roleRepository       = new RoleRepository();
    private final PermissionRepository permissionRepository = new PermissionRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            listRoles(req, resp);
        } else if (path.equals("/new")) {
            showForm(req, resp, new Role());
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3 && parts[2].equals("edit")) {
                Role role = roleRepository.findById(Integer.parseInt(parts[1]));
                if (role != null) showForm(req, resp, role);
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
            createRole(req, resp);
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[1]);
                switch (parts[2]) {
                    case "update": updateRole(req, resp, id); break;
                    case "delete":
                        roleRepository.delete(id);
                        resp.sendRedirect(req.getContextPath() + "/roles");
                        break;
                    default: resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private void listRoles(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("roles", roleRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/views/roles/list.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Role role)
            throws ServletException, IOException {
        req.setAttribute("role",           role);
        req.setAttribute("allPermissions", permissionRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/views/roles/form.jsp").forward(req, resp);
    }

    private void createRole(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Role role = new Role();
        role.setName(req.getParameter("name"));
        role.setDescription(req.getParameter("description"));
        roleRepository.save(role, req.getParameterValues("permissionIds"));
        resp.sendRedirect(req.getContextPath() + "/roles");
    }

    private void updateRole(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {
        Role role = roleRepository.findById(id);
        if (role == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        role.setName(req.getParameter("name"));
        role.setDescription(req.getParameter("description"));
        roleRepository.update(role, req.getParameterValues("permissionIds"));
        resp.sendRedirect(req.getContextPath() + "/roles");
    }
}