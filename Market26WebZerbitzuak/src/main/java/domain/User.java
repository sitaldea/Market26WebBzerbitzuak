package domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class User extends Erabiltzailea implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String telefonoa;
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Sale> sales=new ArrayList<Sale>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Sale> erositakoak=new ArrayList<Sale>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<DiruKontua> kontuak=new ArrayList<DiruKontua>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Erreklamazioa> erreklamazioak=new ArrayList<Erreklamazioa>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Mugimendua> mugimenduak=new ArrayList<Mugimendua>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Oferta> ofertak=new ArrayList<Oferta>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Eskaera> eskaerak=new ArrayList<Eskaera>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Saskia> saskiak=new ArrayList<Saskia>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<BalorazioProfila> balorazioak=new ArrayList<BalorazioProfila>();

	public User() {
		super();
	}

	public User(String email, String name, String password, String telefonoa) {
		super(email, password);
		this.name = name;
		this.telefonoa = telefonoa;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String toString(){
		return getEmail()+";"+name+sales;
	}
	
	/**
	 * This method creates/adds a sale to a seller
	 * 
	 * @param title of the sale
	 * @param description of the sale
	 * @param status 
	 * @param selling price
	 * @param publicationDate
	 * @return Sale
	 */
	
	


	public Sale addSale(String title, String description, int status, float price,  Date pubDate, File file, String egoera)  {
		
		Sale sale=new Sale(title, description, status, price,  pubDate, file, this, egoera);
        sales.add(sale);
        return sale;
	}
	
	public void addErositakoa(Sale sale) {
		erositakoak.add(sale);
	}
	
	public void addDiruKontua(String kontuZenb, double diruKop) {
		DiruKontua kontu=new DiruKontua(kontuZenb, diruKop, this);
		kontuak.add(kontu);
	}
	
	
	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesSaleExist(String title)  {	
		for (Sale s:sales)
			if ( s.getTitle().compareTo(title)==0 )
			 return true;
		return false;
	}
		
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    User other = (User) obj;
	    return getEmail() != null && getEmail().equals(other.getEmail());
	}

	@Override
	public int hashCode() {
	    return getEmail() != null ? getEmail().hashCode() : 0;
	}


	public String getTelefonoa() {
		return telefonoa;
	}

	public void setTelefonoa(String telefonoa) {
		this.telefonoa = telefonoa;
	}

	public List<Sale> getSales() {
		return sales;
	}
	
	public List<Sale> getErositakoak() {
		return erositakoak;
	}
	
	public List<DiruKontua> getKontuak() {
		return kontuak;
	}
	
	public List<Erreklamazioa> getErreklamazioak() {
		return erreklamazioak;
	}
	
	public List<Mugimendua> getMugimenduak() {
		return mugimenduak;
	}
	
	public List<Oferta> getOfertak() {
		return ofertak;
	}
	
	public List<Eskaera> getEskaerak() {
		return eskaerak;
	}
	
	public List<Saskia> getSaskiak() {
		return saskiak;
	}
	
	public List<BalorazioProfila> getBalorazioak() {
		return balorazioak;
	}
	
	public BalorazioProfila addBalorazioa(String balorazioa, int puntuazioa, User user, Sale sale) {
		BalorazioProfila balorazio=new BalorazioProfila(balorazioa, puntuazioa, user, sale);
		balorazioak.add(balorazio);
		return balorazio;
	}

	public void addEskaera(String eskaera) {
		Eskaera eska=new Eskaera(eskaera, this);
		eskaerak.add(eska);		
	}	
	
	public void addProduktuaSaskira(Sale sale, int i) {
        if (saskiak == null) {
            saskiak = new ArrayList<>();
        }
        while (saskiak.size() <= i) {
            Saskia berria = new Saskia();
            berria.setPruduktuak(new ArrayList<>());
            berria.setUser(this);
            berria.setPrezioTotala(0.0);
            saskiak.add(berria);
        }
        Saskia target = saskiak.get(i);
        if (target.getPruduktuak() == null) {
            target.setPruduktuak(new ArrayList<>());
        }
        if (!target.getPruduktuak().isEmpty()) {
            Sale first = target.getPruduktuak().get(0);
            if (first != null && first.getSeller() != null && sale != null && sale.getSeller() != null) {
                String existingSeller = first.getSeller().getEmail();
                String newSeller = sale.getSeller().getEmail();
                if (existingSeller != null && newSeller != null && !existingSeller.equals(newSeller)) {
                    throw new IllegalArgumentException("\r\n"
                    		+ "Saltzaile beraren produktuak bakarrik gehi daitezke\r\n" + "");
                }
            }
        }
        if (!target.getPruduktuak().contains(sale)) {
            target.getPruduktuak().add(sale);
            double current = target.getPrezioTotala();
            target.setPrezioTotala(current + sale.getPrice());
        }
    }
    
    public void removeProduktuaSaskitik(Sale sale, int i) {
        if (saskiak != null && saskiak.size() > i) {
            Saskia target = saskiak.get(i);
            if (target != null && target.getPruduktuak() != null) {
                if (target.getPruduktuak().remove(sale)) {
                    double current = target.getPrezioTotala();
                    target.setPrezioTotala(current - sale.getPrice());
                }
            }
        }
    }
    
    public void clearSaskia(int i) {
        if (saskiak != null && saskiak.size() > i) {
            Saskia target = saskiak.get(i);
            if (target != null) {
                // Desplazar todas las saskias desde la posición i hacia adelante
                saskiak.add(i + 1, target);
                
                // Crear una nueva saskia vacía en la posición i
                Saskia berria = new Saskia();
                berria.setPruduktuak(new ArrayList<>());
                berria.setUser(this);
                berria.setPrezioTotala(0.0);
                saskiak.set(i, berria);
            }
        }
    }

	public void setSaskiak(ArrayList arrayList) {
		this.saskiak = arrayList;
	}
}