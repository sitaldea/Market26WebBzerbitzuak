package businessLogic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dataAccess.DataAccess;
import domain.Admin;
import domain.BalorazioProfila;
import domain.Erabiltzailea;
import domain.Sale;
import domain.User;
import domain.Erreklamazioa;
import domain.Eskaera;
import domain.Mugimendua;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation implements BLFacade {

    private static final String basePath = "src/main/resources/images/";
    DataAccess dbManager;

    public BLFacadeImplementation() {
        System.out.println("Creating BLFacadeImplementation instance");
        dbManager = new DataAccess();
    }

    public BLFacadeImplementation(DataAccess da) {
        System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
        dbManager = da;
    }

    @WebMethod
    public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, byte[] fileContent, String fileName, String egoera)
            throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
        File file = null;
        if (fileContent != null && fileName != null) {
            try {
                file = new File(basePath + fileName);
                Files.write(file.toPath(), fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dbManager.open();
        Sale product = dbManager.createSale(title, description, status, price, pubDate, sellerEmail, file, egoera);
        dbManager.close();
        return product;
    }

    @WebMethod
    public List<Sale> getSales(String desc) {
        dbManager.open();
        List<Sale> rides = dbManager.getSales(desc);
        dbManager.close();
        return rides;
    }

    @WebMethod
    public List<Sale> getPublishedSales(String desc, Date pubDate) {
        dbManager.open();
        List<Sale> rides = dbManager.getPublishedSales(desc, pubDate);
        dbManager.close();
        return rides;
    }

    @WebMethod
    public byte[] downloadImage(String imageName) {
        File image = new File(basePath + imageName);
        try {
            return Files.readAllBytes(image.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        DataAccess dB4oManager = new DataAccess();
        dB4oManager.close();
    }

    @WebMethod
    public void initializeBD() {
        dbManager.open();
        dbManager.initializeDB();
        dbManager.close();
    }

    @WebMethod
    public String isLoginGetEmail(String email, String password) {
        dbManager.open();
        Erabiltzailea res = dbManager.isLogin(email, password);
        dbManager.close();
        if (res == null) return null;
        return res.getEmail();
    }

    @WebMethod
    public boolean isAdmin(String email) {
        dbManager.open();
        Erabiltzailea res = dbManager.getUser(email);
        dbManager.close();
        return res instanceof Admin;
    }

    @WebMethod
    public boolean isLoggedIn(String email) {
        dbManager.open();
        Erabiltzailea res = dbManager.isLogin(email);
        dbManager.close();
        return res != null;
    }

    @Override
    public void addUser(String email, String password, String name, String telefonoa) {
        dbManager.open();
        dbManager.addUser(email, password, name, telefonoa);
        dbManager.close();
    }

    @Override
    public double getDiruKop(String zenb) {
        dbManager.open();
        double res = dbManager.getDiruKop(zenb);
        dbManager.close();
        return res;
    }

    @Override
    public void updateDiruKop(String zenb, double diruKop) {
        dbManager.open();
        dbManager.updateDiruKop(zenb, diruKop);
        dbManager.close();
    }

    @Override
    public void updateEgoera(Integer saleNumber, String egoera) {
        dbManager.open();
        Sale sale = dbManager.getSale(saleNumber);
        dbManager.updateEgoera(sale, egoera);
        dbManager.close();
    }

    @Override
    public String getFirstAccountNumber(String email) {
        dbManager.open();
        String res = dbManager.getFirstAccountNumber(email);
        dbManager.close();
        return res;
    }

    @Override
    public void createErreklamazio(String titulua, String deskripzioa, byte[] fileContent, String fileName, Integer saleNumber) {
        File file = null;
        if (fileContent != null && fileName != null) {
            try {
                file = new File(basePath + fileName);
                Files.write(file.toPath(), fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dbManager.open();
        Sale sale = dbManager.getSale(saleNumber);
        dbManager.createErreklamazio(titulua, deskripzioa, file, sale);
        dbManager.close();
    }

    @Override
    public void updateEgoeraErreklamazioa(Integer erreklamazioId, String egoera) {
        dbManager.open();
        dbManager.updateEgoeraErreklamazioaById(erreklamazioId, egoera);
        dbManager.close();
    }

    @Override
    public List<Erreklamazioa> getErreklamazioakByEgoera(String egoera) {
        dbManager.open();
        List<Erreklamazioa> res = dbManager.getErreklamazioakByEgoera(egoera);
        dbManager.close();
        return res;
    }

    @Override
    public void addMugimenduak(float diruKop, Date data, String productName, String mota, String kontuZenb) {
        dbManager.open();
        dbManager.addMugimenduak(diruKop, data, productName, mota, kontuZenb);
        dbManager.close();
    }

    @Override
    public void createEskaera(String productName, String userMail) {
        dbManager.open();
        dbManager.createEskaera(productName, userMail);
        dbManager.close();
    }

    @Override
    public List<Eskaera> getAllEskaerak() {
        dbManager.open();
        List<Eskaera> res = dbManager.getAllEskaerak();
        dbManager.close();
        return res;
    }

    @Override
    public void createOferta(String title, String description, double price, String userEmail, Integer eskaeraId) {
        dbManager.open();
        User user = (User) dbManager.getUser(userEmail);
        Eskaera eskaera = dbManager.getEskaera(eskaeraId);
        dbManager.createOferta(title, description, price, user, eskaera);
        dbManager.close();
    }

    @Override
    public void removeOfertaAndEskaera(Integer ofertaId) {
        dbManager.open();
        dbManager.removeOfertaAndEskaera(ofertaId);
        dbManager.close();
    }

    @WebMethod
    public void addProduktuaSaskira(Integer saleNumber, int i, String userMail) {
        dbManager.open();
        Sale sale = dbManager.getSale(saleNumber);
        dbManager.addProduktuaSaskira(sale, i, userMail);
        dbManager.close();
    }

    @Override
    public double deskontuaAplikatu(double prezioa, int num) {
        dbManager.open();
        double res = dbManager.deskontuaAplikatu(prezioa, num);
        dbManager.close();
        return res;
    }

    @WebMethod
    public void removeProduktuaSaskitik(Integer saleNumber, int i, String userMail) {
        dbManager.open();
        Sale sale = dbManager.getSale(saleNumber);
        dbManager.removeProduktuaSaskitik(sale, i, userMail);
        dbManager.close();
    }

    @Override
    public void clearSaskia(int i, String userMail) {
        dbManager.open();
        dbManager.clearSaskia(i, userMail);
        dbManager.close();
    }

    @WebMethod
    public List<Sale> getSaskiaSales(String userMail, int saskiaIndex) {
        dbManager.open();
        List<Sale> res = dbManager.getSaskiaSales(userMail, saskiaIndex);
        dbManager.close();
        return res;
    }

    @WebMethod
    public double getSaskiaTotalPrice(String userMail, int saskiaIndex) {
        dbManager.open();
        double res = dbManager.getSaskiaTotalPrice(userMail, saskiaIndex);
        dbManager.close();
        return res;
    }

    @WebMethod
    public void buyProduct(Integer saleNumber, String email) {
        dbManager.open();
        Sale sale = dbManager.getSale(saleNumber);
        dbManager.buyProduct(sale, email);
        dbManager.close();
    }

    @WebMethod
    public List<String> getUserKontuak(String userMail) {
        dbManager.open();
        List<String> kontuak = dbManager.getUserKontuZenbakiak(userMail);
        dbManager.close();
        return kontuak;
    }

    @WebMethod
    public List<Sale> getErositakoak(String userMail) {
        dbManager.open();
        List<Sale> res = dbManager.getErositakoak(userMail);
        dbManager.close();
        return res;
    }

    @WebMethod
    public boolean hasErositakoak(String userMail) {
        dbManager.open();
        List<Sale> res = dbManager.getErositakoak(userMail);
        dbManager.close();
        return res != null && !res.isEmpty();
    }

    @WebMethod
    public boolean hasErreklamazioak(String userMail) {
        dbManager.open();
        boolean res = dbManager.hasErreklamazioak(userMail);
        dbManager.close();
        return res;
    }

    @WebMethod
    public void createBalorazioa(String balorazioa, int puntuazioa, Integer saleNumber) {
        dbManager.open();
        Sale sale = dbManager.getSale(saleNumber);
        if (sale != null && sale.getSeller() != null) {
            dbManager.createBalorazioa(balorazioa, puntuazioa, sale.getSeller(), sale);
        }
        dbManager.close();
    }

    @WebMethod
    public List<Mugimendua> getMugimenduak(String userMail) {
        dbManager.open();
        List<Mugimendua> res = dbManager.getMugimenduak(userMail);
        dbManager.close();
        return res;
    }

    @WebMethod
    public List<Eskaera> getEskaerakByUser(String userMail) {
        dbManager.open();
        List<Eskaera> res = dbManager.getEskaerakByUser(userMail);
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public List<BalorazioProfila> getBalorazioakByUser(String userMail) {
        dbManager.open();
        List<BalorazioProfila> res = dbManager.getBalorazioakByUser(userMail);
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public List<Erreklamazioa> getErreklamazioakByUser(String userMail) {
        dbManager.open();
        List<Erreklamazioa> res = dbManager.getErreklamazioakByUser(userMail);
        dbManager.close();
        return res;
    }
}