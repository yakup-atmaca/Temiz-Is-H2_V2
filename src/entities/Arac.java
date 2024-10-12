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
public class Arac implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer aracId;

    private String plaka;

    private int disRenkId;

    private int icRenkId;

    private String aciklama;

    private List<Hizmet> hizmetList;

    private int aracDurumId;

    private int modelId;

    private int musteriId;
    
    private int aracKategoriId;
    
    

    public Arac() {
    }

    public Arac(Integer aracId) {
        this.aracId = aracId;
    }

    public Arac(Integer aracId, String plaka, int disRenkId, int icRenkId) {
        this.aracId = aracId;
        this.plaka = plaka;
        this.disRenkId = disRenkId;
        this.icRenkId = icRenkId;
    }

    public Integer getAracId() {
        return aracId;
    }

    public void setAracId(Integer aracId) {
        this.aracId = aracId;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public int getDisRenkId() {
        return disRenkId;
    }

    public void setDisRenkId(int disRenkId) {
        this.disRenkId = disRenkId;
    }

    public int getIcRenkId() {
        return icRenkId;
    }

    public void setIcRenkId(int icRenkId) {
        this.icRenkId = icRenkId;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public List<Hizmet> getHizmetList() {
        return hizmetList;
    }

    public void setHizmetList(List<Hizmet> hizmetList) {
        this.hizmetList = hizmetList;
    }

    public int getAracDurumId() {
        return aracDurumId;
    }

    public void setAracDurumId(int aracDurumId) {
        this.aracDurumId = aracDurumId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getMusteriId() {
        return musteriId;
    }

    public void setMusteriId(int musteriId) {
        this.musteriId = musteriId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aracId != null ? aracId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Arac)) {
            return false;
        }
        Arac other = (Arac) object;
        if ((this.aracId == null && other.aracId != null) || (this.aracId != null && !this.aracId.equals(other.aracId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Arac[ aracId=" + aracId + " ]";
    }

    public int getAracKategoriId() {
        return aracKategoriId;
    }

    public void setAracKategoriId(int aracKategoriId) {
        this.aracKategoriId = aracKategoriId;
    }

}
