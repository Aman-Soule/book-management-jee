package sn.iage.isi.book_management_jee2.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import sn.iage.isi.book_management_jee2.config.JpaUtil;
import sn.iage.isi.book_management_jee2.models.Category;

import java.util.List;

public class CategoryRepository {

    public Category create(Category category) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Category c = Category.builder()
                    .name(category.getName())
                    .state(true)
                    .build();
            tx.begin();
            em.persist(c);
            tx.commit();
            return c;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Category> getAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createNamedQuery("Category.findAll", Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Category getById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Category category = em.find(Category.class, id);
            if (category == null)
                throw new EntityNotFoundException("Category not found: id=" + id);
            return category;
        } finally {
            em.close();
        }
    }

    public Category update(int id, Category newCategory) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Category cat = em.find(Category.class, id);
            if (cat == null)
                throw new EntityNotFoundException("Category not found: id=" + id);

            cat.setName(newCategory.getName());
            cat.setState(newCategory.isState());

            tx.begin();
            em.merge(cat);
            tx.commit();
            return cat;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Category c = em.find(Category.class, id);
            if (c == null)
                throw new EntityNotFoundException("Category not found: id=" + id);

            tx.begin();
            em.remove(c);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Category> search(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Category c WHERE LOWER(c.name) LIKE :kw ORDER BY c.name", Category.class)
                    .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public int countCategories() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(c.id) FROM Category c", Long.class)
                    .getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }

    public List<Category> searchActiveCategories() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Category c WHERE c.state = true ORDER BY c.name", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}