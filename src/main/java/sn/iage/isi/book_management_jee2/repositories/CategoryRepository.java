package sn.iage.isi.book_management_jee2.repositories;

import jakarta.persistence.EntityManager;
import sn.iage.isi.book_management_jee2.config.JpaUtil;
import sn.iage.isi.book_management_jee2.models.Category;

import java.util.List;

public class CategoryRepository {


    public List<Category> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        return em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class)
                .getResultList();
    }

    public Category findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Category.class, id);
    }

    public void save(Category category) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Category category) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(category);
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
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public List<Category> listActiveCategories() {
        EntityManager em = JpaUtil.getEntityManager();
        return em.createQuery(
                        "SELECT c FROM Category c WHERE c.state = true ORDER BY c.name",
                        Category.class)
                .getResultList();
    }
}
