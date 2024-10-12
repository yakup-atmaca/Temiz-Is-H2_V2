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
public class AracKategori implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer aracKategoriId;

    private String aracKategoriAdi;
   
    private List<Arac> aracList;

    public AracKategori() {
    }

    public AracKategori(Integer aracKategoriId) {
        this.aracKategoriId = aracKategoriId;
    }

    public AracKategori(Integer aracKategoriId, String aracKategoriAdi) {
        this.aracKategoriId = aracKategoriId;
        this.aracKategoriAdi = aracKategoriAdi;
    }

    public Integer getAracKategoriId() {
        return aracKategoriId;
    }

    public void setAracKategoriId(Integer aracKategoriId) {
        this.aracKategoriId = aracKategoriId;
    }

    public String getAracKategoriAdi() {
        return aracKategoriAdi;
    }

    public void setAracKategoriAdi(String aracKategoriAdi) {
        this.aracKategoriAdi = aracKategoriAdi;
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
        hash += (aracKategoriId != null ? aracKategoriId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AracKategori)) {
            return false;
        }
        AracKategori other = (AracKategori) object;
        if ((this.aracKategoriId == null && other.aracKategoriId != null) || (this.aracKategoriId != null && !this.aracKategoriId.equals(other.aracKategoriId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AracKategori[ aracKategoriId=" + aracKategoriId + " ]";
    }

}
