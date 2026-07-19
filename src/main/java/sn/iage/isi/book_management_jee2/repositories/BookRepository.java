package sn.iage.isi.book_management_jee2.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import sn.iage.isi.book_management_jee2.config.JpaUtil;
import sn.iage.isi.book_management_jee2.models.Book;
import sn.iage.isi.book_management_jee2.utils.Tools;

import java.util.List;

public class BookRepository {

    public Book createBook(Book book) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            // L'ISBN est normalement déjà généré côté formulaire (pré-rempli ou via /books/generate-isbn).
            // On ne le régénère ici que s'il est manquant, en filet de sécurité.
            String isbn = (book.getIsbn() == null || book.getIsbn().isBlank())
                    ? Tools.generateIsbn()
                    : book.getIsbn();

            Book b = Book.builder()
                    .isbn(isbn)
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .publicationYear(book.getPublicationYear())
                    .countPages(book.getCountPages())
                    .category(book.getCategory())
                    .build();

            tx.begin();
            em.persist(b);
            tx.commit();
            return b;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Book> listAllBooks() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT b FROM Book b ORDER BY b.title ASC", Book.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Book findBookById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Book book = em.find(Book.class, id);
            if (book == null)
                throw new EntityNotFoundException("Book not found: id=" + id);
            return book;
        } finally {
            em.close();
        }
    }

    public Book findBookByIsbn(String isbn) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class)
                    .setParameter("isbn", isbn)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Book not found: isbn=" + isbn));
        } finally {
            em.close();
        }
    }

    public Book updateBook(int id, Book newBook) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Book book = em.find(Book.class, id);
            if (book == null)
                throw new EntityNotFoundException("Book not found: id=" + id);

            book.setTitle(newBook.getTitle());
            book.setAuthor(newBook.getAuthor());
            book.setPublicationYear(newBook.getPublicationYear());
            book.setCountPages(newBook.getCountPages());
            book.setCategory(newBook.getCategory());
            // ISBN volontairement non modifié : c'est l'identité métier du livre

            tx.begin();
            em.merge(book);
            tx.commit();
            return book;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteBook(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Book b = em.find(Book.class, id);
            if (b == null)
                throw new EntityNotFoundException("Book not found: id=" + id);

            tx.begin();
            em.remove(b);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    private static final int PAGE_SIZE = 5;

    /**
     * Recherche multi-critères combinée, paginée (10 résultats par page).
     * @param page numéro de page, commence à 1
     */
//    public List<Book> searchBooks(String title, String author, Integer categoryId, int page) {
//        EntityManager em = JpaUtil.getEntityManager();
//        try {
//            StringBuilder jpql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");
//            appendCriteria(jpql, title, author, categoryId);
//            jpql.append(" ORDER BY b.title ASC");
//
//            var query = em.createQuery(jpql.toString(), Book.class);
//            bindCriteria(query, title, author, categoryId);
//
//            int safePage = Math.max(page, 1);
//            query.setFirstResult((safePage - 1) * PAGE_SIZE);
//            query.setMaxResults(PAGE_SIZE);
//
//            return query.getResultList();
//        } finally {
//            em.close();
//        }
//    }

    public List<Book> searchBooks(String title, String author, Integer categoryId, int page) {
        EntityManager em = JpaUtil.getEntityManager();

        StringBuilder jpql = new StringBuilder("SELECT b FROM Book b JOIN FETCH b.category WHERE 1=1");
        if (title != null && !title.isBlank())  jpql.append(" AND LOWER(b.title) LIKE :title");
        if (author != null && !author.isBlank()) jpql.append(" AND LOWER(b.author) LIKE :author");
        if (categoryId != null)                 jpql.append(" AND b.category.id = :categoryId");
        jpql.append(" ORDER BY b.title");

        var query = em.createQuery(jpql.toString(), Book.class);
        if (title != null && !title.isBlank())  query.setParameter("title",  "%" + title.toLowerCase()  + "%");
        if (author != null && !author.isBlank()) query.setParameter("author", "%" + author.toLowerCase() + "%");
        if (categoryId != null)                 query.setParameter("categoryId", categoryId);

        int safePage = Math.max(page, 1);
        query.setFirstResult((safePage - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    /**
     * Nombre total de résultats pour les mêmes critères (nécessaire pour calculer le nombre de pages).
     */
    public long countSearchBooks(String title, String author, Integer categoryId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");
            appendCriteria(jpql, title, author, categoryId);

            var query = em.createQuery(jpql.toString(), Long.class);
            bindCriteria(query, title, author, categoryId);

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    private void appendCriteria(StringBuilder jpql, String title, String author, Integer categoryId) {
        if (title != null && !title.isBlank()) {
            jpql.append(" AND LOWER(b.title) LIKE :title");
        }
        if (author != null && !author.isBlank()) {
            jpql.append(" AND LOWER(b.author) LIKE :author");
        }
        if (categoryId != null) {
            jpql.append(" AND b.category.id = :categoryId");
        }
    }

    private void bindCriteria(jakarta.persistence.Query query, String title, String author, Integer categoryId) {
        if (title != null && !title.isBlank()) {
            query.setParameter("title", "%" + title.toLowerCase() + "%");
        }
        if (author != null && !author.isBlank()) {
            query.setParameter("author", "%" + author.toLowerCase() + "%");
        }
        if (categoryId != null) {
            query.setParameter("categoryId", categoryId);
        }
    }

    public List<Book> listBooksByCategory(String categoryName) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT b FROM Book b WHERE b.category.name = :name ORDER BY b.title", Book.class)
                    .setParameter("name", categoryName)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Book> searchBooksByTitle(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT b FROM Book b WHERE LOWER(b.title) LIKE :kw ORDER BY b.title", Book.class)
                    .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Book> searchBooksByAuthor(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT b FROM Book b WHERE LOWER(b.author) LIKE :kw ORDER BY b.author", Book.class)
                    .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Book> searchBooksAfterYear(int year) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT b FROM Book b WHERE b.publicationYear > :year ORDER BY b.publicationYear", Book.class)
                    .setParameter("year", year)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object[]> countBooksByCategory() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT b.category.name, COUNT(b) FROM Book b GROUP BY b.category.name", Object[].class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public int countAllBooks() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(b.id) FROM Book b", Long.class)
                    .getSingleResult()
                    .intValue();
        } finally {
            em.close();
        }
    }
}