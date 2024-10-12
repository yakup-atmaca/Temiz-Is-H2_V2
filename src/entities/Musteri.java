package entities;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Yakup
 */
public class Musteri implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer musteriId;

    private String musteriAdi;

    private String telefonNo;
 
    private String aciklama;

    private List<Arac> aracList;

    public Musteri() {
    }

    public Musteri(Integer musteriId) {
        this.musteriId = musteriId;
    }

    public Musteri(Integer musteriId, String musteriAdi, String musteriTelefon1) {
        this.musteriId = musteriId;
        this.musteriAdi = musteriAdi;
        this.telefonNo = musteriTelefon1;
    }

    public Integer getMusteriId() {
        return musteriId;
    }

    public void setMusteriId(Integer musteriId) {
        this.musteriId = musteriId;
    }

    public String getMusteriAdi() {
        return musteriAdi;
    }

    public void setMusteriAdi(String musteriAdi) {
        this.musteriAdi = musteriAdi;
    }

    public String getTelefonNo() {
        return telefonNo;
    }

    public void setTelefonNo(String telefonNo) {
        this.telefonNo = telefonNo;
    }

       public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public List<Arac> getAracList() {
        return aracList;
    }

    public void setAracList(List<Arac> aracList) {
        this.aracList = aracList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (musteriId != null ? musteriId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Musteri)) {
            return false;
        }
        Musteri other = (Musteri) object;
        if ((this.musteriId == null && other.musteriId != null) || (this.musteriId != null && !this.musteriId.equals(other.musteriId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Musteri[ musteriId=" + musteriId + " ]";
    }

}
