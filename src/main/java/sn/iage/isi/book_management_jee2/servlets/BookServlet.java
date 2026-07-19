package sn.iage.isi.book_management_jee2.servlets;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.iage.isi.book_management_jee2.models.Book;
import sn.iage.isi.book_management_jee2.models.Category;
import sn.iage.isi.book_management_jee2.repositories.BookRepository;
import sn.iage.isi.book_management_jee2.repositories.CategoryRepository;
import sn.iage.isi.book_management_jee2.utils.Tools;

import java.io.IOException;
import java.util.List;

@WebServlet("/books/*")
public class BookServlet extends HttpServlet {

    private final BookRepository bookRepository = new BookRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        System.out.println("Path Info: " + pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            listBooks(req, resp);
        } else if (pathInfo.equals("/new")) {
            Book book = new Book();
            book.setIsbn(Tools.generateIsbn()); // pré-génération affichée dans le formulaire (Figure 4)
            showForm(req, resp, book);
        } else if (pathInfo.equals("/generate-isbn")) {
            generateIsbn(resp);
        } else {
            String[] parts = pathInfo.split("/");
            // /books/{id}/edit
            if (parts.length == 3 && parts[2].equals("edit")) {
                int id = Integer.parseInt(parts[1]);
                try {
                    Book book = bookRepository.findBookById(id);
                    showForm(req, resp, book);
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
            createBook(req, resp);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[1]);
                switch (parts[2]) {
                    case "update":
                        updateBook(req, resp, id);
                        break;
                    case "delete":
                        try {
                            bookRepository.deleteBook(id);
                        } catch (EntityNotFoundException e) {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                            return;
                        }
                        resp.sendRedirect(req.getContextPath() + "/books");
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    public void listBooks(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String title = req.getParameter("titre");
        String author = req.getParameter("auteur");
        String categoryIdParam = req.getParameter("categoryId");
        Integer categoryId = (categoryIdParam != null && !categoryIdParam.isBlank())
                ? Integer.parseInt(categoryIdParam)
                : null;

        String pageParam = req.getParameter("page");
        int page = 1;
        if (pageParam != null && !pageParam.isBlank()) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException ignored) {
                page = 1;
            }
        }

        List<Book> listeBooks = bookRepository.searchBooks(title, author, categoryId, page);
        long totalBooks = bookRepository.countSearchBooks(title, author, categoryId);
        int totalPages = (int) Math.max(1, Math.ceil(totalBooks / 5.0));

        req.setAttribute("books", listeBooks);
        req.setAttribute("categories", categoryRepository.searchActiveCategories());
        // On repasse les valeurs saisies pour que le formulaire de recherche les affiche après soumission
        req.setAttribute("titre", title);
        req.setAttribute("auteur", author);
        req.setAttribute("categoryId", categoryIdParam);
        // Pagination
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalBooks", totalBooks);

        req.getRequestDispatcher("/WEB-INF/views/books/list.jsp")
                .forward(req, resp);
    }

    public void showForm(HttpServletRequest req, HttpServletResponse resp, Book book)
            throws ServletException, IOException {
        req.setAttribute("book", book);
        // Seules les catégories actives doivent apparaître dans le formulaire (cf. énoncé 2.2)
        req.setAttribute("categories", categoryRepository.searchActiveCategories());
        req.getRequestDispatcher("/WEB-INF/views/books/form.jsp").forward(req, resp);
    }

    /**
     * GET /books/generate-isbn — appelé en AJAX (fetch) par le bouton "Régénérer".
     * Retourne l'ISBN en texte brut, sans rechargement de page.
     */
    public void generateIsbn(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write(Tools.generateIsbn());
    }

    public void createBook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Book book = new Book();
        extractBook(req, book);
        bookRepository.createBook(book);
        resp.sendRedirect(req.getContextPath() + "/books");
    }

    public void updateBook(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {
        Book book = new Book();
        extractBook(req, book);
        try {
            bookRepository.updateBook(id, book);
        } catch (EntityNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/books");
    }

    private void extractBook(HttpServletRequest req, Book book) {
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setIsbn(req.getParameter("isbn"));

        String yearParam = req.getParameter("publicationYear");
        if (yearParam != null && !yearParam.isBlank()) {
            book.setPublicationYear(Integer.parseInt(yearParam));
        }

        String pagesParam = req.getParameter("countPages");
        if (pagesParam != null && !pagesParam.isBlank()) {
            book.setCountPages(Integer.parseInt(pagesParam));
        }

        String categoryIdParam = req.getParameter("categoryId");
        if (categoryIdParam != null && !categoryIdParam.isBlank()) {
            Category category = new Category();
            category.setId(Integer.parseInt(categoryIdParam));
            book.setCategory(category);
        }
    }
}