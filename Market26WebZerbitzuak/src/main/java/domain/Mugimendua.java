package domain;

import java.io.Serializable;
import java.util.Date;
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
public class Mugimendua implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlID
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @Id
    @GeneratedValue
    private Integer id;

    private float diruKop;
    private Date data;
    private String productName;
    private String mota;

	@XmlIDREF
    private DiruKontua kontua;
	
	private String kontuZenb;

	public String getKontuZenb() { return kontuZenb; }
	public void setKontuZenb(String kontuZenb) { this.kontuZenb = kontuZenb; }
	
    public Mugimendua() { super(); }

    public Mugimendua(float diruKop, Date data, String productName, String mota) {
        super();
        this.diruKop = diruKop;
        this.data = data;
        this.productName = productName;
        this.mota = mota;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public float getDiruKop() { return diruKop; }
    public void setDiruKop(float diruKop) { this.diruKop = diruKop; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getMota() { return mota; }
    public void setMota(String mota) { this.mota = mota; }

    public DiruKontua getKontua() { return kontua; }
    public void setKontua(DiruKontua kontua) { this.kontua = kontua; }
}