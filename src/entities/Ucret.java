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
public class Ucret implements Serializable {

    private int ucret_id;
    private int arac_kategori_id;
    private int faaliyet_id;
    private int ucret;
    private String aciklama;

    public int getUcret_id() {
        return ucret_id;
    }

    public void setUcret_id(int ucret_id) {
        this.ucret_id = ucret_id;
    }

    public int getArac_kategori_id() {
        return arac_kategori_id;
    }

    public void setArac_kategori_id(int arac_kategori_id) {
        this.arac_kategori_id = arac_kategori_id;
    }

    public int getFaaliyet_id() {
        return faaliyet_id;
    }

    public void setFaaliyet_id(int faaliyet_id) {
        this.faaliyet_id = faaliyet_id;
    }

    public int getUcret() {
        return ucret;
    }

    public void setUcret(int ucret) {
        this.ucret = ucret;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }



}
