package sn.iage.isi.book_management_jee2.servlets;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.iage.isi.book_management_jee2.models.Category;
import sn.iage.isi.book_management_jee2.repositories.CategoryRepository;

import java.io.IOException;
import java.util.List;

@WebServlet("/categories/*")
public class CategoryServlet extends HttpServlet {

    private final CategoryRepository repository = new CategoryRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        System.out.println("Path Info: " + pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            listCategories(req, resp);
        } else if (pathInfo.equals("/new")) {
            showForm(req, resp, new Category());
        } else {
            String[] parts = pathInfo.split("/");
            // /categories/{id}/edit
            if (parts.length == 3 && parts[2].equals("edit")) {
                int id = Integer.parseInt(parts[1]);
                try {
                    Category category = repository.getById(id);
                    showForm(req, resp, category);
                } catch (EntityNotFoundException e) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            createCategory(req, resp);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[1]);
                switch (parts[2]) {
                    case "update":
                        updateCategory(req, resp, id);
                        break;
                    case "delete":
                        try {
                            repository.delete(id);
                        } catch (EntityNotFoundException e) {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                            return;
                        }
                        resp.sendRedirect(req.getContextPath() + "/categories");
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    public void listCategories(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Category> listeCategories = repository.getAll();
        req.setAttribute("categories", listeCategories);
        req.getRequestDispatcher("/WEB-INF/views/categories/list.jsp")
                .forward(req, resp);
    }

    public void showForm(HttpServletRequest req, HttpServletResponse resp, Category category)
            throws ServletException, IOException {
        req.setAttribute("category", category);
        req.getRequestDispatcher("/WEB-INF/views/categories/form.jsp").forward(req, resp);
    }

    public void createCategory(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Category category = new Category();
        extractCategory(req, category);
        repository.create(category);
        resp.sendRedirect(req.getContextPath() + "/categories");
    }

    public void updateCategory(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {
        Category category = new Category();
        extractCategory(req, category);
        try {
            repository.update(id, category);
        } catch (EntityNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/categories");
    }

    private void extractCategory(HttpServletRequest req, Category category) {
        category.setName(req.getParameter("name"));
        category.setState("on".equals(req.getParameter("state")));
    }
}