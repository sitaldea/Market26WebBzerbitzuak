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
public class Saskia implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlID
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @Id
    @GeneratedValue
    private Integer id;

    private double prezioTotala;

    @XmlIDREF
    private User user;

    @XmlIDREF
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Sale> pruduktuak = new ArrayList<Sale>();

    public Saskia() { super(); }

    public Saskia(double prezioTotala, User user) {
        super();
        this.prezioTotala = prezioTotala;
        this.user = user;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public double getPrezioTotala() { return prezioTotala; }
    public void setPrezioTotala(double prezioTotala) { this.prezioTotala = prezioTotala; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Sale> getPruduktuak() { return pruduktuak; }
    public void setPruduktuak(List<Sale> pruduktuak) { this.pruduktuak = pruduktuak; }
}