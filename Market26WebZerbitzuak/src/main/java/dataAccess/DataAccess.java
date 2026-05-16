package dataAccess;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.User;
import domain.Admin;
import domain.BalorazioProfila;
import domain.DiruKontua;
import domain.Erabiltzailea;
import domain.Erreklamazioa;
import domain.Sale;
import domain.Eskaera;
import domain.Mugimendua;
import domain.Oferta;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

public class DataAccess {
    private EntityManager db;
    private EntityManagerFactory emf;
    private static final int baseSize = 160;
    private static final String basePath = "src/main/resources/images/";

    ConfigXML c = ConfigXML.getInstance();

    public DataAccess() {
        String fileName = c.getDbFilename();
        File dbFile = new File(fileName);
        boolean exists = dbFile.exists();
        open();

        if (c.isDatabaseInitialized()) {
            if (!exists) {
                initializeDB();
            } else {
                System.out.println("Database file already exists; skipping initialization.");
            }
        }

        System.out.println("DataAccess created => isDatabaseLocal: " + c.isDatabaseLocal() + " isDatabaseInitialized: " + c.isDatabaseInitialized());
        close();
    }

    public DataAccess(EntityManager db) {
        this.db = db;
    }

    public void initializeDB() {
        db.getTransaction().begin();
        try {
            Erabiltzailea seller1 = new User("seller1@gmail.com", "Aitor Fernandez", "1234", "666666666");
            Erabiltzailea seller2 = new User("seller22@gmail.com", "Ane Gaztañaga", "1234", "655555555");
            Erabiltzailea seller3 = new User("seller3@gmail.com", "Test Seller", "1234", "644444444");
            Erabiltzailea admin = new Admin("admin@gmail.com", "1234");

            ((User) seller1).addDiruKontua("ES45678923245", 1000);
            ((User) seller1).addDiruKontua("ES37848898695", 5);
            ((User) seller3).addDiruKontua("ES45689653264", 580);
            ((User) seller2).addDiruKontua("ES09245762456", 20);
            ((User) seller3).addDiruKontua("ES44764463247", 453);
            ((User) seller3).addDiruKontua("ES34786843767", 100);
            ((User) seller3).addDiruKontua("ES97935722866", 40);

            Date today = UtilDate.trim(new Date());

            ((User) seller1).addSale("futbol baloia", "oso polita, gutxi erabilita", 2, 10, today, null, "Ez erosita");
            ((User) seller1).addSale("salomon mendiko botak", "44 zenbakia, 3 ateraldi", 2, 20, today, null, "Ez erosita");
            ((User) seller1).addSale("samsung 42\" telebista", "berria, erabili gabe", 1, 175, today, null, "Ez erosita");

            ((User) seller2).addSale("imac 27", "7 urte, dena ondo dabil", 1, 200, today, null, "Ez erosita");
            ((User) seller2).addSale("iphone 17", "oso gutxi erabilita", 2, 400, today, null, "Ez erosita");
            ((User) seller2).addSale("orbea mendiko bizikleta", "29\" 10 urte, mantenua behar du", 3, 225, today, null, "Ez erosita");
            ((User) seller2).addSale("polar kilor erlojua", "Vantage M, ondo dago", 3, 30, today, null, "Ez erosita");

            ((User) seller3).addSale("sukaldeko mahaia", "1.8*0.8, 4 aulkiekin. Prezio finkoa", 3, 45, today, null, "Ez erosita");

            db.persist(seller1);
            db.persist(seller2);
            db.persist(seller3);
            db.persist(admin);

            db.getTransaction().commit();
            System.out.println("Db initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Sale createSale(String title, String description, int status, float price,
            Date pubDate, String sellerEmail, File file, String egoera)
            throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {

        if (pubDate.before(UtilDate.trim(new Date()))) {
            throw new MustBeLaterThanTodayException(
                    ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorSaleMustBeLaterThanToday"));
        }

        if (file == null) {
            throw new FileNotUploadedException(
                    ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorFileNotUploadedException"));
        }

        db.getTransaction().begin();
        try {
            User seller = db.find(User.class, sellerEmail);

            if (seller == null) {
                db.getTransaction().rollback();
                throw new IllegalArgumentException("No se encontró un vendedor con email: " + sellerEmail);
            }

            if (seller.doesSaleExist(title)) {
                db.getTransaction().rollback();
                throw new SaleAlreadyExistException(
                        ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SaleAlreadyExist"));
            }

            Sale sale = seller.addSale(title, description, status, price, pubDate, file, egoera);
            db.persist(seller);
            db.getTransaction().commit();
            return sale;

        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public List<Sale> getSales(String desc) {
        TypedQuery<Sale> query = db.createQuery(
                "SELECT s FROM User u JOIN u.sales s WHERE s.title LIKE ?1", Sale.class);
        query.setParameter(1, "%" + desc + "%");
        return query.getResultList();
    }

    public List<Sale> getPublishedSales(String desc, Date pubDate) {
        TypedQuery<Sale> query = db.createQuery(
                "SELECT s FROM User u JOIN u.sales s WHERE s.title LIKE ?1 AND s.pubDate <=?2", Sale.class);
        query.setParameter(1, "%" + desc + "%");
        query.setParameter(2, pubDate);
        return query.getResultList();
    }

    public void open() {
        String fileName = c.getDbFilename();
        if (c.isDatabaseLocal()) {
            emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
            db = emf.createEntityManager();
        } else {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.user", c.getUser());
            properties.put("javax.persistence.jdbc.password", c.getPassword());
            emf = Persistence.createEntityManagerFactory(
                    "objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);
            db = emf.createEntityManager();
        }
        System.out.println("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());
    }

    public void close() {
        try {
            if (db != null && db.isOpen()) {
                if (db.getTransaction().isActive()) {
                    try {
                        db.getTransaction().commit();
                    } catch (Exception ex) {
                        try { db.getTransaction().rollback(); } catch (Exception e) { }
                    }
                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
        System.out.println("DataAccess closed");
    }

    public Erabiltzailea isLogin(String email, String password) {
        TypedQuery<User> queryUser = db.createQuery(
                "SELECT u FROM User u WHERE u.email=?1 AND u.password=?2", User.class);
        queryUser.setParameter(1, email);
        queryUser.setParameter(2, password);
        if (!queryUser.getResultList().isEmpty()) return queryUser.getResultList().get(0);

        TypedQuery<Admin> queryAdmin = db.createQuery(
                "SELECT a FROM Admin a WHERE a.email=?1 AND a.password=?2", Admin.class);
        queryAdmin.setParameter(1, email);
        queryAdmin.setParameter(2, password);
        if (!queryAdmin.getResultList().isEmpty()) return queryAdmin.getResultList().get(0);

        return null;
    }

    public Erabiltzailea isLogin(String email) {
        TypedQuery<User> queryUser = db.createQuery(
                "SELECT u FROM User u WHERE u.email=?1", User.class);
        queryUser.setParameter(1, email);
        if (!queryUser.getResultList().isEmpty()) return queryUser.getResultList().get(0);

        TypedQuery<Admin> queryAdmin = db.createQuery(
                "SELECT a FROM Admin a WHERE a.email=?1", Admin.class);
        queryAdmin.setParameter(1, email);
        if (!queryAdmin.getResultList().isEmpty()) return queryAdmin.getResultList().get(0);

        return null;
    }

    public void addUser(String email, String password, String name, String telefonoa) {
        Erabiltzailea u = db.find(Erabiltzailea.class, email);
        if (u != null) {
            throw new IllegalArgumentException("User with email " + email + " already exists.");
        } else {
            db.getTransaction().begin();
            db.persist(new User(email, name, password, telefonoa));
            db.getTransaction().commit();
        }
    }

    public Erabiltzailea getUser(String email) {
        TypedQuery<User> query = db.createQuery(
                "SELECT u FROM Erabiltzailea u WHERE u.email=?1", User.class);
        query.setParameter(1, email);
        return query.getResultList().isEmpty() ? null : query.getResultList().get(0);
    }

    public void buyProduct(Sale sale, String email) {
        Erabiltzailea u = db.find(Erabiltzailea.class, email);
        if (u != null) {
            Sale managedSale = db.merge(sale);
            db.getTransaction().begin();
            ((User) u).addErositakoa(managedSale);
            db.getTransaction().commit();
            updateEgoera(managedSale, "Erosita");
            updateDiruKop(managedSale.getSeller().getKontuak().get(0).getKontuZenb(),
                    getDiruKop(managedSale.getSeller().getKontuak().get(0).getKontuZenb()) + managedSale.getPrice());
            updateDiruKop(((User) u).getKontuak().get(0).getKontuZenb(),
                    getDiruKop(((User) u).getKontuak().get(0).getKontuZenb()) - managedSale.getPrice());
            addMugimenduak(managedSale.getPrice(), new Date(), managedSale.getTitle(), "Salmenta",
                    managedSale.getSeller().getKontuak().get(0).getKontuZenb());
            addMugimenduak(-managedSale.getPrice(), new Date(), managedSale.getTitle(), "Erosketa",
                    ((User) u).getKontuak().get(0).getKontuZenb());
        }
    }

    public double getDiruKop(String zenb) {
        TypedQuery<Double> query = db.createQuery(
                "SELECT d.diruKop FROM User u JOIN u.kontuak d WHERE d.kontuZenb = ?1", Double.class);
        query.setParameter(1, zenb);
        List<Double> result = query.getResultList();
        return result.isEmpty() ? 0 : result.get(0);
    }

    public void updateDiruKop(String zenb, double diruKop) {
        DiruKontua d = db.find(DiruKontua.class, zenb);
        if (d != null) {
            db.getTransaction().begin();
            d.setDiruKop(diruKop);
            db.getTransaction().commit();
        }
    }

    public void updateEgoera(Sale sale, String egoera) {
        Sale s = db.find(Sale.class, sale.getSaleNumber());
        if (s != null) {
            db.getTransaction().begin();
            s.setEgoera(egoera);
            db.getTransaction().commit();
        }
    }

    public void createErreklamazio(String titulua, String deskripzioa, File file, Sale sale) {
        db.getTransaction().begin();
        Sale s = db.find(Sale.class, sale.getSaleNumber());
        if (s != null) {
            Erreklamazioa erre = new Erreklamazioa(titulua, deskripzioa,
                    file != null ? file.getName() : null, s, "Pendiente");
            s.setErreklamazioa(erre);
            User seller = s.getSeller();
            seller.getErreklamazioak().add(erre);
        }
        db.getTransaction().commit();
    }

    public String getFirstAccountNumber(String email) {
        TypedQuery<String> query = db.createQuery(
                "SELECT d.kontuZenb FROM User u JOIN u.kontuak d WHERE u.email = ?1", String.class);
        query.setParameter(1, email);
        List<String> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }


    public void updateEgoeraErreklamazioaById(Integer erreklamazioId, String egoera) {
        Erreklamazioa e = db.find(Erreklamazioa.class, erreklamazioId);
        if (e != null) {
            db.getTransaction().begin();
            e.setEgoera(egoera);
            db.getTransaction().commit();
        }
    }

    public List<Erreklamazioa> getErreklamazioakByEgoera(String egoera) {
        TypedQuery<Erreklamazioa> query = db.createQuery(
                "SELECT e FROM Erreklamazioa e WHERE e.egoera = ?1", Erreklamazioa.class);
        query.setParameter(1, egoera);
        return query.getResultList();
    }

    public void addMugimenduak(float diruKop, Date data, String productName, String mota, String kontuZenb) {
        db.getTransaction().begin();
        DiruKontua d = db.find(DiruKontua.class, kontuZenb);
        if (d != null) {
            d.addMugimenduak(diruKop, data, productName, mota);
            db.getTransaction().commit();
        }
    }

    public void createBalorazioa(String balorazioa, int puntuazioa, User user, Sale sale) {
        db.getTransaction().begin();
        User u = db.find(User.class, user.getEmail());
        if (u != null) {
            BalorazioProfila b = u.addBalorazioa(balorazioa, puntuazioa, user, sale);
            sale.setBalorazioProfila(b);
        }
        db.getTransaction().commit();
    }

    public void createEskaera(String eskaera, String user) {
        db.getTransaction().begin();
        User u = db.find(User.class, user);
        if (u != null) u.addEskaera(eskaera);
        db.getTransaction().commit();
    }

    public List<Eskaera> getAllEskaerak() {
        TypedQuery<Eskaera> query = db.createQuery("SELECT e FROM Eskaera e", Eskaera.class);
        return query.getResultList();
    }


    public Eskaera getEskaera(Integer eskaeraId) {
        return db.find(Eskaera.class, eskaeraId);
    }

    public void createOferta(String title, String description, double price, User user, Eskaera eskaera) {
        db.getTransaction().begin();
        Eskaera e = db.find(Eskaera.class, eskaera.getId());
        if (e != null) e.addOferta(title, description, price, user);
        db.getTransaction().commit();
    }

    public void removeOfertaAndEskaera(Integer ofertaId) {
        db.getTransaction().begin();
        Oferta oferta = db.find(Oferta.class, ofertaId);
        if (oferta != null) {
            Eskaera e = db.find(Eskaera.class, oferta.getEskaera().getId());
            if (e != null) {
                User buyer = e.getUser();
                User seller = db.find(User.class, oferta.getUser().getEmail());

                if (buyer != null && seller != null) {
                    Sale sale = new Sale(oferta.getTitle(), oferta.getDescription(), 1,
                            (float) oferta.getPrice(), new Date(), null, seller, "Erosita");
                    db.persist(sale);
                    buyer.addErositakoa(sale);
                    seller.getSales().add(sale);
                    buyer.getEskaerak().remove(e);

                    float price = (float) oferta.getPrice();

                    if (buyer.getKontuak() != null && !buyer.getKontuak().isEmpty()) {
                        DiruKontua buyerKontua = buyer.getKontuak().get(0);
                        buyerKontua.setDiruKop(buyerKontua.getDiruKop() - price);
                        buyerKontua.addMugimenduak(-price, new Date(), oferta.getTitle(), "Erosketa");
                    }

                    if (seller.getKontuak() != null && !seller.getKontuak().isEmpty()) {
                        DiruKontua sellerKontua = seller.getKontuak().get(0);
                        sellerKontua.setDiruKop(sellerKontua.getDiruKop() + price);
                        sellerKontua.addMugimenduak(price, new Date(), oferta.getTitle(), "Salmenta");
                    }
                }
                db.remove(e);
            }
        }
        db.getTransaction().commit();
    }

    public void addProduktuaSaskira(Sale sale, int i, String userMail) {
        db.getTransaction().begin();
        try {
            User u = db.find(User.class, userMail);
            Sale s = db.find(Sale.class, sale.getSaleNumber());
            if (u != null && s != null) {
                u.addProduktuaSaskira(s, i);
                db.getTransaction().commit();
            } else {
                db.getTransaction().rollback();
            }
        } catch (RuntimeException ex) {
            if (db.getTransaction().isActive()) {
                try { db.getTransaction().rollback(); } catch (Exception e) { }
            }
            throw ex;
        }
    }

    public double deskontuaAplikatu(double prezioa, int num) {
        db.getTransaction().begin();
        double deskontua = prezioa;
        if (num == 2) deskontua = prezioa * 0.90;
        else if (num == 3) deskontua = prezioa * 0.85;
        else if (num >= 4) deskontua = prezioa * 0.80;
        db.getTransaction().commit();
        return deskontua;
    }

    public void removeProduktuaSaskitik(Sale sale, int i, String userMail) {
        db.getTransaction().begin();
        try {
            User u = db.find(User.class, userMail);
            Sale s = db.find(Sale.class, sale.getSaleNumber());
            if (u != null && s != null) {
                u.removeProduktuaSaskitik(s, i);
                db.getTransaction().commit();
            } else {
                db.getTransaction().rollback();
            }
        } catch (RuntimeException ex) {
            if (db.getTransaction().isActive()) {
                try { db.getTransaction().rollback(); } catch (Exception e) { }
            }
            throw ex;
        }
    }

    public void clearSaskia(int i, String userMail) {
        db.getTransaction().begin();
        try {
            User u = db.find(User.class, userMail);
            if (u != null) {
                u.clearSaskia(i);
                db.getTransaction().commit();
            } else {
                db.getTransaction().rollback();
            }
        } catch (RuntimeException ex) {
            if (db.getTransaction().isActive()) {
                try { db.getTransaction().rollback(); } catch (Exception e) { }
            }
            throw ex;
        }
    }

    public Sale getSale(Integer saleNumber) {
        return db.find(Sale.class, saleNumber);
    }

    public List<String> getUserKontuZenbakiak(String userMail) {
        TypedQuery<String> query = db.createQuery(
                "SELECT d.kontuZenb FROM User u JOIN u.kontuak d WHERE u.email=?1", String.class);
        query.setParameter(1, userMail);
        return query.getResultList();
    }

    public List<Sale> getErositakoak(String userMail) {
        TypedQuery<Sale> query = db.createQuery(
                "SELECT s FROM User u JOIN u.erositakoak s WHERE u.email=?1", Sale.class);
        query.setParameter(1, userMail);
        return query.getResultList();
    }

    public boolean hasErreklamazioak(String userMail) {
        TypedQuery<Long> query = db.createQuery(
                "SELECT COUNT(e) FROM User u JOIN u.erreklamazioak e WHERE u.email=?1", Long.class);
        query.setParameter(1, userMail);
        return query.getSingleResult() > 0;
    }

    public List<Mugimendua> getMugimenduak(String userMail) {
        TypedQuery<Mugimendua> query = db.createQuery(
                "SELECT m FROM User u JOIN u.kontuak k JOIN k.mugimenduak m WHERE u.email=?1",
                Mugimendua.class);
        query.setParameter(1, userMail);
        return query.getResultList();
    }

    public List<Eskaera> getEskaerakByUser(String userMail) {
        TypedQuery<Eskaera> query = db.createQuery(
                "SELECT e FROM User u JOIN u.eskaerak e WHERE u.email=?1", Eskaera.class);
        query.setParameter(1, userMail);
        return query.getResultList();
    }
    
    public List<BalorazioProfila> getBalorazioakByUser(String userMail) {
        TypedQuery<BalorazioProfila> query = db.createQuery(
            "SELECT b FROM User u JOIN u.balorazioak b WHERE u.email=?1", BalorazioProfila.class);
        query.setParameter(1, userMail);
        return query.getResultList();
    }
    
    public List<Erreklamazioa> getErreklamazioakByUser(String userMail) {
        TypedQuery<Erreklamazioa> query = db.createQuery(
            "SELECT e FROM User u JOIN u.erreklamazioak e WHERE u.email=?1", Erreklamazioa.class);
        query.setParameter(1, userMail);
        return query.getResultList();
    }
    
    public double getSaskiaTotalPrice(String userMail, int saskiaIndex) {
        db.getTransaction().begin();
    	User user = db.find(User.class, userMail);
        double total = 0.0;
        if (user != null && user.getSaskiak() != null && user.getSaskiak().size() > saskiaIndex) {
            total = user.getSaskiak().get(saskiaIndex).getPrezioTotala();
        }
        db.getTransaction().commit();
        return total;
    }
    
    public List<Sale> getSaskiaSales(String userMail, int saskiaIndex) {
        db.getTransaction().begin();
    	User user = db.find(User.class, userMail);
        List<Sale> res = new ArrayList<>();
        if (user != null && user.getSaskiak() != null && user.getSaskiak().size() > saskiaIndex) {
            res = user.getSaskiak().get(saskiaIndex).getPruduktuak();
        }
        db.getTransaction().commit();
        return res;
    }
}