package sn.iage.isi.book_management_jee2;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import sn.iage.isi.book_management_jee2.repositories.CategoryRepository;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
//        message = "Hello World!";
        CategoryRepository cr = new CategoryRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}