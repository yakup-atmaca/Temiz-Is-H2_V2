package dao;

import entities.Arac;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AracDAO {

    DataSource ds;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    public void connect() throws IOException, SQLException, Exception {
        MyDataSource myDataSource = new MyDataSource();
        ds = myDataSource.getH2DataSource();
        conn = ds.getConnection();
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

    public void aracEkle(Arac arac) throws SQLException, ClassNotFoundException {

        try {

            //Aynı plakalı araç aktif olarak var ise işlemi yapma
            boolean plakaAktifMi = false;
            if (arac.getAracDurumId() == 1) {
                String query = "select * from arac where arac_durum_id= 1 and plaka='" + arac.getPlaka() + "'";

                connect();
                pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    plakaAktifMi = true;
                }
            }
            //////////
            if (plakaAktifMi == false) {

                pstmt = conn
                        .prepareStatement("INSERT INTO arac(plaka,musteri_id,arac_model_id,dis_renk_id,ic_renk_id,arac_durum_id,aciklama)\n"
                                + "VALUES(?,?,?,?,?,?,?)");

                pstmt.setString(1, arac.getPlaka());
                pstmt.setInt(2, arac.getMusteriId());
                pstmt.setInt(3, arac.getModelId());
                pstmt.setInt(4, arac.getDisRenkId());
                pstmt.setInt(5, arac.getIcRenkId());
                pstmt.setInt(6, arac.getAracDurumId());
                pstmt.setString(7, arac.getAciklama());
                pstmt.executeUpdate();

            } else {
                String msg = "";

                msg = "Bu plakalı araç 'AKTİF' olarak başka bir müşteri üzerinde görünüyor. Plakayı doğru yazdığınızdan eminseniz aynı plakalı diğer aracın durumunu 'PASİF' hale getiriniz!";

                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();

        }
    }

    public void aracGuncelle(Arac arac) throws SQLException, ClassNotFoundException, IOException {
        try {
            connect();
            boolean plakaAktifMi = false;
            //Aynı plakalı araç aktif olarak var ise işlemi yapma
            if (arac.getAracDurumId() == 1) {
                String query = "select * from arac where arac_durum_id= 1 and plaka='" + arac.getPlaka() + "' and musteri_id !=" + arac.getMusteriId();

                connect();
                pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    plakaAktifMi = true;
                }
            }
            //////////
            if (plakaAktifMi == false) {
                pstmt = conn
                        .prepareStatement("UPDATE arac SET plaka = ?, musteri_id = ?, arac_model_id = ?, dis_renk_id = ?,\n"
                                + "ic_renk_id = ?, arac_durum_id = ?, aciklama = ? WHERE arac_id = ?");

                pstmt.setString(1, arac.getPlaka());
                pstmt.setInt(2, arac.getMusteriId());
                pstmt.setInt(3, arac.getModelId());
                pstmt.setInt(4, arac.getDisRenkId());
                pstmt.setInt(5, arac.getIcRenkId());
                pstmt.setInt(6, arac.getAracDurumId());
                pstmt.setString(7, arac.getAciklama());
                pstmt.setInt(8, arac.getAracId());
                pstmt.executeUpdate();

            } else {
                String msg = "";

                msg = "Bu plakalı araç 'AKTİF' olarak başka bir müşteri üzerinde görünüyor. Plakayı doğru yazdığınızdan eminseniz aynı plakalı diğer aracın durumunu 'PASİF' hale getiriniz!";

                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
            close();
        } catch (Exception ex) {
            close();
            if (ex.getMessage().contains("Duplicate")) {
                String msg = "";

                msg = "Bu plakalı araç veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void aracSil(Arac arac) throws SQLException, ClassNotFoundException, IOException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("delete from arac where arac_id = ?");

            pstmt.setInt(1, arac.getAracId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
            if (e.getMessage().contains("foreign key")) {
                String msg = "Bu aracı silmek için önce Hizmet Ekranı'nda araca ait hizmetleri silmelisiniz! Araç üzerinde hizmet kalmadıysa aracı silebilirsiniz!";
                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void fillTable(JTable table, String str) throws ClassNotFoundException, IOException, SQLException, Exception {
        try {
            String query = "SELECT rownum() as sira_no, a.musteri_id,m.musteri_adi,a.arac_id,a.plaka, mr.arac_marka_adi, k.arac_model_adi, r1.renk_adi as dis_renk_adi,\n"
                    + " r2.renk_adi as ic_renk_adi, d.arac_durum_adi, a.aciklama, k.arac_model_id, ak.arac_kategori_adi\n"
                    + "FROM arac a, musteri m, arac_model k, renk r1, renk r2, arac_durum d, arac_marka mr, arac_kategori ak \n"
                    + "where a.musteri_id = m.musteri_id\n"
                    + "and a.arac_model_id = k.arac_model_id\n"
                    + "and k.arac_kategori_id = ak.arac_kategori_id\n"
                    + "and mr.arac_marka_id = k.arac_marka_id\n"
                    + "and a.dis_renk_id = r1.renk_id\n"
                    + "and a.ic_renk_id = r2.renk_id\n"
                    + "and a.arac_durum_id = d.arac_durum_id\n"
                    + "and a.plaka like  '" + str + "%'  \n"
                    + "order by a.arac_id desc";
                    

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
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
    }

    public void fillTable(JTable table, int musteriId) throws ClassNotFoundException, IOException, SQLException {
        try {
            String query = "SELECT rownum() as sira_no, a.musteri_id,m.musteri_adi, a.arac_id,a.plaka, mr.arac_marka_adi, k.arac_model_adi, r1.renk_adi as dis_renk_adi,\n"
                    + " r2.renk_adi as ic_renk_adi,  d.arac_durum_adi, a.aciklama, k.arac_model_id, ak.arac_kategori_adi\n"
                    + "FROM arac a, musteri m, arac_model k, renk r1, renk r2, arac_durum d, arac_marka mr, arac_kategori ak \n"
                    + "where a.musteri_id = m.musteri_id\n"
                    + "and a.arac_model_id = k.arac_model_id\n"
                    + "and k.arac_kategori_id = ak.arac_kategori_id\n"
                    + "and mr.arac_marka_id = k.arac_marka_id\n"
                    + "and a.dis_renk_id = r1.renk_id\n"
                    + "and a.ic_renk_id = r2.renk_id\n"
                    + "and a.arac_durum_id = d.arac_durum_id\n"
                    + "and a.musteri_id =  " + musteriId ;

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

    public Arac getArac(String plaka) throws IOException {
        Arac arac = new Arac();
        try {
            String query = "SELECT arac_id, plaka, musteri_id, a.arac_model_id, m.arac_kategori_id\n"
                    + "FROM arac a, arac_model m where a.arac_model_id = m.arac_model_id \n"
                    + "and a.plaka like  '" + plaka + "%'  \n"
                    + "order by a.arac_id desc";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    arac.setAracId(rs.getInt("arac_id"));
                    arac.setMusteriId(rs.getInt("musteri_id"));
                    arac.setModelId(rs.getInt("arac_model_id"));
                    arac.setAracKategoriId(rs.getInt("arac_kategori_id"));
                }
            }
            close();
            return arac;
        } catch (Exception e) {
            close();
            e.printStackTrace();
            return null;
        }
    }
}
