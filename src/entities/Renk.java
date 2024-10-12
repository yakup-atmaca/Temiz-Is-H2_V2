/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;


/**
 *
 * @author Yakup
 */

public class Renk implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer renkId;

    private String renkAdi;

    public Renk() {
    }

    public Renk(Integer renkId) {
        this.renkId = renkId;
    }

    public Renk(Integer renkId, String renkAdi) {
        this.renkId = renkId;
        this.renkAdi = renkAdi;
    }

    public Integer getRenkId() {
        return renkId;
    }

    public void setRenkId(Integer renkId) {
        this.renkId = renkId;
    }

    public String getRenkAdi() {
        return renkAdi;
    }

    public void setRenkAdi(String renkAdi) {
        this.renkAdi = renkAdi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (renkId != null ? renkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Renk)) {
            return false;
        }
        Renk other = (Renk) object;
        if ((this.renkId == null && other.renkId != null) || (this.renkId != null && !this.renkId.equals(other.renkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Renk[ renkId=" + renkId + " ]";
    }
    
}
