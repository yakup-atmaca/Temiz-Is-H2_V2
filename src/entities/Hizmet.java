/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.sql.Date;




/**
 *
 * @author Yakup
 */
public class Hizmet implements Serializable {

    private static final long serialVersionUID = 1L;
  
    private Integer hizmetId;

    private float puan;

    private Date hizmetTarihi;

    private Date kayitGirisTarihi;

    private Date kayitGuncellemeTarihi;

    private String aciklama;

    private int aracId;

    private int faaliyetId;
    private int ucret;

    public Hizmet() {
    }

    public Hizmet(Integer hizmetId) {
        this.hizmetId = hizmetId;
    }

    public Hizmet(Integer hizmetId, float puan, Date hizmetTarihi, Date kayitGirisTarihi) {
        this.hizmetId = hizmetId;
        this.puan = puan;
        this.hizmetTarihi = hizmetTarihi;
        this.kayitGirisTarihi = kayitGirisTarihi;
    }

    public Integer getHizmetId() {
        return hizmetId;
    }

    public void setHizmetId(Integer hizmetId) {
        this.hizmetId = hizmetId;
    }

    public float getPuan() {
        return puan;
    }

    public void setPuan(float puan) {
        this.puan = puan;
    }

    public Date getHizmetTarihi() {
        return hizmetTarihi;
    }

    public void setHizmetTarihi(Date hizmetTarihi) {
        this.hizmetTarihi = hizmetTarihi;
    }

    public Date getKayitGirisTarihi() {
        return kayitGirisTarihi;
    }

    public void setKayitGirisTarihi(Date kayitGirisTarihi) {
        this.kayitGirisTarihi = kayitGirisTarihi;
    }

    public Date getKayitGuncellemeTarihi() {
        return kayitGuncellemeTarihi;
    }

    public void setKayitGuncellemeTarihi(Date kayitGuncellemeTarihi) {
        this.kayitGuncellemeTarihi = kayitGuncellemeTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public int getAracId() {
        return aracId;
    }

    public void setAracId(int aracId) {
        this.aracId = aracId;
    }

    public int getFaaliyetId() {
        return faaliyetId;
    }

    public void setFaaliyetId(int faaliyetId) {
        this.faaliyetId = faaliyetId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hizmetId != null ? hizmetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hizmet)) {
            return false;
        }
        Hizmet other = (Hizmet) object;
        if ((this.hizmetId == null && other.hizmetId != null) || (this.hizmetId != null && !this.hizmetId.equals(other.hizmetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Hizmet[ hizmetId=" + hizmetId + " ]";
    }

    public int getUcret() {
        return ucret;
    }

    public void setUcret(int ucret) {
        this.ucret = ucret;
    }
    
}
