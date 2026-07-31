package sn.iage.isi.book_management_jee2.utils;

import sn.iage.isi.book_management_jee2.models.User;

/**
 * Contexte d'exécution courant (thread-local), alimenté par AuthFilter
 * une fois l'authentification en place. Pour l'instant, alimenté
 * manuellement dans Main pour les tests.
 */
public class SessionContext {

    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    public static User getCurrentUser() { return CURRENT_USER.get(); }
    public static void setCurrentUser(User user) { CURRENT_USER.set(user); }
    public static void clear() { CURRENT_USER.remove(); }

    public static String getCurrentUsername() {
        User u = CURRENT_USER.get();
        return u != null ? u.getUsername() : System.getProperty("user.name", "system");
    }

    public static boolean hasPermission(String permission) {
        User u = CURRENT_USER.get();
        if (u == null || u.getRole() == null) return false;
        return u.getRole().getPermissions().stream()
                .anyMatch(p -> p.getName().equals(permission));
    }

    public static boolean hasRole(String roleName) {
        User u = CURRENT_USER.get();
        if (u == null || u.getRole() == null) return false;
        return roleName.equals(u.getRole().getName());
    }
}