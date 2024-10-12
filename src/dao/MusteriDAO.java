package dao;

import entities.Musteri;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MusteriDAO {

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

    public void musteriEkle(Musteri musteri) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("INSERT INTO musteri(musteri_adi,telefon_no,aciklama)VALUES(?,?,?)");

            pstmt.setString(1, musteri.getMusteriAdi());
            pstmt.setString(2, musteri.getTelefonNo());
            pstmt.setString(3, musteri.getAciklama());
            pstmt.executeUpdate();

            close();
        } catch (Exception ex) {
            close();
            if (ex.getMessage().contains("Duplicate")) {
                String msg = "Lütfen Müşteri Adı alanını boş bırakmayınız!";
                msg = "Bu kayıt veritabanında mevcut!";
                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void musteriGuncelle(Musteri musteri) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("UPDATE musteri SET musteri_adi = ?,telefon_no = ?, aciklama = ? WHERE musteri_id = ?");

            pstmt.setString(1, musteri.getMusteriAdi());
            pstmt.setString(2, musteri.getTelefonNo());
            pstmt.setString(3, musteri.getAciklama());
            pstmt.setInt(4, musteri.getMusteriId());
            pstmt.executeUpdate();

            close();
        } catch (Exception ex) {
            close();
            if (ex.getMessage().contains("Duplicate")) {
                String msg = "Lütfen Müşteri Adı alanını boş bırakmayınız!";
                msg = "Bu kayıt veritabanında mevcut!";
                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void musteriSil(Musteri musteri) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("delete from musteri where musteri_id = ?");

            pstmt.setInt(1, musteri.getMusteriId());
            pstmt.executeUpdate();

            close();
        } catch (Exception ex) {
            ex.printStackTrace();
            close();
            String msg = "";
            if (ex.getMessage().contains("foreign key")) {
                msg = "Bu müşteriyi silmek için önce Hizmet Ekranı'nda müşteriye ait hizmetleri ve araçları silmelisiniz! Müşteri üzerinde hizmet ve araç kalmadıysa müşteriyi silebilirsiniz!";
                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void fillTable(JTable table) throws ClassNotFoundException, IOException {
        try {
            String query = "    select rownum() as sira_no, m.*\n"
                    + "    from musteri m  \n"
                    + "    order by musteri_id desc";

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

    public void fillTable(JTable table, String str) throws ClassNotFoundException, IOException {
        try {
            String query = "select rownum() as sira_no, m.*\n"
                    + "    from musteri m    \n"
                    + "    where musteri_adi like '" + str + "%'  \n"
                    + "    order by musteri_adi asc";

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

    public HashMap hmMusteri() throws ClassNotFoundException, IOException {
        try {

            HashMap hashMap = new HashMap();

            String query = "select (m.musteri_adi ||' # ' || a.plaka) as musteri_adi from musteri m, arac a\n"
                    + "where m.musteri_id = a.musteri_id and a.arac_durum_id=1 order by m.musteri_adi";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    hashMap.put(rs.getRow(), rs.getString("musteri_adi"));
                }
            }

            close();
            return hashMap;
        } catch (SQLException e) {
            close();
            e.printStackTrace();
            return null;
        }
    }
}
