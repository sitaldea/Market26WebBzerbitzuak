package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({User.class, Admin.class})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Erabiltzailea implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlID
    @Id
    private String email;
    private String password;

    public Erabiltzailea() {
        super();
    }

    public Erabiltzailea(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}