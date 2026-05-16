package businessLogic;

import java.util.Date;
import java.util.List;

import domain.BalorazioProfila;
import domain.Erabiltzailea;
import domain.Sale;
import domain.Erreklamazioa;
import domain.Eskaera;
import domain.Mugimendua;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface BLFacade {

	@WebMethod
	public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, byte[] fileContent, String fileName, String egoera)
			throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;

	@WebMethod public List<Sale> getSales(String desc);

	@WebMethod public List<Sale> getPublishedSales(String desc, Date pubDate);

	@WebMethod public void initializeBD();

	@WebMethod public String isLoginGetEmail(String email, String password);

	@WebMethod public boolean isAdmin(String email);

	@WebMethod public boolean isLoggedIn(String email);

	@WebMethod public void addUser(String email, String password, String name, String telefonoa);

	@WebMethod public byte[] downloadImage(String imageName);

	@WebMethod public double getDiruKop(String zenb);

	@WebMethod public void updateDiruKop(String zenb, double diruKop);

	@WebMethod public void close();

	@WebMethod public void updateEgoera(Integer saleNumber, String egoera);

	@WebMethod public String getFirstAccountNumber(String email);

	@WebMethod public void createErreklamazio(String titulua, String deskripzioa, byte[] fileContent, String fileName, Integer saleNumber);

	@WebMethod public void updateEgoeraErreklamazioa(Integer erreklamazioId, String egoera);

	@WebMethod public List<Erreklamazioa> getErreklamazioakByEgoera(String egoera);

	@WebMethod public void addMugimenduak(float diruKop, Date data, String productName, String mota, String kontuZenb);

	@WebMethod public void createEskaera(String productName, String userMail);

	@WebMethod public List<Eskaera> getAllEskaerak();

	@WebMethod public void createOferta(String title, String description, double price, String userEmail, Integer eskaeraId);

	@WebMethod public void removeOfertaAndEskaera(Integer ofertaId);

	@WebMethod public void addProduktuaSaskira(Integer saleNumber, int i, String userMail);

	@WebMethod public double deskontuaAplikatu(double prezioa, int num);

	@WebMethod public void removeProduktuaSaskitik(Integer saleNumber, int i, String userMail);

	@WebMethod public void clearSaskia(int i, String userMail);

	@WebMethod public List<Sale> getSaskiaSales(String userMail, int saskiaIndex);

	@WebMethod public double getSaskiaTotalPrice(String userMail, int saskiaIndex);

	@WebMethod public void buyProduct(Integer saleNumber, String email);

	@WebMethod public List<String> getUserKontuak(String userMail);

	@WebMethod public List<Sale> getErositakoak(String userMail);

	@WebMethod public boolean hasErositakoak(String userMail);

	@WebMethod public boolean hasErreklamazioak(String userMail);

	@WebMethod public void createBalorazioa(String balorazioa, int puntuazioa, Integer saleNumber);

	@WebMethod public List<Mugimendua> getMugimenduak(String userMail);

	@WebMethod public List<Eskaera> getEskaerakByUser(String userMail);
	
	@WebMethod public List<BalorazioProfila> getBalorazioakByUser(String userMail);
	
	@WebMethod public List<Erreklamazioa> getErreklamazioakByUser(String userMail);
}