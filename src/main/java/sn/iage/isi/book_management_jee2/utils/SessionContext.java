package sn.iage.isi.book_management_jee2.utils;

/**
 * Contexte d'exécution courant (thread-local), alimenté par AuthFilter
 * une fois l'authentification en place. Pour l'instant, alimenté
 * manuellement dans Main pour les tests.
 */
public class SessionContext {

    private static final ThreadLocal<String> CURRENT_USERNAME = new ThreadLocal<>();

    private SessionContext() {}

    public static void setCurrentUsername(String username) {
        CURRENT_USERNAME.set(username);
    }

    public static String getCurrentUsername() {
        String username = CURRENT_USERNAME.get();
        return username != null ? username : "system"; // valeur par défaut
    }

    public static void clear() {
        CURRENT_USERNAME.remove();
    }
}