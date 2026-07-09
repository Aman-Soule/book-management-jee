package sn.iage.isi.book_management_jee2.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.iage.isi.book_management_jee2.models.Book;
import sn.iage.isi.book_management_jee2.models.Category;
import sn.iage.isi.book_management_jee2.repositories.BookRepository;
import sn.iage.isi.book_management_jee2.repositories.CategoryRepository;

import java.io.IOException;
import java.util.List;

@WebServlet("/books/*")
public class BookServlet extends HttpServlet {

    private final BookRepository repository = new BookRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        System.out.println("Path Info: " + pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            listBooks(req, resp);
        } else if (pathInfo.equals("/new")) {
            showForm(req, resp, new Book());
        } else {
            String[] parts = pathInfo.split("/");
            // /books/{id}/edit
            if (parts.length == 3 && parts[2].equals("edit")) {
                int id = Integer.parseInt(parts[1]);
                Book book = repository.findById(id);
                if (book != null) {
                    showForm(req, resp, book);
                } else {
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
                        repository.delete(id);
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
        List<Book> listeBooks = repository.findAll();
        req.setAttribute("books", listeBooks);
        req.getRequestDispatcher("/WEB-INF/views/books/list.jsp")
                .forward(req, resp);
    }

    public void showForm(HttpServletRequest req, HttpServletResponse resp, Book book)
            throws ServletException, IOException {
        List<Category> categories = categoryRepository.listActiveCategories();
        req.setAttribute("book", book);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/books/form.jsp").forward(req, resp);
    }

    public void createBook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Book book = new Book();
        extractBook(req, book);
        repository.save(book);
        resp.sendRedirect(req.getContextPath() + "/books");
    }

    public void updateBook(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {
        Book book = repository.findById(id);
        if (book == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        extractBook(req, book);
        repository.update(book);
        resp.sendRedirect(req.getContextPath() + "/books");
    }

    private void extractBook(HttpServletRequest req, Book book) {
        book.setIsbn(req.getParameter("isbn"));
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setPublicationYear(Integer.parseInt(req.getParameter("publicationYear")));
        book.setCountPages(Integer.parseInt(req.getParameter("countPages")));
        int categoryId = Integer.parseInt(req.getParameter("categoryId"));
        book.setCategory(categoryRepository.findById(categoryId));
    }
}
