package ma.fstt.ecommerce.util;

import ma.fstt.ecommerce.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;

@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            try {
                Long countCategories = em.createQuery("SELECT COUNT(c) FROM Categorie c", Long.class).getSingleResult();
                if (countCategories == null || countCategories == 0L) {
                    initialiserDonneesDemonstration(em);
                }
            } finally {
                em.close();
            }
            System.out.println("[AtlasTech E-Commerce] JPA initialisé avec succès (MySQL - EcommercePU).");
        } catch (Exception e) {
            System.err.println("[AtlasTech E-Commerce] Erreur lors de l'initialisation JPA : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initialiserDonneesDemonstration(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. Catégories
            Categorie catLaptop = new Categorie("Ordinateurs & Laptops",
                    "Ultrabooks, stations de travail ingénieur et PC portables haute performance.", "laptop");
            Categorie catPhone = new Categorie("Smartphones & Tablettes",
                    "Derniers smartphones 5G, tablettes tactiles et accessoires mobiles.", "phone");
            Categorie catGaming = new Categorie("Périphériques & Gaming",
                    "Claviers mécaniques, souris optiques, écrans 165 Hz et manettes pro.", "gaming");
            Categorie catAudio = new Categorie("Audio & Objets Connectés",
                    "Casques à réduction active de bruit, écouteurs sans fil et montres connectées.", "audio");
            Categorie catStockage = new Categorie("Composants & Stockage",
                    "SSD NVMe PCIe 4.0, routeurs Wi-Fi 6E, barrettes RAM et hubs USB-C.", "storage");

            em.persist(catLaptop);
            em.persist(catPhone);
            em.persist(catGaming);
            em.persist(catAudio);
            em.persist(catStockage);

            // 2. Produits de la Vitrine (prix en Dirhams MAD)
            Produit p1 = new Produit("PRD-LAP-001", "MacBook Air 15\" M3 (16 Go / 512 Go SSD)", "Apple",
                    "Puce Apple M3 8 coeurs, écran Liquid Retina 15,3 pouces, autonomie jusqu'à 18 heures, châssis aluminium ultra-fin idéal pour le développement logiciel et l'ingénierie.",
                    16490.0, 17990.0, 12,
                    ProductImageGenerator.createSvgDataUri("laptop", "MACBOOK M3", "#0f172a", "#1d4ed8"),
                    true, catLaptop);

            Produit p2 = new Produit("PRD-LAP-002", "ThinkPad X1 Carbon Gen 11 (Core i7 / 32 Go)", "Lenovo",
                    "Certifié Intel Evo, processeur Core i7-1365U, 32 Go LPDDR5, SSD NVMe 1 To, clavier rétroéclairé légendaire et châssis fibre de carbone 1,12 kg.",
                    18200.0, 19500.0, 8,
                    ProductImageGenerator.createSvgDataUri("laptop", "THINKPAD X1", "#111827", "#0f766e"),
                    true, catLaptop);

            Produit p3 = new Produit("PRD-LAP-003", "ASUS ROG Zephyrus G16 RTX 4070", "ASUS",
                    "Écran OLED Nebula 240 Hz, Intel Core Ultra 9, NVIDIA GeForce RTX 4070 8 Go GDDR6, 32 Go RAM — conçu pour l'IA, la 3D et le gaming AAA.",
                    22900.0, null, 5,
                    ProductImageGenerator.createSvgDataUri("laptop", "ROG RTX 4070", "#311042", "#be123c"),
                    false, catLaptop);

            Produit p4 = new Produit("PRD-PHN-001", "Galaxy S24 Ultra 5G (256 Go - Titane)", "Samsung",
                    "Écran Dynamic AMOLED 2X 6,8\" QHD+ 120 Hz, capteur 200 MP, stylet S-Pen intégré, châssis en titane et fonctionnalités Galaxy AI embarquées.",
                    13490.0, 14890.0, 15,
                    ProductImageGenerator.createSvgDataUri("phone", "GALAXY S24 ULTRA", "#1e1b4b", "#4338ca"),
                    true, catPhone);

            Produit p5 = new Produit("PRD-PHN-002", "iPhone 16 Pro 256 Go - Titane Naturel", "Apple",
                    "Puce A18 Pro gravée en 3 nm, écran Super Retina XDR ProMotion 120 Hz, triple caméra Pro 48 MP avec zoom optique 5x et USB-C haut débit.",
                    14290.0, null, 10,
                    ProductImageGenerator.createSvgDataUri("phone", "IPHONE 16 PRO", "#1c1917", "#57534e"),
                    true, catPhone);

            Produit p6 = new Produit("PRD-PHN-003", "iPad Air M2 11\" Wi-Fi 256 Go", "Apple",
                    "Puce M2 ultra-rapide, compatible Apple Pencil Pro, écran Liquid Retina laminé antireflet — parfait pour la prise de notes en cycle ingénieur.",
                    7890.0, 8490.0, 14,
                    ProductImageGenerator.createSvgDataUri("phone", "IPAD AIR M2", "#0c4a6e", "#0284c7"),
                    false, catPhone);

            Produit p7 = new Produit("PRD-GAM-001", "Clavier Mécanique Keychron Q1 Pro Sans Fil", "Keychron",
                    "Boîtier CNC tout aluminium, montage Gasket, switches tactiles lubrifiés Hot-Swap, connexion Bluetooth 5.1 multi-appareils et firmware QMK/VIA.",
                    1890.0, 2190.0, 20,
                    ProductImageGenerator.createSvgDataUri("gaming", "KEYCHRON Q1 PRO", "#1e293b", "#6366f1"),
                    true, catGaming);

            Produit p8 = new Produit("PRD-GAM-002", "Souris Logitech MX Master 3S Sans Fil", "Logitech",
                    "Capteur optique Darkfield 8000 DPI sur verre, molette électromagnétique MagSpeed ultra-rapide et clics silencieux ergonomiques pour développeurs.",
                    1090.0, 1290.0, 25,
                    ProductImageGenerator.createSvgDataUri("gaming", "MX MASTER 3S", "#0f172a", "#0d9488"),
                    false, catGaming);

            Produit p9 = new Produit("PRD-GAM-003", "Manette DualSense Wireless PS5 & PC", "Sony",
                    "Retour haptique immersif, gâchettes adaptatives dynamiques, microphone intégré et compatibilité native Bluetooth/USB-C sur PC et PlayStation 5.",
                    790.0, 890.0, 30,
                    ProductImageGenerator.createSvgDataUri("gaming", "DUALSENSE PRO", "#172554", "#2563eb"),
                    false, catGaming);

            Produit p10 = new Produit("PRD-AUD-001", "Casque Sony WH-1000XM5 Réduction de Bruit", "Sony",
                    "Référence mondiale de l'ANC avec 8 microphones, autonomie 30 heures, charge rapide USB-PD (3 min = 3h d'écoute) et audio haute résolution LDAC.",
                    3690.0, 4190.0, 18,
                    ProductImageGenerator.createSvgDataUri("audio", "WH-1000XM5", "#18181b", "#4f46e5"),
                    true, catAudio);

            Produit p11 = new Produit("PRD-AUD-002", "AirPods Pro (2e génération) USB-C", "Apple",
                    "Puce H2, réduction active du bruit jusqu'à 2x supérieure, mode Transparence adaptatif et boîtier MagSafe USB-C résistant à l'eau IP54.",
                    2790.0, null, 22,
                    ProductImageGenerator.createSvgDataUri("audio", "AIRPODS PRO 2", "#0f172a", "#0369a1"),
                    false, catAudio);

            Produit p12 = new Produit("PRD-STK-001", "SSD Samsung 990 PRO NVMe M.2 - 2 To", "Samsung",
                    "Vitesses séquentielles fulgurantes jusqu'à 7450 Mo/s en lecture et 6900 Mo/s en écriture PCIe 4.0, contrôleur nickelé haute efficacité thermique.",
                    1950.0, 2290.0, 16,
                    ProductImageGenerator.createSvgDataUri("storage", "990 PRO 2TB", "#1e1b4b", "#b91c1c"),
                    false, catStockage);

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.persist(p5);
            em.persist(p6);
            em.persist(p7);
            em.persist(p8);
            em.persist(p9);
            em.persist(p10);
            em.persist(p11);
            em.persist(p12);

            // 3. Internautes de démonstration (Client + Administrateur)
            Internaute admin = new Internaute(
                    "ELAACHAK", "Admin LSI", "admin@fstt.ac.ma",
                    PasswordUtil.hashPassword("admin123"),
                    "+212 6 61 00 11 22",
                    "Ancienne Route de l'Aéroport, Km 10, Ziaten, BP 416",
                    "Tanger", "ADMIN"
            );
            em.persist(admin);
            em.persist(new Panier(admin));

            Internaute client = new Internaute(
                    "BENALI", "Youssef", "client@fstt.ac.ma",
                    PasswordUtil.hashPassword("client123"),
                    "+212 6 77 88 99 00",
                    "Boulevard Mohammed VI, Résidence Corniche N°14",
                    "Tanger", "CLIENT"
            );
            em.persist(client);
            em.persist(new Panier(client));

            // 4. Une commande exemple initiale pour illustrer l'historique
            Commande cmdDemo = new Commande();
            cmdDemo.setNumeroCommande("CMD-20261109-1001");
            cmdDemo.setDateCommande(new Date());
            cmdDemo.setInternaute(client);
            cmdDemo.setStatut("LIVREE");
            cmdDemo.setAdresseLivraison(client.getAdresse());
            cmdDemo.setVilleLivraison(client.getVille());
            cmdDemo.setTelephoneContact(client.getTelephone());
            cmdDemo.setModePaiement("CARTE_BANCAIRE");
            cmdDemo.setFraisLivraison(0.0);

            LigneCommande lc1 = new LigneCommande(cmdDemo, p8, 1, p8.getPrix());
            LigneCommande lc2 = new LigneCommande(cmdDemo, p12, 1, p12.getPrix());
            cmdDemo.getLignesCommande().add(lc1);
            cmdDemo.getLignesCommande().add(lc2);
            cmdDemo.setMontantTotal(lc1.getSousTotal() + lc2.getSousTotal());
            em.persist(cmdDemo);

            tx.commit();
            System.out.println("[AtlasTech E-Commerce] Données initiales insérées avec succès (5 catégories, 12 produits, 2 comptes).");
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}
