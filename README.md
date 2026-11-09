# AtlasTech Store — Application E-Commerce JSF 2.2 & JPA (EclipseLink / MySQL)

**Université Abdelmalek Essaâdi — Faculté des Sciences et Techniques de Tanger (FSTT)**  
**Département Génie Informatique — Cycle Ingénieur : LSI — Module JEE**  
**Encadré par : Pr. Lotfi ELAACHAK**

---

## 1. Présentation du Projet

Application web e-Commerce complète développée de A à Z selon le cahier des charges de l'atelier JEE :
- **Étape 1 (Conception UML)** : Diagramme de classes complet disponible dans `docs/diagramme_classes_uml.png` couvrant les 7 entités (`Internaute`, `Panier`, `LignePanier`, `Commande`, `LigneCommande`, `Produit`, `Categorie`).
- **Étape 2 (Projet Web Maven)** : Web Module 4.0 (`WEB-INF/web.xml`) et `pom.xml` intégrant **JSF 2.2.20 (Mojarra)**, **JPA 2.2 / EclipseLink 2.7.5**, **MySQL Connector/J** et **Servlet API 4.0**.
- **Étape 3 (Couche Modèle / Persistance & Transactions JPA)** : Entités JPA annotées, `META-INF/persistence.xml` (`EcommercePU`), génération automatique des tables MySQL, DAOs transactionnels (`EntityTransaction` ACID), initialisateur automatique de données (`AppInitListener`) + script SQL complet dans `sql/ecommerce_jsf_jpa.sql`.
- **Étape 4 (Managed Beans JSF)** : `VitrineBean`, `PanierBean`, `InternauteBean` et `AdminBean` + 8 vues Facelets XHTML responsives.
- **Mini Rapport** : Rapport complet au format Word disponible dans `docs/Mini_Rapport_Atelier_JSF_JPA_FSTT.docx`.

---

## 2. Arborescence du Projet

```text
ecommerce-jsf-jpa/
├── pom.xml                                        # Configuration Maven & Dépendances JEE
├── sql/
│   └── ecommerce_jsf_jpa.sql                      # Script SQL MySQL complet (DDL + Données)
├── docs/
│   ├── diagramme_classes_uml.png                  # Diagramme de classes UML (Étape 1)
│   └── Mini_Rapport_Atelier_JSF_JPA_FSTT.docx     # Mini Rapport académique (Word)
└── src/main/
    ├── java/ma/fstt/ecommerce/
    │   ├── entities/                              # 7 Entités JPA (@Entity)
    │   │   ├── Categorie.java
    │   │   ├── Produit.java
    │   │   ├── Internaute.java
    │   │   ├── Panier.java
    │   │   ├── LignePanier.java
    │   │   ├── Commande.java
    │   │   └── LigneCommande.java
    │   ├── dao/                                   # Couche DAO avec Transactions JPA
    │   │   ├── CategorieDAO.java
    │   │   ├── ProduitDAO.java
    │   │   ├── InternauteDAO.java
    │   │   ├── PanierDAO.java
    │   │   └── CommandeDAO.java
    │   ├── beans/                                 # Managed Beans JSF 2.2 (@SessionScoped)
    │   │   ├── VitrineBean.java
    │   │   ├── PanierBean.java
    │   │   ├── InternauteBean.java
    │   │   └── AdminBean.java
    │   └── util/                                  # Utilitaires JPA, SHA-256, Encodage & Seed
    │       ├── JPAUtil.java
    │       ├── PasswordUtil.java
    │       ├── ProductImageGenerator.java
    │       ├── CharacterEncodingFilter.java
    │       └── AppInitListener.java
    ├── resources/META-INF/
    │   └── persistence.xml                        # Unité de persistance JPA (EcommercePU)
    └── webapp/
        ├── resources/css/style.css                # Design responsive moderne
        ├── WEB-INF/
        │   ├── web.xml                            # Servlet 4.0 + FacesServlet (*.xhtml)
        │   ├── faces-config.xml                   # Configuration JSF 2.2
        │   └── templates/layout.xhtml             # Template principal Facelets
        ├── index.xhtml                            # Vitrine, recherche, filtres & tri
        ├── produit.xhtml                          # Fiche détaillée produit & similaires
        ├── panier.xhtml                           # Gestion du panier & validation commande
        ├── connexion.xhtml                        # Authentification + Accès démo 1 clic
        ├── inscription.xhtml                      # Création de compte Internaute
        ├── profil.xhtml                           # Profil Internaute & historique commandes
        ├── commande-confirmation.xhtml            # Reçu / Facture imprimable
        └── admin.xhtml                            # Back-office CRUD Produits/Catégories/Commandes
```

---

## 3. Exécution sous Eclipse IDE + MySQL + Apache Tomcat 9

1. **Démarrer MySQL** (XAMPP / WAMP / MySQL Server sur `127.0.0.1:3306`).
   - Par défaut dans `src/main/resources/META-INF/persistence.xml` : `user = root`, `password = ""` (vide). Adaptez le mot de passe si votre serveur MySQL local en possède un.
   - La base `ecommerce_jsf_jpa`, ses 7 tables et les données de démonstration sont **créées automatiquement au démarrage** de Tomcat ! Vous pouvez aussi importer manuellement `sql/ecommerce_jsf_jpa.sql` via phpMyAdmin ou MySQL Workbench.
2. **Importer dans Eclipse IDE (Enterprise Java / JEE)** :
   - `File` -> `Import...` -> `Maven` -> `Existing Maven Projects` -> Sélectionner le dossier `ecommerce-jsf-jpa` -> `Finish`.
3. **Lancer sur Apache Tomcat 9** :
   - Clic droit sur le projet -> `Run As` -> `Run on Server` -> Sélectionner **Apache Tomcat v9.0**.
   - Ouvrir : `http://localhost:8080/ecommerce-jsf-jpa/index.xhtml`

---

## 4. Comptes de Démonstration Pré-configurés

| Rôle | E-mail | Mot de passe | Fonctionnalités |
|---|---|---|---|
| **Client (Internaute)** | `client@fstt.ac.ma` | `client123` | Panier persistant MySQL, passage de commandes, profil & historique |
| **Administrateur** | `admin@fstt.ac.ma` | `admin123` | Tableau de bord KPI, CRUD Produits & Catégories, gestion des commandes |
