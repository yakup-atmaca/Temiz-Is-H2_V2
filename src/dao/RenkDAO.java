package dao;

import entities.Renk;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.DataSource;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RenkDAO {

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

    public void renkEkle(Renk renk) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("insert into renk(renk_adi) values (?)");

            pstmt.setString(1, renk.getRenkAdi());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void renkGuncelle(Renk renk) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("update renk set renk_adi = ? where renk_id = ?");

            pstmt.setString(1, renk.getRenkAdi());
            pstmt.setInt(2, renk.getRenkId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void renkSil(Renk renk) throws SQLException, ClassNotFoundException {
        try {
            connect();

            pstmt = conn
                    .prepareStatement("delete from renk where renk_id = ?");

            pstmt.setInt(1, renk.getRenkId());
            pstmt.executeUpdate();

            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void fillTable(JTable table) throws ClassNotFoundException, IOException {
        try {
            String query =  "    select rownum() as sira_no, r.*\n"
                    + "    from renk r   \n"
                    + "    order by renk_adi asc";

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

    public HashMap hashMap() throws ClassNotFoundException, IOException {
        try {

            HashMap hashMap = new HashMap();

            String query = "select renk_id, renk_adi from renk";

            connect();
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    hashMap.put(rs.getInt("renk_id"), rs.getString("renk_adi"));
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
