package domain;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Oferta implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlID
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @Id
    @GeneratedValue
    private Integer id;  // ← eremu berria

    private String title;
    private String description;
    private double price;
    private User user;
    @XmlIDREF
    private Eskaera eskaera;

    public Oferta() { super(); }

    public Oferta(String title, String description, double price, User user, Eskaera eskaera) {
        super();
        this.title = title;
        this.description = description;
        this.price = price;
        this.user = user;
        this.eskaera = eskaera;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Eskaera getEskaera() { return eskaera; }
    public void setEskaera(Eskaera eskaera) { this.eskaera = eskaera; }
}