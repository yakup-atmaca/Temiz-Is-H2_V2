package dao;

import entities.AracKategori;
import entities.AracModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.DataSource;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ModelDAO {

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

    public void modelEkle(AracModel aracModel) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("INSERT INTO arac_model(arac_marka_id, arac_kategori_id, arac_model_adi)VALUES(?,?,?)");

            pstmt.setInt(1, aracModel.getAracMarkaId());
            pstmt.setInt(2, aracModel.getAracKategoriId());
            pstmt.setString(3, aracModel.getAracModelAdi());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void modelGuncelle(AracModel aracModel) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("UPDATE arac_model SET arac_marka_id = ?, arac_kategori_id = ?, arac_model_adi = ?\n"
                            + "WHERE arac_model_id = ?");

            pstmt.setInt(1, aracModel.getAracMarkaId());
            pstmt.setInt(2, aracModel.getAracKategoriId());
            pstmt.setString(3, aracModel.getAracModelAdi());
            pstmt.setInt(4, aracModel.getAracModelId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void modelSil(AracModel aracModel) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("delete from arac_model where arac_model_id = ?");

            pstmt.setInt(1, aracModel.getAracModelId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void fillTable(JTable table) throws ClassNotFoundException, IOException {
        try {
            String query = "SELECT rownum() as sira_no, m.arac_kategori_id, k.arac_kategori_adi, m.arac_marka_id, r.arac_marka_adi, arac_model_id, arac_model_adi\n"
                    + "FROM arac_model m, arac_kategori k, arac_marka r\n"
                    + "where m.arac_kategori_id = k.arac_kategori_id\n"
                    + "and m.arac_marka_id = r.arac_marka_id\n"
                    + "order by r.arac_marka_adi";

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

    public void fillTable(JTable table, int aracKategoriId, int aracMarkaId) throws ClassNotFoundException, IOException {
        try {
            String query = "";

            if (aracKategoriId != 0 && aracMarkaId == 0) {
                query = "SELECT rownum() as sira_no, m.arac_kategori_id, k.arac_kategori_adi, m.arac_marka_id, r.arac_marka_adi, arac_model_id, arac_model_adi\n"
                        + "FROM arac_model m, arac_kategori k, arac_marka r\n"
                        + "where m.arac_kategori_id = k.arac_kategori_id\n"
                        + "and m.arac_marka_id = r.arac_marka_id\n"
                        + "and m.arac_kategori_id =" + aracKategoriId + "\n"
                        + "order by k.arac_kategori_id";

            } else if (aracKategoriId == 0 && aracMarkaId != 0) {
                query =  "SELECT rownum() as sira_no, m.arac_kategori_id, k.arac_kategori_adi, m.arac_marka_id, r.arac_marka_adi, arac_model_id, arac_model_adi\n"
                        + "FROM arac_model m, arac_kategori k, arac_marka r\n"
                        + "where m.arac_kategori_id = k.arac_kategori_id\n"
                        + "and m.arac_marka_id = r.arac_marka_id\n"
                        + "and m.arac_marka_id =" + aracMarkaId + "\n"
                        + "order by k.arac_kategori_id";
            } else if (aracKategoriId != 0 && aracMarkaId != 0) {
                query =  "SELECT rownum() as sira_no, m.arac_kategori_id, k.arac_kategori_adi, m.arac_marka_id, r.arac_marka_adi, arac_model_id, arac_model_adi\n"
                        + "FROM arac_model m, arac_kategori k, arac_marka r\n"
                        + "where m.arac_kategori_id = k.arac_kategori_id\n"
                        + "and m.arac_marka_id = r.arac_marka_id\n"
                        + "and m.arac_marka_id =" + aracMarkaId + "\n"
                        + "and m.arac_kategori_id =" + aracKategoriId + "\n"
                        + "order by k.arac_kategori_id";
            } else {
                query = "SELECT rownum() as sira_no, m.arac_kategori_id, k.arac_kategori_adi, m.arac_marka_id, r.arac_marka_adi, arac_model_id, arac_model_adi\n"
                        + "FROM arac_model m, arac_kategori k, arac_marka r\n"
                        + "where m.arac_kategori_id = k.arac_kategori_id\n"
                        + "and m.arac_marka_id = r.arac_marka_id\n"
                        + "order by k.arac_kategori_id";
            }

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

    public HashMap hmAracKategori() throws ClassNotFoundException, IOException {
        try {

            HashMap hashMap = new HashMap();

            String query = "select arac_kategori_id, arac_kategori_adi from arac_kategori order by arac_kategori_id";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    hashMap.put(rs.getInt("arac_kategori_id"), rs.getString("arac_kategori_adi"));
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

    public HashMap hmAracMarka() throws ClassNotFoundException, IOException {
        try {

            HashMap hashMap = new HashMap();

            String query = "select arac_marka_id, arac_marka_adi from arac_marka order by arac_marka_adi";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    hashMap.put(rs.getInt("arac_marka_id"), rs.getString("arac_marka_adi"));
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

    public String aracMarka(int aracModelId) throws ClassNotFoundException, IOException {
        try {

            String aracMarkaAdi = "";

            String query = "select arac_marka_adi from arac_model mo, arac_marka ma\n"
                    + " where ma.arac_marka_id = mo.arac_marka_id and mo.arac_model_id = '" + aracModelId + "'";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    aracMarkaAdi = rs.getString("arac_marka_adi");
                }
            }

            close();

            return aracMarkaAdi;
        } catch (SQLException e) {
            close();
            e.printStackTrace();
            return null;
        }
    }

    public HashMap hmAracModel(String aracMarkaAdi) throws ClassNotFoundException, IOException {
        try {

            HashMap hashMap = new HashMap();

            String query = "select arac_model_id, arac_model_adi from arac_model mo, arac_marka ma\n"
                    + " where ma.arac_marka_id = mo.arac_marka_id and ma.arac_marka_adi = '" + aracMarkaAdi + "'";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    hashMap.put(rs.getInt("arac_model_id"), rs.getString("arac_model_adi"));
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

    public HashMap hmAracModel() throws ClassNotFoundException, IOException {
        try {

            HashMap hashMap = new HashMap();

            String query = "select arac_model_id, arac_model_adi from arac_model mo, arac_marka ma\n"
                    + " where ma.arac_marka_id = mo.arac_marka_id";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    hashMap.put(rs.getInt("arac_model_id"), rs.getString("arac_model_adi"));
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
