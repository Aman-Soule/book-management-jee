package sn.iage.isi.book_management_jee2.repositories;

import jakarta.persistence.EntityManager;
import sn.iage.isi.book_management_jee2.config.JpaUtil;
import sn.iage.isi.book_management_jee2.models.Permission;
import sn.iage.isi.book_management_jee2.models.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleRepository {

    public List<Role> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.permissions ORDER BY r.name",
                            Role.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Role findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Role> result = em.createQuery(
                            "SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.id = :id",
                            Role.class)
                    .setParameter("id", id)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public void save(Role role, String[] permissionIds) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            role.setPermissions(loadPermissions(em, permissionIds));
            em.persist(role);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Role role, String[] permissionIds) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Role managed = em.find(Role.class, role.getId());
            managed.setName(role.getName());
            managed.setDescription(role.getDescription());
            managed.setPermissions(loadPermissions(em, permissionIds));
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
            Role role = em.find(Role.class, id);
            if (role != null) em.remove(role);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private Set<Permission> loadPermissions(EntityManager em, String[] ids) {
        Set<Permission> perms = new HashSet<>();
        if (ids != null) {
            for (String id : ids) {
                Permission p = em.find(Permission.class, Integer.parseInt(id));
                if (p != null) perms.add(p);
            }
        }
        return perms;
    }

}