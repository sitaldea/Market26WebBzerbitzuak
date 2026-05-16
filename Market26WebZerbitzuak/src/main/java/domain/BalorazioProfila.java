package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class BalorazioProfila implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlID
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @Id
    @GeneratedValue
    private Integer id;

    private String balorazioa;
    private int puntuazioa;

    @XmlIDREF
    private User user;

    @XmlIDREF
    @OneToOne
    private Sale sale;

    public BalorazioProfila() { super(); }

    public BalorazioProfila(String balorazioa, int puntuazioa, User user, Sale sale) {
        super();
        this.balorazioa = balorazioa;
        this.puntuazioa = puntuazioa;
        this.user = user;
        this.sale = sale;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBalorazioa() { return balorazioa; }
    public void setBalorazioa(String balorazioa) { this.balorazioa = balorazioa; }

    public int getPuntuazioa() { return puntuazioa; }
    public void setPuntuazioa(int puntuazioa) { this.puntuazioa = puntuazioa; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }
}