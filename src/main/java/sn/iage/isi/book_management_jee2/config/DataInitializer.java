package sn.iage.isi.book_management_jee2.config;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import sn.iage.isi.book_management_jee2.models.Permission;
import sn.iage.isi.book_management_jee2.models.Role;
import sn.iage.isi.book_management_jee2.models.User;
import sn.iage.isi.book_management_jee2.utils.Tools;

import java.util.*;

@WebListener
public class DataInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Map<String, Permission> perms = ensurePermissions(em);

            Role adminRole = ensureRole(em, "ADMIN", "Administrateur systeme",
                    new HashSet<>(perms.values()));

            ensureRole(em, "LIBRARIAN", "Bibliothecaire",
                    new HashSet<>(Arrays.asList(
                            perms.get("MANAGE_BOOKS"),
                            perms.get("MANAGE_CATEGORIES")
                    )));

            ensureAdminUser(em, adminRole);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static final String[] PERMISSION_NAMES = {
            "MANAGE_BOOKS", "MANAGE_CATEGORIES", "MANAGE_USERS", "MANAGE_ROLES"
    };

    private Map<String, Permission> ensurePermissions(EntityManager em) {
        Map<String, Permission> map = new HashMap<>();
        for (String name : PERMISSION_NAMES) {
            List<Permission> found = em.createQuery(
                            "SELECT p FROM Permission p WHERE p.name = :n", Permission.class)
                    .setParameter("n", name).getResultList();
            Permission perm;
            if (found.isEmpty()) {
                perm = new Permission();
                perm.setName(name);
                perm.setDescription(descFor(name));
                em.persist(perm);
            } else {
                perm = found.get(0);
            }
            map.put(name, perm);
        }
        return map;
    }

    private Role ensureRole(EntityManager em, String name, String desc, Set<Permission> permissions) {
        List<Role> found = em.createQuery(
                        "SELECT r FROM Role r WHERE r.name = :n", Role.class)
                .setParameter("n", name).getResultList();
        if (!found.isEmpty()) return found.get(0);
        Role r = new Role();
        r.setName(name);
        r.setDescription(desc);
        r.setPermissions(permissions);
        em.persist(r);
        return r;
    }

    private void ensureAdminUser(EntityManager em, Role adminRole) {
        List<User> found = em.createQuery(
                        "SELECT u FROM User u WHERE u.username = :u", User.class)
                .setParameter("u", "admin").getResultList();
        if (!found.isEmpty()) return;
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(Tools.hashPassword("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("System");
        admin.setEmail("admin@bibliotheque.com");
        admin.setRole(adminRole);
        admin.setActive(true);
        em.persist(admin);
    }

    private String descFor(String name) {
        switch (name) {
            case "MANAGE_BOOKS":      return "Creer, modifier et supprimer des livres";
            case "MANAGE_CATEGORIES": return "Creer, modifier et supprimer des categories";
            case "MANAGE_USERS":      return "Gerer les comptes utilisateurs";
            case "MANAGE_ROLES":      return "Gerer les roles et permissions";
            default:                  return name;
        }
    }
}