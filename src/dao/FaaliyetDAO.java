package dao;

import entities.Faaliyet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.DataSource;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FaaliyetDAO {

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

    public void faaliyetEkle(Faaliyet faaliyet) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("insert into faaliyet(faaliyet_adi, puan) values (?, ?)");

            pstmt.setString(1, faaliyet.getFaaliyetAdi());
            pstmt.setInt(2, faaliyet.getPuan());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void faaliyetGuncelle(Faaliyet faaliyet) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("update faaliyet set faaliyet_adi = ?,  puan = ? where faaliyet_id = ?");

            pstmt.setString(1, faaliyet.getFaaliyetAdi());
            pstmt.setInt(2, faaliyet.getPuan());
            pstmt.setInt(3, faaliyet.getFaaliyetId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void faaliyetSil(Faaliyet faaliyet) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("delete from faaliyet where faaliyet_id = ?");

            pstmt.setInt(1, faaliyet.getFaaliyetId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void fillTable(JTable table) throws ClassNotFoundException, IOException {
        try {
            String query = " select rownum() as sira_no, f.*\n"
                    + "    from faaliyet  f  \n"
                    + "    order by faaliyet_adi asc";

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

    public HashMap hashMap() throws ClassNotFoundException {
        try {

            HashMap hashMap = new HashMap();

            String query = "select faaliyet_id, faaliyet_adi from faaliyet order by faaliyet_id";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    hashMap.put(rs.getInt("faaliyet_id"), rs.getString("faaliyet_adi"));
                }
            }

            close();

            return hashMap;

        } catch (Exception e) {
            close();
            e.printStackTrace();
            return null;
        }
    }

    public Faaliyet getFaaliyet(String faaliyetAdi) throws ClassNotFoundException, IOException {
        try {
            Faaliyet f = new Faaliyet();

            String query = "select faaliyet_id, faaliyet_adi,  puan from faaliyet where faaliyet_adi = '" + faaliyetAdi + "'";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    f.setFaaliyetId(rs.getInt("faaliyet_id"));
                    f.setFaaliyetAdi(rs.getString("faaliyet_adi"));
                    f.setPuan(rs.getInt("puan"));
                }
            }

            close();

            return f;
        } catch (SQLException e) {
            close();
            e.printStackTrace();
            return null;
        }
    }
}
