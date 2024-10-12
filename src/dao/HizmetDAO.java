package dao;

import entities.Hizmet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HizmetDAO {

    DataSource ds;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    public void connect() throws IOException, SQLException {
        try {
            MyDataSource myDataSource = new MyDataSource();
            ds = myDataSource.getH2DataSource();
            conn = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hizmetEkle(Hizmet hizmet) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("INSERT INTO hizmet(arac_id,faaliyet_id,puan,hizmet_tarihi,aciklama, ucret)\n"
                            + "VALUES(?,?,?,?,?,?)");

            pstmt.setInt(1, hizmet.getAracId());
            pstmt.setInt(2, hizmet.getFaaliyetId());
            pstmt.setFloat(3, hizmet.getPuan());
            pstmt.setDate(4, (java.sql.Date) hizmet.getHizmetTarihi());
            pstmt.setString(5, hizmet.getAciklama());
            pstmt.setInt(6, hizmet.getUcret());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void hizmetGuncelle(Hizmet hizmet) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("UPDATE hizmet SET faaliyet_id = ?,puan = ?,hizmet_tarihi =?,"
                            + "kayit_guncelleme_tarihi = CURRENT_TIMESTAMP(), aciklama = ?, ucret=? \n"
                            + "WHERE hizmet_id = ?");

            pstmt.setInt(1, hizmet.getFaaliyetId());
            pstmt.setFloat(2, hizmet.getPuan());
            pstmt.setDate(3, (java.sql.Date) hizmet.getHizmetTarihi());
            pstmt.setString(4, hizmet.getAciklama());
            pstmt.setInt(5, hizmet.getUcret());
            pstmt.setInt(6, hizmet.getHizmetId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void hizmetSil(Hizmet hizmet) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("delete from hizmet where hizmet_id = ?");

            pstmt.setInt(1, hizmet.getHizmetId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void aracBul(JTable table, String str) throws ClassNotFoundException {
        try {
            String query = "select rownum() as sira_no, m.musteri_id, m.musteri_adi, a.arac_id, a.plaka, k.arac_kategori_adi, d.arac_kategori_id\n"
                    + "from musteri m, arac a, arac_model d, arac_kategori k\n"
                    + "where a.musteri_id = m.musteri_id\n"
                    + "and a.arac_model_id = d.arac_model_id\n"
                    + "and d.arac_kategori_id = k.arac_kategori_id\n"
                    + "and a.arac_durum_id = 1\n"
                    + "and (m.musteri_adi like '" + str + "%'  or a.plaka like  '" + str + "%' ) ";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            //To remove previously added rows
            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void aracBulAktifPasif(JTable table, String str) throws ClassNotFoundException, IOException {
        try {
            String query = "select rownum() as sira_no, m.musteri_id, m.musteri_adi, a.arac_id, a.plaka\n"
                    + "from musteri m, arac a\n"
                    + "where a.musteri_id = m.musteri_id\n"
                    + "and (m.musteri_adi like '" + str + "%'  or a.plaka like  '" + str + "%' ) ";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            //To remove previously added rows
            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void hizmetGetir(JTable table, int aracId) throws ClassNotFoundException, IOException {
        try {
            String query = "select rownum() as sira_no, h.hizmet_id, m.musteri_adi, a.plaka, f.faaliyet_adi, h.ucret, h.puan, "
                    + "TO_CHAR(hizmet_tarihi, 'DD/MM/YYYY') as hizmet_tarihi, h.aciklama\n"
                    + "from hizmet h, musteri m, arac a, faaliyet f\n"
                    + "where h.arac_id = a.arac_id\n"
                    + "and a.musteri_id = m.musteri_id\n"
                    + "and h.faaliyet_id = f.faaliyet_id\n"
                    + "and a.arac_durum_id = 1 \n"
                    + "and a.arac_id =  " + aracId + "  \n"
                    + "order by hizmet_id desc";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            //To remove previously added rows
            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void hizmetGetir(JTable table) throws ClassNotFoundException, IOException {
        try {
            String query = "select rownum() as sira_no,  h.hizmet_id, m.musteri_adi, a.plaka, f.faaliyet_adi, h.ucret, h.puan, TO_CHAR(hizmet_tarihi, 'DD/MM/YYYY') as hizmet_tarihi, h.aciklama\n"
                    + "from hizmet h, musteri m, arac a, faaliyet f\n"
                    + "where h.arac_id = a.arac_id\n"
                    + "and a.musteri_id = m.musteri_id\n"
                    + "and h.faaliyet_id = f.faaliyet_id\n"
                    + "and a.arac_durum_id = 1 \n"
                    + "order by hizmet_id desc LIMIT 0,100";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            //To remove previously added rows
            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void musteriBazliRapor(JTable table, int musteriId, String ilkTarih, String sonTarih) throws ClassNotFoundException, IOException {
        try {
            String query = "select rownum() as sira_no, m.musteri_adi, a.plaka, f.faaliyet_adi, h.ucret, h.puan, "
                    + " to_char(hizmet_tarihi,'dd/MM/yyyy'), h.aciklama\n"
                    + "from hizmet h, musteri m, arac a, faaliyet f\n"
                    + "where h.arac_id = a.arac_id\n"
                    + "and a.musteri_id = m.musteri_id\n"
                    + "and h.faaliyet_id = f.faaliyet_id\n"
                    + "and a.musteri_id =  " + musteriId + "  \n"
                    + "and hizmet_tarihi between '" + ilkTarih + "' and '" + sonTarih + "' \n"
                    + "order by hizmet_tarihi desc";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            //To remove previously added rows
            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void genelRapor(JTable table, String ilkTarih, String sonTarih) throws ClassNotFoundException, IOException {
        try {
            String query = "select rownum() as sira_no, m.musteri_adi, a.plaka, f.faaliyet_adi, h.ucret, h.puan, "
                    + " to_char(hizmet_tarihi,'dd/MM/yyyy'), h.aciklama\n"
                    + "from hizmet h, musteri m, arac a, faaliyet f\n"
                    + "where h.arac_id = a.arac_id\n"
                    + "and a.musteri_id = m.musteri_id\n"
                    + "and h.faaliyet_id = f.faaliyet_id\n"
                    + "and hizmet_tarihi between '" + ilkTarih + "' and '" + sonTarih + "' \n"
                    + "order by hizmet_tarihi desc";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            //To remove previously added rows
            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public int toplamPuanGetirByMusteriID(int musteriId) {
        int toplamPuan = 0;
        try {
            String query = "select sum(puan) as toplam_puan from hizmet h, arac a, musteri m\n"
                    + "where h.arac_id = a.arac_id\n"
                    + "and a.musteri_id = m.musteri_id\n"
                    + "and m.musteri_id = " + musteriId;

            connect();
            pstmt = conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                toplamPuan = rs.getInt("toplam_puan");
            }

            close();

            return toplamPuan;
        } catch (Exception e) {
            close();
            e.printStackTrace();
            return toplamPuan;
        }
    }

    public int toplamPuanGetirByAracID(int aracId) {
        int toplamPuan = 0;
        try {
            String query = "select sum(puan) as toplam_puan from hizmet h, arac a, musteri m\n"
                    + "where h.arac_id = a.arac_id\n"
                    + "and a.musteri_id = m.musteri_id   \n"
                    + "and  m.musteri_id = (select musteri_id from arac where arac_id =" + aracId + ") ";

            connect();
            pstmt = conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                toplamPuan = rs.getInt("toplam_puan");
            }

            close();
            return toplamPuan;
        } catch (Exception e) {
            close();
            e.printStackTrace();
            return toplamPuan;
        }
    }

    public int toplamGunlukKazanc() {
        int toplamPuan = 0;
        try {
            String query = "select sum(ucret) as toplam_gunluk_kazanc from hizmet where hizmet_tarihi = CURRENT_DATE";

            connect();
            pstmt = conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                toplamPuan = rs.getInt("toplam_gunluk_kazanc");
            }

            close();

            return toplamPuan;
        } catch (Exception e) {
            close();
            e.printStackTrace();
            return toplamPuan;
        }
    }
}
