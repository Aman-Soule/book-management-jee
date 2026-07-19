package sn.iage.isi.book_management_jee2.repositories;

import jakarta.persistence.EntityManager;
import sn.iage.isi.book_management_jee2.config.JpaUtil;
import sn.iage.isi.book_management_jee2.models.Book;

import java.util.List;
import java.util.Random;

public class BookRepository {
    public String generateIsbn() {
        // Préfixe ISBN-13 : 978 ou 979
        String[] prefixes = {"978", "979"};
        Random random = new Random();

        String prefix = prefixes[random.nextInt(2)];        // 978 ou 979
        String group = String.valueOf(random.nextInt(2));    // 0 ou 1 (groupe langue)
        String publisher = String.format("%04d", random.nextInt(10000));   // éditeur 4 chiffres
        String title    = String.format("%04d", random.nextInt(10000));    // titre   4 chiffres

        // Calcul du chiffre de contrôle (checksum ISBN-13)
        String base = prefix + group + publisher + title;   // 12 chiffres
        int checkDigit = computeIsbn13CheckDigit(base);

        String isbn = base + checkDigit;

        // Format lisible : 978-X-XXXX-XXXX-X
        return String.format("%s-%s-%s-%s-%d",
                prefix, group, publisher, title, checkDigit);
    }

    public int computeIsbn13CheckDigit(String base12) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(base12.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;   // alternance poids 1 et 3
        }
        int remainder = sum % 10;
        return remainder == 0 ? 0 : 10 - remainder;
    }

    public List<Book> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        return em.createQuery("SELECT b FROM Book b ORDER BY b.title", Book.class)
                .getResultList();
    }

    public Book findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Book.class, id);
    }

    public void save(Book book){
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (book.getIsbn() == null || book.getIsbn().isBlank()) {
                book.setIsbn(generateIsbn());
            }
            em.persist(book);
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }  finally {
            em.close();
        }
    }

    public void update(Book book) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, id);
            if (book != null) {
                em.remove(book);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
