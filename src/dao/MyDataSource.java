/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import org.h2.jdbcx.JdbcDataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import security.AESenc;

/**
 *
 * @author Yakup
 */
public class MyDataSource {

    public JdbcDataSource getH2DataSource() throws
            FileNotFoundException, IOException, Exception {

        Properties props = new Properties();
        FileInputStream fis = null;
        JdbcDataSource ds = null;

        fis = new FileInputStream("app.properties");
        props.load(fis);

        ds = new JdbcDataSource();
        ds.setURL(AESenc.decrypt(props.getProperty("h2.url").trim()));
        ds.setUser(AESenc.decrypt(props.getProperty("h2.username").trim()));
        ds.setPassword(AESenc.decrypt(props.getProperty("h2.password").trim()));

        return ds;
    }

}
