package ma.fstt.ecommerce.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "EcommercePU";
    private static volatile EntityManagerFactory emf;

    private JPAUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (JPAUtil.class) {
                if (emf == null) {
                    Map<String, String> overrides = new HashMap<>();
                    String dbUrl = System.getenv("DB_URL");
                    String dbUser = System.getenv("DB_USER");
                    String dbPass = System.getenv("DB_PASSWORD");
                    if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                        overrides.put("javax.persistence.jdbc.url", dbUrl.trim());
                    }
                    if (dbUser != null && !dbUser.trim().isEmpty()) {
                        overrides.put("javax.persistence.jdbc.user", dbUser.trim());
                    }
                    if (dbPass != null) {
                        overrides.put("javax.persistence.jdbc.password", dbPass);
                    }
                    emf = overrides.isEmpty()
                            ? Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)
                            : Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, overrides);
                }
            }
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }
}
