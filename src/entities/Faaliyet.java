/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Yakup
 */
public class Faaliyet implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer faaliyetId;
    private String faaliyetAdi;

    private int puan;

    private List<Hizmet> hizmetList;

    public Faaliyet() {
    }

    public Faaliyet(Integer faaliyetId) {
        this.faaliyetId = faaliyetId;
    }

    public Faaliyet(Integer faaliyetId, String faaliyetAdi) {
        this.faaliyetId = faaliyetId;
        this.faaliyetAdi = faaliyetAdi;
    }

    public Integer getFaaliyetId() {
        return faaliyetId;
    }

    public void setFaaliyetId(Integer faaliyetId) {
        this.faaliyetId = faaliyetId;
    }

    public String getFaaliyetAdi() {
        return faaliyetAdi;
    }

    public void setFaaliyetAdi(String faaliyetAdi) {
        this.faaliyetAdi = faaliyetAdi;
    }

    public List<Hizmet> getHizmetList() {
        return hizmetList;
    }

    public void setHizmetList(List<Hizmet> hizmetList) {
        this.hizmetList = hizmetList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faaliyetId != null ? faaliyetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Faaliyet)) {
            return false;
        }
        Faaliyet other = (Faaliyet) object;
        if ((this.faaliyetId == null && other.faaliyetId != null) || (this.faaliyetId != null && !this.faaliyetId.equals(other.faaliyetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Faaliyet[ faaliyetId=" + faaliyetId + " ]";
    }

    public int getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

}
