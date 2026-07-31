package sn.iage.isi.book_management_jee2.repositories;

import jakarta.persistence.EntityManager;
import sn.iage.isi.book_management_jee2.config.JpaUtil;
import sn.iage.isi.book_management_jee2.models.Role;
import sn.iage.isi.book_management_jee2.models.User;

import java.util.List;

public class UserRepository {

    public List<User> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM User u LEFT JOIN FETCH u.role ORDER BY u.username",
                            User.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public User findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public User findByUsername(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<User> result = em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class)
                    .setParameter("username", username)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public void save(User user, int roleId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (roleId > 0) user.setRole(em.find(
                    Role.class, roleId));
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(User user, int roleId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User managed = em.find(User.class, user.getId());
            managed.setFirstName(user.getFirstName());
            managed.setLastName(user.getLastName());
            managed.setEmail(user.getEmail());
            managed.setActive(user.isActive());
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                managed.setPassword(user.getPassword());
            }
            if (roleId > 0) managed.setRole(em.find(
                    Role.class, roleId));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void updateProfile(User user) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User managed = em.find(User.class, user.getId());
            managed.setFirstName(user.getFirstName());
            managed.setLastName(user.getLastName());
            managed.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                managed.setPassword(user.getPassword());
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}