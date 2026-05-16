package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Admin extends Erabiltzailea implements Serializable {
    private static final long serialVersionUID = 1L;

    public Admin() { super(); }

    public Admin(String email, String password) { super(email, password); }
}