package dao;

import entities.Ucret;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FiyatListesiDAO {

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

    public void ucretEkle(Ucret ucret) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("INSERT INTO ucret(arac_kategori_id, faaliyet_id, ucret, aciklama) values (?, ?, ?, ?)");

            pstmt.setInt(1, ucret.getArac_kategori_id());
            pstmt.setInt(2, ucret.getFaaliyet_id());
            pstmt.setInt(3, ucret.getUcret());
            pstmt.setString(4, ucret.getAciklama());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void ucretGuncelle(Ucret ucret) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("UPDATE ucret SET arac_kategori_id = ?, faaliyet_id = ?, ucret = ?, aciklama = ? WHERE ucret_id = ?");

            pstmt.setInt(1, ucret.getArac_kategori_id());
            pstmt.setInt(2, ucret.getFaaliyet_id());
            pstmt.setInt(3, ucret.getUcret());
            pstmt.setString(4, ucret.getAciklama());
            pstmt.setInt(5, ucret.getUcret_id());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void ucretSil(Ucret ucret) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("delete from ucret where ucret_id = ?");

            pstmt.setInt(1, ucret.getUcret_id());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void fillTable(JTable table) throws ClassNotFoundException, IOException {
        try {
            String query = "select u.ucret_id, ak.arac_kategori_adi, f.faaliyet_adi, u.ucret, u.aciklama \n"
                    + "from ucret u, arac_kategori ak, faaliyet f\n"
                    + "where u.arac_kategori_id = ak.arac_kategori_id\n"
                    + "and u.faaliyet_id = f.faaliyet_id";

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

    public void fillTable(JTable table, Ucret ucret) throws ClassNotFoundException, IOException {
        try {
            String query = "";

            connect();
            ResultSet rs = null;
            if (ucret.getArac_kategori_id() == 0 && ucret.getFaaliyet_id() != 0) {
                query = "select u.ucret_id, ak.arac_kategori_adi, f.faaliyet_adi, u.ucret, u.aciklama \n"
                        + "from ucret u, arac_kategori ak, faaliyet f\n"
                        + "where u.arac_kategori_id = ak.arac_kategori_id\n"
                        + "and u.faaliyet_id = f.faaliyet_id \n"
                        + "and u.faaliyet_id = " + ucret.getFaaliyet_id();

                pstmt = conn.prepareStatement(query);
                rs = pstmt.executeQuery();
            } else if (ucret.getArac_kategori_id() != 0 && ucret.getFaaliyet_id() != 0) {
                query = "select u.ucret_id, ak.arac_kategori_adi, f.faaliyet_adi, u.ucret, u.aciklama \n"
                        + "from ucret u, arac_kategori ak, faaliyet f\n"
                        + "where u.arac_kategori_id = ak.arac_kategori_id\n"
                        + "and u.faaliyet_id = f.faaliyet_id \n"
                        + "and u.arac_kategori_id = " + ucret.getArac_kategori_id()
                        + "\n and u.faaliyet_id = " + ucret.getFaaliyet_id();
                pstmt = conn.prepareStatement(query);
                rs = pstmt.executeQuery();
            } else {
                query = "select u.ucret_id, ak.arac_kategori_adi, f.faaliyet_adi, u.ucret, u.aciklama \n"
                        + "from ucret u, arac_kategori ak, faaliyet f\n"
                        + "where u.arac_kategori_id = ak.arac_kategori_id\n"
                        + "and u.faaliyet_id = f.faaliyet_id \n"
                        + "and u.arac_kategori_id = " + ucret.getArac_kategori_id();
                pstmt = conn.prepareStatement(query);
                rs = pstmt.executeQuery();
            }
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

    public boolean kayitVarMi(Ucret ucret) {
        try {
            boolean kayitVarMi = false;
            String query = "";

            connect();
            ResultSet rs = null;

            query = "select u.ucret_id, ak.arac_kategori_adi, f.faaliyet_adi, u.ucret, u.aciklama \n"
                    + "from ucret u, arac_kategori ak, faaliyet f\n"
                    + "where u.arac_kategori_id = ak.arac_kategori_id\n"
                    + "and u.faaliyet_id = f.faaliyet_id \n"
                    + "and u.arac_kategori_id = " + ucret.getArac_kategori_id()
                    + "\n and u.faaliyet_id = " + ucret.getFaaliyet_id();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kayitVarMi = true;
            }
            return kayitVarMi;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Ucret getUcret(int aracKategoriId, int faaliyetId) {
        try {
            Ucret ucr = new Ucret();
            String query = "";

            connect();
            ResultSet rs = null;

            query = "select u.ucret_id, ak.arac_kategori_adi, f.faaliyet_adi, u.ucret, u.aciklama \n"
                    + "from ucret u, arac_kategori ak, faaliyet f\n"
                    + "where u.arac_kategori_id = ak.arac_kategori_id\n"
                    + "and u.faaliyet_id = f.faaliyet_id \n"
                    + "and u.arac_kategori_id = " + aracKategoriId
                    + "\n and u.faaliyet_id = " + faaliyetId;
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ucr.setUcret_id(rs.getInt("ucret_id"));
                ucr.setUcret(rs.getInt("ucret"));
            }
            close();
            return ucr;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
