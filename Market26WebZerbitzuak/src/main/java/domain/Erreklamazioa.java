package domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Erreklamazioa implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlID
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @Id
    @GeneratedValue
    private Integer erreklamazioId;

    private String izenburua;
    private String deskripzioa;
    private String irudia;
    private String egoera;

    @XmlIDREF
    @OneToOne
    private Sale sale;

    @XmlIDREF
    private User user;

    public Erreklamazioa() { super(); }

    public Erreklamazioa(String izenburua, String deskripzioa, String irudia, Sale sale, String egoera) {
        super();
        this.izenburua = izenburua;
        this.deskripzioa = deskripzioa;
        this.irudia = irudia;
        this.egoera = egoera;
        this.sale = sale;
    }

    public Integer getErreklamazioId() { return erreklamazioId; }
    public void setErreklamazioId(Integer erreklamazioId) { this.erreklamazioId = erreklamazioId; }

    public String getIzenburua() { return izenburua; }
    public void setIzenburua(String izenburua) { this.izenburua = izenburua; }

    public String getDeskripzioa() { return deskripzioa; }
    public void setDeskripzioa(String deskripzioa) { this.deskripzioa = deskripzioa; }

    public String getIrudia() { return irudia; }
    public void setIrudia(String irudia) { this.irudia = irudia; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getEgoera() { return egoera; }
    public void setEgoera(String egoera) { this.egoera = egoera; }
}