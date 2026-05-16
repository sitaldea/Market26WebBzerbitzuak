package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Eskaera implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue
	private Integer id;
	private String productName;
	@XmlIDREF
	private User user;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Oferta> ofertak=new ArrayList<Oferta>();
	
	public Eskaera() {
		super();
	}
	
	public Eskaera(String productName, User user) {
		super();
		this.productName = productName;
		this.user = user;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public List<Oferta> getOfertak() {
		return ofertak;
	}
	
	public void setOfertak(List<Oferta> ofertak) {
		this.ofertak = ofertak;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void addOferta(String title, String description, double price, User user) {
		Oferta oferta=new Oferta(title, description, price, user, this);
		ofertak.add(oferta);
	}
}
