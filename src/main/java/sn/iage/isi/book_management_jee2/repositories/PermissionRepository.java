package sn.iage.isi.book_management_jee2.repositories;

import jakarta.persistence.EntityManager;
import sn.iage.isi.book_management_jee2.config.JpaUtil;
import sn.iage.isi.book_management_jee2.models.Permission;

import java.util.List;

public class PermissionRepository {

    public List<Permission> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Permission p ORDER BY p.name",
                            Permission.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Permission findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Permission.class, id);
        } finally {
            em.close();
        }
    }

}