/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ekranlar;

import dao.AracDAO;
import dao.FiyatListesiDAO;
import entities.Faaliyet;
import entities.Hizmet;
import entities.Ucret;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import dao.FaaliyetDAO;
import dao.HizmetDAO;
import dao.ModelDAO;
import dao.MusteriDAO;
import dao.RenkDAO;
import entities.Arac;
import entities.AracModel;
import entities.Musteri;
import entities.Renk;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import security.AESenc;
import security.MacAddress;

/**
 *
 * @author Yakup
 */
public class Temizis extends javax.swing.JFrame {

    HashMap hmMusteri = new HashMap();
    Set entrySetMusteri;
    Iterator itMusteri;

    HashMap hmFaaliyet = new HashMap();
    Set entrySetFaaliyet;
    Iterator itFaaliyet;

    int hizmetId = 0;
    int aracKategoriId = 0;
    int faaliyetId = 0;

    Arac arac;
    String plaka;

    int arac_id;
    int musteri_id;
    HashMap hmAracMarka = new HashMap();
    Set entrySetAracMarka;
    Iterator itAracMarka;

    HashMap hmAracModel = new HashMap();
    Set entrySetAracModel;
    Iterator itAracModel;

    HashMap hmRenk;
    Set entrySetRenk;
    Iterator itRenk;

    AracDAO aracDAO = new AracDAO();
    ModelDAO modelDAO = new ModelDAO();
    RenkDAO renkDAO = new RenkDAO();
    MusteriDAO musteriDAO = new MusteriDAO();

    HashMap hmAracKategori = new HashMap();
    Set entrySetAracKategori;
    Iterator itAracKategori;

    int aracModelId = 0;
    int aracMarkaId = 0;
    int faaliyet_id = 0;
    int renk_id = 0;
    int ucretID = 0;

    String musteriAdi = null;

    boolean programBasladi = false;

    public Temizis() throws ClassNotFoundException, IOException, Exception {
        initComponents();

        cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
        cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);

        HizmetDAO hizmetDAO = new HizmetDAO();
        hizmetDAO.hizmetGetir(tblHizmet);

        FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

        hmFaaliyet = faaliyetDAO.hashMap();

        cmbFaaliyet.addItem("Seçiniz");

        entrySetFaaliyet = hmFaaliyet.entrySet();

        itFaaliyet = entrySetFaaliyet.iterator();

        while (itFaaliyet.hasNext()) {
            Map.Entry me = (Map.Entry) itFaaliyet.next();
            cmbFaaliyet.addItem(me.getValue().toString());
        }

        Calendar today = Calendar.getInstance();

        dtIslemTarihi.setDate(today.getTime());
        getToplamGunlukKazanc();

        ///Müşteri ve Araç Tanımı
        try {
            aracDAO.fillTable(tblArac, "");
            musteriDAO.fillTable(tblMusteri);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        hmAracMarka = modelDAO.hmAracMarka();
        cmbAracMarka.addItem("Seçiniz");
        entrySetAracMarka = hmAracMarka.entrySet();
        itAracMarka = entrySetAracMarka.iterator();

        while (itAracMarka.hasNext()) {
            Map.Entry me = (Map.Entry) itAracMarka.next();
            cmbAracMarka.addItem(me.getValue().toString());
        }

        try {
            hmAracModel = modelDAO.hmAracModel();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        cmbAracModel.addItem("Seçiniz");
        entrySetAracModel = hmAracModel.entrySet();
        itAracModel = entrySetAracModel.iterator();

        while (itAracModel.hasNext()) {
            Map.Entry me = (Map.Entry) itAracModel.next();
            cmbAracModel.addItem(me.getValue().toString());
        }

        hmRenk = new HashMap();
        hmRenk = renkDAO.hashMap();

        cmbDisRenk.addItem("Seçiniz");
        cmbIcRenk.addItem("Seçiniz");

        entrySetRenk = hmRenk.entrySet();

        itRenk = entrySetRenk.iterator();

        while (itRenk.hasNext()) {
            Map.Entry me = (Map.Entry) itRenk.next();
            cmbDisRenk.addItem(me.getValue().toString());
            cmbIcRenk.addItem(me.getValue().toString());
        }

        cmbAracDurumu.addItem("Seçiniz");
        cmbAracDurumu.addItem("AKTİF");
        cmbAracDurumu.addItem("PASİF");

        ///////////Model Sayfası için
        ModelDAO modelDAO = new ModelDAO();
        try {
            hmAracKategori = modelDAO.hmAracKategori();
            cmbAracKategorisi.addItem("Seçiniz");
            entrySetAracKategori = hmAracKategori.entrySet();
            itAracKategori = entrySetAracKategori.iterator();

            while (itAracKategori.hasNext()) {
                Map.Entry me = (Map.Entry) itAracKategori.next();
                cmbAracKategorisi.addItem(me.getValue().toString());
            }

            hmAracMarka = modelDAO.hmAracMarka();
            cmbAracMarkaModelTanimi.addItem("Seçiniz");
            entrySetAracMarka = hmAracMarka.entrySet();
            itAracMarka = entrySetAracMarka.iterator();

            while (itAracMarka.hasNext()) {
                Map.Entry me = (Map.Entry) itAracMarka.next();
                cmbAracMarkaModelTanimi.addItem(me.getValue().toString());
            }

            modelDAO.fillTable(tblAracModel);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        ////////Hizmetler sayfası için
        faaliyetDAO = new FaaliyetDAO();
        try {
            faaliyetDAO.fillTable(tblFaaliyet);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        //////////Fiyat Listesi sayfası için
        faaliyetDAO = new FaaliyetDAO();
        hmFaaliyet = faaliyetDAO.hashMap();
        cmbFaaliyetFiyatListesi.addItem("Seçiniz");
        entrySetFaaliyet = hmFaaliyet.entrySet();
        itFaaliyet = entrySetFaaliyet.iterator();

        while (itFaaliyet.hasNext()) {
            Map.Entry me = (Map.Entry) itFaaliyet.next();
            cmbFaaliyetFiyatListesi.addItem(me.getValue().toString());
        }

        ModelDAO aracKategoriDAO = new ModelDAO();
        hmAracKategori = aracKategoriDAO.hmAracKategori();
        cmbAracKategorisiFiyatListesi.addItem("Seçiniz");
        entrySetAracKategori = hmAracKategori.entrySet();
        itAracKategori = entrySetAracKategori.iterator();

        while (itAracKategori.hasNext()) {
            Map.Entry me = (Map.Entry) itAracKategori.next();
            cmbAracKategorisiFiyatListesi.addItem(me.getValue().toString());
        }

        FiyatListesiDAO ucretDAO = new FiyatListesiDAO();

        ucretDAO.fillTable(tblFiyatListesi);

        programBasladi = true;

        Font f = new Font("Arial", Font.BOLD, 16);
        JTableHeader header = tblArac.getTableHeader();
        header.setFont(f);

        header = tblAracModel.getTableHeader();
        header.setFont(f);

        header = tblHizmet.getTableHeader();
        header.setFont(f);

        header = tblFaaliyet.getTableHeader();
        header.setFont(f);

        header = tblFiyatListesi.getTableHeader();
        header.setFont(f);

        header = tblMusteri.getTableHeader();
        header.setFont(f);

        header = tblRapor.getTableHeader();
        header.setFont(f);
    }

    public final void cmbMusteriAdiPlakaDoldur(JComboBox cmb) throws ClassNotFoundException, IOException {

        cmb.removeAllItems();

        MusteriDAO musteriDAO = new MusteriDAO();

        hmMusteri = musteriDAO.hmMusteri();

        entrySetMusteri = hmMusteri.entrySet();

        itMusteri = entrySetMusteri.iterator();

        while (itMusteri.hasNext()) {
            Map.Entry me = (Map.Entry) itMusteri.next();
            cmb.addItem(me.getValue().toString());
        }
        AutoCompleteDecorator.decorate(cmb);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        cmbMusteriAdiPlaka = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbFaaliyet = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtUcret = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPuan = new javax.swing.JTextField();
        rdbPuanEkle = new javax.swing.JRadioButton();
        rdbPuanKullan = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        dtIslemTarihi = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtAciklama = new javax.swing.JTextField();
        btnEkle = new javax.swing.JButton();
        btnGuncelle = new javax.swing.JButton();
        btnSil = new javax.swing.JButton();
        lblToplamPuan = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblToplamGunlukKazanc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHizmet = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtTelefonNo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnEkle1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblArac = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnSilMst = new javax.swing.JButton();
        btnGuncelleMst = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnGuncelle1 = new javax.swing.JButton();
        txtAciklamaArac = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtAciklamaMst = new javax.swing.JTextField();
        txtPlaka = new javax.swing.JTextField();
        cmbAracDurumu = new javax.swing.JComboBox<>();
        cmbAracMarka = new javax.swing.JComboBox<>();
        txtMusteriAdi = new javax.swing.JTextField();
        cmbDisRenk = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMusteri = new javax.swing.JTable();
        btnEkleMst = new javax.swing.JButton();
        cmbIcRenk = new javax.swing.JComboBox<>();
        btnSilArac = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cmbAracModel = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        btnExceleGonderMstRapor = new javax.swing.JButton();
        btnRaporGetirMstRapor = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        dtIlkTarihMstRapor = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblRapor = new javax.swing.JTable();
        dtSonTarihMstRapor = new com.toedter.calendar.JDateChooser();
        jLabel37 = new javax.swing.JLabel();
        cmbMusteriAdiPlakaRapor = new javax.swing.JComboBox<>();
        chkTumMusteriler = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblAracModel = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        txtModelAdi = new javax.swing.JTextField();
        btnGuncelleModel = new javax.swing.JButton();
        cmbAracKategorisi = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        cmbAracMarkaModelTanimi = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        btnSilModel = new javax.swing.JButton();
        btnEkleModel = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnGuncelle2 = new javax.swing.JButton();
        txtPuanHizmetler = new javax.swing.JTextField();
        txtFaaliyetAdi = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblFaaliyet = new javax.swing.JTable();
        btnEkle2 = new javax.swing.JButton();
        btnSil1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        cmbAracKategorisiFiyatListesi = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        cmbFaaliyetFiyatListesi = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        txtUcretFiyatListesi = new javax.swing.JTextField();
        btnKaydet = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblFiyatListesi = new javax.swing.JTable();
        jLabel34 = new javax.swing.JLabel();
        txtAciklamaFiyatListesi = new javax.swing.JTextField();
        btnSil3 = new javax.swing.JButton();
        btnExceleGonder1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblSonuc = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Temiz-İş Oto Yıkama Takip Programı");
        setBackground(new java.awt.Color(0, 51, 153));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setSize(new java.awt.Dimension(1200, 800));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Arial", 1, 23)); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1195, 790));

        jPanel1.setBackground(new java.awt.Color(0, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1170, 790));

        cmbMusteriAdiPlaka.setEditable(true);
        cmbMusteriAdiPlaka.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        cmbMusteriAdiPlaka.setForeground(new java.awt.Color(0, 51, 204));
        cmbMusteriAdiPlaka.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMusteriAdiPlakaItemStateChanged(evt);
            }
        });
        cmbMusteriAdiPlaka.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cmbMusteriAdiPlakaKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Müşteri Adı-Plaka");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("İşlem");

        cmbFaaliyet.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        cmbFaaliyet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFaaliyetItemStateChanged(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setText("Ücret");

        txtUcret.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtUcret.setText("0");
        txtUcret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUcretActionPerformed(evt);
            }
        });
        txtUcret.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUcretKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setText("Puan");

        txtPuan.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPuan.setText("0");
        txtPuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPuanActionPerformed(evt);
            }
        });
        txtPuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPuanKeyTyped(evt);
            }
        });

        rdbPuanEkle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rdbPuanEkle.setSelected(true);
        rdbPuanEkle.setText("Puan Ekle");
        rdbPuanEkle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdbPuanEkleİtemStateChanged(evt);
            }
        });

        rdbPuanKullan.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rdbPuanKullan.setText("Puan Kullan");
        rdbPuanKullan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdbPuanKullanİtemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Tarih");

        dtIslemTarihi.setDateFormatString("dd/MM/yyyy");
        dtIslemTarihi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setText("Açıklama");
        jLabel6.setPreferredSize(new java.awt.Dimension(64, 16));

        txtAciklama.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        btnEkle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnEkle.setText("Ekle");
        btnEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEkleActionPerformed(evt);
            }
        });
        btnEkle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEkleKeyPressed(evt);
            }
        });

        btnGuncelle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnGuncelle.setText("Güncelle");
        btnGuncelle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuncelleActionPerformed(evt);
            }
        });
        btnGuncelle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuncelleKeyPressed(evt);
            }
        });

        btnSil.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnSil.setText("Sil");
        btnSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilActionPerformed(evt);
            }
        });
        btnSil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSilKeyPressed(evt);
            }
        });

        lblToplamPuan.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setText("Müşteri Puanı:");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setText("Bugünkü Toplam Kazanç:");

        lblToplamGunlukKazanc.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1800, 1800));

        tblHizmet.setAutoCreateRowSorter(true);
        tblHizmet.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tblHizmet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sıra No", "Hizmet No", "Müşteri Adı ", "Plaka", "İşlem", "Ücret", "Puan", "Tarih", "Açıklama"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHizmet.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tblHizmet.setMinimumSize(new java.awt.Dimension(115, 0));
        tblHizmet.setPreferredSize(new java.awt.Dimension(1200, 1600));
        tblHizmet.setRowHeight(20);
        tblHizmet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHizmetMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHizmet);
        if (tblHizmet.getColumnModel().getColumnCount() > 0) {
            tblHizmet.getColumnModel().getColumn(0).setMaxWidth(60);
            tblHizmet.getColumnModel().getColumn(1).setMaxWidth(100);
            tblHizmet.getColumnModel().getColumn(2).setMinWidth(150);
            tblHizmet.getColumnModel().getColumn(3).setMinWidth(120);
            tblHizmet.getColumnModel().getColumn(3).setMaxWidth(200);
            tblHizmet.getColumnModel().getColumn(4).setMinWidth(200);
            tblHizmet.getColumnModel().getColumn(8).setMinWidth(300);
            tblHizmet.getColumnModel().getColumn(8).setMaxWidth(300);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMusteriAdiPlaka, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtPuan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(rdbPuanEkle)
                                    .addGap(18, 18, 18)
                                    .addComponent(rdbPuanKullan))
                                .addComponent(txtAciklama)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblToplamGunlukKazanc, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(264, 264, 264)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbFaaliyet, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUcret, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtIslemTarihi, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblToplamPuan, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                        .addGap(161, 161, 161))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(btnEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuncelle)
                            .addComponent(btnSil, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblToplamPuan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMusteriAdiPlaka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtUcret, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbFaaliyet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(1, 1, 1)))
                            .addComponent(dtIslemTarihi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rdbPuanEkle)
                                    .addComponent(rdbPuanKullan))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAciklama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblToplamGunlukKazanc, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(btnEkle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuncelle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSil, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Hizmet Girişi", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(1195, 790));

        txtTelefonNo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtTelefonNo.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtTelefonNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonNoKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel11.setText("Aracın Modeli");

        btnEkle1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnEkle1.setText("Araç Ekle");
        btnEkle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEkle1ActionPerformed(evt);
            }
        });
        btnEkle1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEkle1KeyPressed(evt);
            }
        });

        tblArac.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tblArac.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sıra No", "Müşteri Id", "Müşteri Adı ", "Arac Id", "Plaka", "Marka", "Model", "Aracın Dış Rengi", "Aracın İç Rengi", "Aracın Durumu", "Açıklama", "ModelId", "Araç Kategorisi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblArac.setRowHeight(20);
        tblArac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAracMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblArac);
        if (tblArac.getColumnModel().getColumnCount() > 0) {
            tblArac.getColumnModel().getColumn(0).setMinWidth(70);
            tblArac.getColumnModel().getColumn(0).setMaxWidth(70);
            tblArac.getColumnModel().getColumn(1).setMinWidth(0);
            tblArac.getColumnModel().getColumn(1).setMaxWidth(0);
            tblArac.getColumnModel().getColumn(3).setMinWidth(0);
            tblArac.getColumnModel().getColumn(3).setMaxWidth(0);
            tblArac.getColumnModel().getColumn(11).setMinWidth(0);
            tblArac.getColumnModel().getColumn(11).setMaxWidth(0);
        }

        jLabel10.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel10.setText("Aracın Durumu");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel12.setText("Aracın İç Rengi");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel13.setText("Aracın Dış Rengi");

        btnSilMst.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnSilMst.setText("Müşteri Sil");
        btnSilMst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilMstActionPerformed(evt);
            }
        });
        btnSilMst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSilMstKeyPressed(evt);
            }
        });

        btnGuncelleMst.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnGuncelleMst.setText("Müşteri Güncelle");
        btnGuncelleMst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuncelleMstActionPerformed(evt);
            }
        });
        btnGuncelleMst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuncelleMstKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel2.setText("Plaka");

        btnGuncelle1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnGuncelle1.setText("Araç Güncelle");
        btnGuncelle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuncelle1ActionPerformed(evt);
            }
        });
        btnGuncelle1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuncelle1KeyPressed(evt);
            }
        });

        txtAciklamaArac.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel14.setText("Açıklama");
        jLabel14.setPreferredSize(new java.awt.Dimension(64, 16));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel15.setText("Müşteri Adı");

        txtAciklamaMst.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        txtPlaka.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtPlaka.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtPlaka.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPlakaKeyTyped(evt);
            }
        });

        cmbAracDurumu.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        cmbAracMarka.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cmbAracMarka.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAracMarkaItemStateChanged(evt);
            }
        });

        txtMusteriAdi.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtMusteriAdi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMusteriAdiKeyTyped(evt);
            }
        });

        cmbDisRenk.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        tblMusteri.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tblMusteri.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Sıra No", "Müşteri Id", "Müşteri Adı ", "Telefon", "Açıklama"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMusteri.setColumnSelectionAllowed(true);
        tblMusteri.setRowHeight(20);
        tblMusteri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMusteriMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblMusteri);
        tblMusteri.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblMusteri.getColumnModel().getColumnCount() > 0) {
            tblMusteri.getColumnModel().getColumn(0).setMinWidth(70);
            tblMusteri.getColumnModel().getColumn(0).setMaxWidth(70);
            tblMusteri.getColumnModel().getColumn(1).setMinWidth(0);
            tblMusteri.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        btnEkleMst.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnEkleMst.setText("Müşteri Ekle");
        btnEkleMst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEkleMstActionPerformed(evt);
            }
        });
        btnEkleMst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEkleMstKeyPressed(evt);
            }
        });

        cmbIcRenk.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        btnSilArac.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnSilArac.setText("Araç Sil");
        btnSilArac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilAracActionPerformed(evt);
            }
        });
        btnSilArac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSilAracKeyPressed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel16.setText("Telefon");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel17.setText("Açıklama");
        jLabel17.setPreferredSize(new java.awt.Dimension(64, 16));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel18.setText("Aracın Markası");

        cmbAracModel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuncelleMst)
                        .addGap(178, 178, 178)
                        .addComponent(btnSilMst))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtMusteriAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefonNo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtAciklamaMst))))
                .addGap(356, 356, 356))
            .addComponent(jScrollPane2)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(386, 386, 386)
                                .addComponent(btnGuncelle1)
                                .addGap(254, 254, 254)
                                .addComponent(btnSilArac))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(124, 124, 124)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEkle1)
                                    .addComponent(txtAciklamaArac, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(btnEkleMst))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(377, 377, 377)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbIcRenk, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbAracDurumu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtPlaka, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbDisRenk, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(343, 343, 343)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAracMarka, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbAracModel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtMusteriAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(txtTelefonNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAciklamaMst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEkleMst)
                    .addComponent(btnGuncelleMst)
                    .addComponent(btnSilMst))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(cmbAracMarka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPlaka, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(cmbAracModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbDisRenk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbIcRenk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbAracDurumu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAciklamaArac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuncelle1)
                    .addComponent(btnSilArac)
                    .addComponent(btnEkle1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Müşteri ve Araç Tanımı", jPanel2);

        jPanel4.setBackground(new java.awt.Color(0, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(1195, 790));

        btnExceleGonderMstRapor.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnExceleGonderMstRapor.setText("Raporu Excele Yazdır");
        btnExceleGonderMstRapor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceleGonderMstRaporActionPerformed(evt);
            }
        });

        btnRaporGetirMstRapor.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnRaporGetirMstRapor.setText("Rapor Getir");
        btnRaporGetirMstRapor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRaporGetirMstRaporActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel22.setText("Son Tarih");

        dtIlkTarihMstRapor.setDateFormatString("dd/MM/yyyy");
        dtIlkTarihMstRapor.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel23.setText("İlk Tarih");

        tblRapor.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tblRapor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sıra No", "Müşteri Adı ", "Plaka", "İşlem", "Ücret", "Puan", "Tarih", "Açıklama"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRapor.setRowHeight(20);
        tblRapor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRaporMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblRapor);
        if (tblRapor.getColumnModel().getColumnCount() > 0) {
            tblRapor.getColumnModel().getColumn(0).setMinWidth(70);
            tblRapor.getColumnModel().getColumn(0).setMaxWidth(70);
            tblRapor.getColumnModel().getColumn(1).setMinWidth(300);
            tblRapor.getColumnModel().getColumn(1).setMaxWidth(300);
            tblRapor.getColumnModel().getColumn(4).setMinWidth(70);
            tblRapor.getColumnModel().getColumn(4).setMaxWidth(70);
            tblRapor.getColumnModel().getColumn(5).setMinWidth(70);
            tblRapor.getColumnModel().getColumn(5).setMaxWidth(70);
        }

        dtSonTarihMstRapor.setDateFormatString("dd/MM/yyyy");
        dtSonTarihMstRapor.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel37.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel37.setText("Müşteri Adı-Plaka");

        cmbMusteriAdiPlakaRapor.setEditable(true);
        cmbMusteriAdiPlakaRapor.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        cmbMusteriAdiPlakaRapor.setForeground(new java.awt.Color(0, 51, 204));
        cmbMusteriAdiPlakaRapor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMusteriAdiPlakaRaporItemStateChanged(evt);
            }
        });
        cmbMusteriAdiPlakaRapor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cmbMusteriAdiPlakaRaporKeyTyped(evt);
            }
        });

        chkTumMusteriler.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        chkTumMusteriler.setText("Tüm Müşteriler");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addComponent(dtIlkTarihMstRapor, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtSonTarihMstRapor, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(121, 121, 121)
                        .addComponent(btnRaporGetirMstRapor))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMusteriAdiPlakaRapor, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(86, 86, 86)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkTumMusteriler)
                    .addComponent(btnExceleGonderMstRapor))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane6)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMusteriAdiPlakaRapor)
                    .addComponent(jLabel37)
                    .addComponent(chkTumMusteriler))
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel22))
                    .addComponent(dtIlkTarihMstRapor, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(dtSonTarihMstRapor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExceleGonderMstRapor, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btnRaporGetirMstRapor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(88, 88, 88)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Raporlama", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 204));
        jPanel5.setPreferredSize(new java.awt.Dimension(1195, 790));

        tblAracModel.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        tblAracModel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sıra No", "AracKtgId", "Araç Kategorisi", "Marka Id", "Marka Adı", "Model Id", "Model Adı"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAracModel.setColumnSelectionAllowed(true);
        tblAracModel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAracModelMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblAracModel);
        tblAracModel.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblAracModel.getColumnModel().getColumnCount() > 0) {
            tblAracModel.getColumnModel().getColumn(0).setMinWidth(70);
            tblAracModel.getColumnModel().getColumn(0).setMaxWidth(70);
            tblAracModel.getColumnModel().getColumn(1).setMinWidth(0);
            tblAracModel.getColumnModel().getColumn(1).setMaxWidth(0);
            tblAracModel.getColumnModel().getColumn(3).setMinWidth(0);
            tblAracModel.getColumnModel().getColumn(3).setMaxWidth(0);
            tblAracModel.getColumnModel().getColumn(5).setMinWidth(0);
            tblAracModel.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        jLabel25.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel25.setText("Araç Kategorisi");

        txtModelAdi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        btnGuncelleModel.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnGuncelleModel.setText("Güncelle");
        btnGuncelleModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuncelleModelActionPerformed(evt);
            }
        });
        btnGuncelleModel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuncelleModelKeyPressed(evt);
            }
        });

        cmbAracKategorisi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        cmbAracKategorisi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAracKategorisiItemStateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel26.setText("Model Adı");

        cmbAracMarkaModelTanimi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        cmbAracMarkaModelTanimi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAracMarkaModelTanimiItemStateChanged(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel27.setText("Araç Markası");

        btnSilModel.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnSilModel.setText("Sil");
        btnSilModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilModelActionPerformed(evt);
            }
        });
        btnSilModel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSilModelKeyPressed(evt);
            }
        });

        btnEkleModel.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnEkleModel.setText("Ekle");
        btnEkleModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEkleModelActionPerformed(evt);
            }
        });
        btnEkleModel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEkleModelKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbAracKategorisi, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbAracMarkaModelTanimi, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtModelAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104)
                .addComponent(btnEkleModel)
                .addGap(37, 37, 37)
                .addComponent(btnGuncelleModel)
                .addGap(41, 41, 41)
                .addComponent(btnSilModel)
                .addContainerGap())
            .addComponent(jScrollPane7)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(cmbAracKategorisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEkleModel)
                        .addComponent(btnGuncelleModel)
                        .addComponent(btnSilModel)
                        .addComponent(jLabel27)
                        .addComponent(cmbAracMarkaModelTanimi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtModelAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Araç Modelleri", jPanel5);

        jPanel6.setBackground(new java.awt.Color(0, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(1195, 790));

        btnGuncelle2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnGuncelle2.setText("Güncelle");
        btnGuncelle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuncelle2ActionPerformed(evt);
            }
        });
        btnGuncelle2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuncelle2KeyPressed(evt);
            }
        });

        txtPuanHizmetler.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPuanHizmetler.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPuanHizmetlerKeyTyped(evt);
            }
        });

        txtFaaliyetAdi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel28.setText("Puan");

        jLabel29.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel29.setText("İşlem Adı");

        tblFaaliyet.setAutoCreateRowSorter(true);
        tblFaaliyet.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        tblFaaliyet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Sıra No", "İşlem Id", "Hizmet Adı ", "Puan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFaaliyet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFaaliyetMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblFaaliyet);
        if (tblFaaliyet.getColumnModel().getColumnCount() > 0) {
            tblFaaliyet.getColumnModel().getColumn(0).setMinWidth(70);
            tblFaaliyet.getColumnModel().getColumn(0).setMaxWidth(70);
            tblFaaliyet.getColumnModel().getColumn(1).setMinWidth(0);
            tblFaaliyet.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        btnEkle2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnEkle2.setText("Ekle");
        btnEkle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEkle2ActionPerformed(evt);
            }
        });
        btnEkle2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEkle2KeyPressed(evt);
            }
        });

        btnSil1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnSil1.setText("Sil");
        btnSil1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSil1ActionPerformed(evt);
            }
        });
        btnSil1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSil1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFaaliyetAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPuanHizmetler, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134)
                .addComponent(btnEkle2)
                .addGap(37, 37, 37)
                .addComponent(btnGuncelle2)
                .addGap(41, 41, 41)
                .addComponent(btnSil1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1190, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel29)
                        .addComponent(txtFaaliyetAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel28)
                        .addComponent(txtPuanHizmetler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEkle2)
                        .addComponent(btnGuncelle2)
                        .addComponent(btnSil1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Hizmet Tanımı", jPanel6);

        jPanel10.setBackground(new java.awt.Color(255, 255, 204));
        jPanel10.setPreferredSize(new java.awt.Dimension(1195, 790));

        cmbAracKategorisiFiyatListesi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        cmbAracKategorisiFiyatListesi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAracKategorisiFiyatListesiItemStateChanged(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel31.setText("Araç Kategorisi");

        jLabel32.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel32.setText("İşlem");

        cmbFaaliyetFiyatListesi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        cmbFaaliyetFiyatListesi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFaaliyetFiyatListesiItemStateChanged(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel33.setText("Ücret");

        txtUcretFiyatListesi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtUcretFiyatListesi.setText("0");
        txtUcretFiyatListesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUcretFiyatListesiActionPerformed(evt);
            }
        });
        txtUcretFiyatListesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUcretFiyatListesiKeyTyped(evt);
            }
        });

        btnKaydet.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnKaydet.setText("Kaydet");
        btnKaydet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKaydetActionPerformed(evt);
            }
        });
        btnKaydet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnKaydetKeyPressed(evt);
            }
        });

        tblFiyatListesi.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tblFiyatListesi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UcretID", "Araç Kategorisi", "İşlem Türü", "Ücret", "Açıklama"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFiyatListesi.setRowHeight(20);
        tblFiyatListesi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFiyatListesiMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblFiyatListesi);
        if (tblFiyatListesi.getColumnModel().getColumnCount() > 0) {
            tblFiyatListesi.getColumnModel().getColumn(0).setMinWidth(0);
            tblFiyatListesi.getColumnModel().getColumn(0).setMaxWidth(0);
            tblFiyatListesi.getColumnModel().getColumn(3).setMinWidth(70);
            tblFiyatListesi.getColumnModel().getColumn(3).setMaxWidth(70);
        }

        jLabel34.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel34.setText("Açıklama");
        jLabel34.setPreferredSize(new java.awt.Dimension(64, 16));

        txtAciklamaFiyatListesi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        btnSil3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnSil3.setText("Sil");
        btnSil3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSil3ActionPerformed(evt);
            }
        });
        btnSil3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSil3KeyPressed(evt);
            }
        });

        btnExceleGonder1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnExceleGonder1.setText("Excele Gönder");
        btnExceleGonder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceleGonder1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(cmbAracKategorisiFiyatListesi, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFaaliyetFiyatListesi, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUcretFiyatListesi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtAciklamaFiyatListesi))
                .addGap(40, 40, 40)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnKaydet)
                        .addGap(34, 34, 34)
                        .addComponent(btnSil3))
                    .addComponent(btnExceleGonder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(56, 56, 56))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAracKategorisiFiyatListesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(cmbFaaliyetFiyatListesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(txtUcretFiyatListesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKaydet)
                    .addComponent(btnSil3))
                .addGap(33, 33, 33)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAciklamaFiyatListesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExceleGonder1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Fiyat Listesi", jPanel10);

        jPanel8.setBackground(new java.awt.Color(0, 255, 255));

        lblSonuc.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblSonuc.setForeground(new java.awt.Color(204, 0, 0));

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel35.setText("* Bu program oto yıkama, ürün satışı, lastik değişimi vs. işlemlerin kaydını tutmak için yapılmıştır.");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setText("* Teknik destek almak veya öneride bulunmak için yakup.atmaca@gmail.com adresine e-posta gönderebilirsiniz.");

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel38.setText("* Bu program tek bilgisayar üzerinde çalışır. Çoklu kullanım için lütfen irtibata geçiniz.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(737, 737, 737)
                .addComponent(lblSonuc, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 1166, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1166, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 1166, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel35)
                .addGap(18, 18, 18)
                .addComponent(jLabel38)
                .addGap(18, 18, 18)
                .addComponent(jLabel36)
                .addGap(67, 67, 67)
                .addComponent(lblSonuc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(651, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Hakkında", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnExceleGonder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceleGonder1ActionPerformed
        try {
            exportTable(tblFiyatListesi, new File("Raporlar/FiyatListesi.xls"));
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExceleGonder1ActionPerformed

    private void btnSil3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSil3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String msg1 = "Lütfen tablodan bir işlem seçiniz!";

            int silmeUyarisiCevabi = 1;
            String msg2 = "Seçmiş olduğunuz işlem kaydını silmek istediğinizden emin misiniz?";
            try {

                if (ucretID == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    silmeUyarisiCevabi = JOptionPane.showConfirmDialog(null, msg2, "Kayıt Silme Uyarısı", JOptionPane.YES_NO_OPTION);
                    if (silmeUyarisiCevabi == 0) {

                        Ucret ucret = new Ucret();
                        ucret.setUcret_id(ucretID);

                        FiyatListesiDAO fiyatListesiDAO = new FiyatListesiDAO();
                        fiyatListesiDAO.ucretSil(ucret);

                        fiyatListesiDAO.fillTable(tblFiyatListesi);

                        cmbAracKategorisiFiyatListesi.setSelectedIndex(0);
                        cmbFaaliyet.setSelectedIndex(0);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSil3KeyPressed

    private void btnSil3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSil3ActionPerformed
        String msg1 = "Lütfen tablodan bir işlem seçiniz!";

        int silmeUyarisiCevabi = 1;
        String msg2 = "Seçmiş olduğunuz işlem kaydını silmek istediğinizden emin misiniz?";
        try {

            if (ucretID == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {
                silmeUyarisiCevabi = JOptionPane.showConfirmDialog(null, msg2, "Kayıt Silme Uyarısı", JOptionPane.YES_NO_OPTION);
                if (silmeUyarisiCevabi == 0) {

                    Ucret ucret = new Ucret();
                    ucret.setUcret_id(ucretID);

                    FiyatListesiDAO fiyatListesiDAO = new FiyatListesiDAO();
                    fiyatListesiDAO.ucretSil(ucret);

                    fiyatListesiDAO.fillTable(tblFiyatListesi);

                    cmbAracKategorisiFiyatListesi.setSelectedIndex(0);
                    cmbFaaliyet.setSelectedIndex(0);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSil3ActionPerformed

    private void tblFiyatListesiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFiyatListesiMouseClicked
        try {
            ucretID = (int) tblFiyatListesi.getValueAt(tblFiyatListesi.getSelectedRow(), 0);
        } catch (Exception e) {
        }
        try {
            for (int i = 0; i < cmbAracKategorisiFiyatListesi.getItemCount(); i++) {
                if (cmbAracKategorisiFiyatListesi.getItemAt(i).equals(tblFiyatListesi.getValueAt(tblFiyatListesi.getSelectedRow(), 1))) {
                    cmbAracKategorisiFiyatListesi.setSelectedIndex(i);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < cmbFaaliyetFiyatListesi.getItemCount(); i++) {
                if (cmbFaaliyetFiyatListesi.getItemAt(i).equals(tblFiyatListesi.getValueAt(tblFiyatListesi.getSelectedRow(), 2))) {
                    cmbFaaliyetFiyatListesi.setSelectedIndex(i);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (tblFiyatListesi.getValueAt(tblFiyatListesi.getSelectedRow(), 3) != null) {
                txtUcretFiyatListesi.setText(tblFiyatListesi.getValueAt(tblFiyatListesi.getSelectedRow(), 3).toString());
            }
            if (tblFiyatListesi.getValueAt(tblFiyatListesi.getSelectedRow(), 4) != null) {
                txtAciklamaFiyatListesi.setText(tblFiyatListesi.getValueAt(tblFiyatListesi.getSelectedRow(), 4).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblFiyatListesiMouseClicked

    private void btnKaydetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnKaydetKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String msg1 = "Lütfen araç kategorisi seçiniz!";
            String msg2 = "Lütfen işlem seçiniz!";
            String msg3 = "Lütfen ücret tutarını giriniz!";

            try {
                Ucret ucret = new Ucret();

                if (cmbAracKategorisiFiyatListesi.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (cmbFaaliyetFiyatListesi.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(txtUcretFiyatListesi.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {

                    itAracKategori = entrySetAracKategori.iterator();
                    while (itAracKategori.hasNext()) {
                        Map.Entry me = (Map.Entry) itAracKategori.next();
                        if (me.getValue().toString().equals(cmbAracKategorisiFiyatListesi.getSelectedItem().toString())) {
                            ucret.setArac_kategori_id((int) me.getKey());
                            break;
                        }
                    }

                    itFaaliyet = entrySetFaaliyet.iterator();
                    while (itFaaliyet.hasNext()) {
                        Map.Entry me = (Map.Entry) itFaaliyet.next();
                        if (me.getValue().toString().equals(cmbFaaliyetFiyatListesi.getSelectedItem().toString())) {
                            ucret.setFaaliyet_id((int) me.getKey());
                            break;
                        }
                    }

                    ucret.setUcret(Integer.parseInt(txtUcretFiyatListesi.getText()));

                    ucret.setAciklama(txtAciklamaFiyatListesi.getText());

                    FiyatListesiDAO fiyatListesiDAO = new FiyatListesiDAO();

                    if (fiyatListesiDAO.kayitVarMi(ucret)) {
                        if (ucretID > 0) {
                            ucret.setUcret_id(ucretID);
                            fiyatListesiDAO.ucretGuncelle(ucret);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tablodan güncellemek istediğiniz kaydı seçiniz!", "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        fiyatListesiDAO.ucretEkle(ucret);
                    }
                    fiyatListesiDAO.fillTable(tblFiyatListesi, ucret);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnKaydetKeyPressed

    private void btnKaydetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKaydetActionPerformed
        String msg1 = "Lütfen araç kategorisi seçiniz!";
        String msg2 = "Lütfen işlem seçiniz!";
        String msg3 = "Lütfen ücret tutarını giriniz!";

        try {
            Ucret ucret = new Ucret();

            if (cmbAracKategorisiFiyatListesi.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (cmbFaaliyetFiyatListesi.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(txtUcretFiyatListesi.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                itAracKategori = entrySetAracKategori.iterator();
                while (itAracKategori.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracKategori.next();
                    if (me.getValue().toString().equals(cmbAracKategorisiFiyatListesi.getSelectedItem().toString())) {
                        ucret.setArac_kategori_id((int) me.getKey());
                        break;
                    }
                }

                itFaaliyet = entrySetFaaliyet.iterator();
                while (itFaaliyet.hasNext()) {
                    Map.Entry me = (Map.Entry) itFaaliyet.next();
                    if (me.getValue().toString().equals(cmbFaaliyetFiyatListesi.getSelectedItem().toString())) {
                        ucret.setFaaliyet_id((int) me.getKey());
                        break;
                    }
                }

                ucret.setUcret(Integer.parseInt(txtUcretFiyatListesi.getText()));

                ucret.setAciklama(txtAciklamaFiyatListesi.getText());

                FiyatListesiDAO fiyatListesiDAO = new FiyatListesiDAO();

                if (fiyatListesiDAO.kayitVarMi(ucret)) {
                    if (ucretID > 0) {
                        ucret.setUcret_id(ucretID);
                        fiyatListesiDAO.ucretGuncelle(ucret);
                    } else {
                        JOptionPane.showMessageDialog(null, "Tablodan güncellemek istediğiniz kaydı seçiniz!", "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    fiyatListesiDAO.ucretEkle(ucret);
                }
                fiyatListesiDAO.fillTable(tblFiyatListesi, ucret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnKaydetActionPerformed

    private void txtUcretFiyatListesiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUcretFiyatListesiKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtUcretFiyatListesiKeyTyped

    private void txtUcretFiyatListesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUcretFiyatListesiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUcretFiyatListesiActionPerformed

    private void cmbFaaliyetFiyatListesiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFaaliyetFiyatListesiItemStateChanged
        FiyatListesiDAO ucretDAO = new FiyatListesiDAO();
        Ucret ucret = new entities.Ucret();

        if (cmbFaaliyetFiyatListesi.getSelectedIndex() != 0) {

            itAracKategori = entrySetAracKategori.iterator();
            while (itAracKategori.hasNext()) {
                Map.Entry me = (Map.Entry) itAracKategori.next();
                if (me.getValue().toString().equals(cmbAracKategorisiFiyatListesi.getSelectedItem().toString())) {
                    ucret.setArac_kategori_id((int) me.getKey());
                    break;
                }
            }

            itFaaliyet = entrySetFaaliyet.iterator();
            while (itFaaliyet.hasNext()) {
                Map.Entry me = (Map.Entry) itFaaliyet.next();
                if (me.getValue().toString().equals(cmbFaaliyetFiyatListesi.getSelectedItem().toString())) {
                    ucret.setFaaliyet_id((int) me.getKey());
                    break;
                }
            }

            try {
                ucretDAO.fillTable(tblFiyatListesi, ucret);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                ucretDAO.fillTable(tblFiyatListesi);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cmbFaaliyetFiyatListesiItemStateChanged

    private void cmbAracKategorisiFiyatListesiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAracKategorisiFiyatListesiItemStateChanged
        FiyatListesiDAO ucretDAO = new FiyatListesiDAO();
        Ucret ucret = new entities.Ucret();

        if (cmbAracKategorisiFiyatListesi.getSelectedIndex() != 0) {

            itAracKategori = entrySetAracKategori.iterator();
            while (itAracKategori.hasNext()) {
                Map.Entry me = (Map.Entry) itAracKategori.next();
                if (me.getValue().toString().equals(cmbAracKategorisiFiyatListesi.getSelectedItem().toString())) {
                    ucret.setArac_kategori_id((int) me.getKey());
                    break;
                }
            }

            itFaaliyet = entrySetFaaliyet.iterator();
            while (itFaaliyet.hasNext()) {
                Map.Entry me = (Map.Entry) itFaaliyet.next();
                if (me.getValue().toString().equals(cmbFaaliyetFiyatListesi.getSelectedItem().toString())) {
                    ucret.setFaaliyet_id((int) me.getKey());
                    break;
                }
            }
            try {
                ucretDAO.fillTable(tblFiyatListesi, ucret);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                ucretDAO.fillTable(tblFiyatListesi);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cmbAracKategorisiFiyatListesiItemStateChanged

    private void btnSil1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSil1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Faaliyet faaliyet = new Faaliyet();

            faaliyet.setFaaliyetAdi(txtFaaliyetAdi.getText());
            faaliyet.setFaaliyetId(faaliyet_id);

            FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

            try {
                faaliyetDAO.faaliyetSil(faaliyet);
                faaliyetDAO.fillTable(tblFaaliyet);
                txtFaaliyetAdi.setText("");

                txtPuan.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSil1KeyPressed

    private void btnSil1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSil1ActionPerformed
        Faaliyet faaliyet = new Faaliyet();

        faaliyet.setFaaliyetAdi(txtFaaliyetAdi.getText());
        faaliyet.setFaaliyetId(faaliyet_id);

        FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

        try {
            faaliyetDAO.faaliyetSil(faaliyet);
            faaliyetDAO.fillTable(tblFaaliyet);
            txtFaaliyetAdi.setText("");

            txtPuan.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSil1ActionPerformed

    private void btnEkle2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEkle2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Faaliyet faaliyet = new Faaliyet();
            String msg = "Lütfen Faaliyet Adı alanını boş bırakmayınız!";
            try {
                if ("".equals(txtFaaliyetAdi.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    faaliyet.setFaaliyetAdi(txtFaaliyetAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));

                    faaliyet.setPuan(Integer.parseInt(txtPuan.getText().trim()));

                    FaaliyetDAO faaliyetDAO = new FaaliyetDAO();
                    faaliyetDAO.faaliyetEkle(faaliyet);
                    faaliyetDAO.fillTable(tblFaaliyet);
                    txtFaaliyetAdi.setText("");

                    txtPuan.setText("0");
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }//GEN-LAST:event_btnEkle2KeyPressed

    private void btnEkle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEkle2ActionPerformed
        Faaliyet faaliyet = new Faaliyet();
        String msg = "Lütfen Faaliyet Adı alanını boş bırakmayınız!";
        try {
            if ("".equals(txtFaaliyetAdi.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {
                faaliyet.setFaaliyetAdi(txtFaaliyetAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));
                faaliyet.setPuan(Integer.parseInt(txtPuan.getText().trim()));

                FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

                faaliyetDAO.faaliyetEkle(faaliyet);
                faaliyetDAO.fillTable(tblFaaliyet);
                txtFaaliyetAdi.setText("");
                txtPuan.setText("");
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

            }
        }
    }//GEN-LAST:event_btnEkle2ActionPerformed

    private void tblFaaliyetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFaaliyetMouseClicked
        faaliyet_id = (int) tblFaaliyet.getValueAt(tblFaaliyet.getSelectedRow(), 1);
        txtFaaliyetAdi.setText(tblFaaliyet.getValueAt(tblFaaliyet.getSelectedRow(), 2).toString());

        txtPuanHizmetler.setText(tblFaaliyet.getValueAt(tblFaaliyet.getSelectedRow(), 3).toString());
    }//GEN-LAST:event_tblFaaliyetMouseClicked

    private void txtPuanHizmetlerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPuanHizmetlerKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPuanHizmetlerKeyTyped

    private void btnGuncelle2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuncelle2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Faaliyet faaliyet = new Faaliyet();
            String msg = "Lütfen Faaliyet Adı alanını boş bırakmayınız!";
            try {
                if ("".equals(txtFaaliyetAdi.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    faaliyet.setFaaliyetAdi(txtFaaliyetAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));

                    faaliyet.setPuan(Integer.parseInt(txtPuan.getText().trim()));
                    faaliyet.setFaaliyetId(faaliyet_id);

                    FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

                    faaliyetDAO.faaliyetGuncelle(faaliyet);
                    faaliyetDAO.fillTable(tblFaaliyet);
                    txtFaaliyetAdi.setText("");

                    txtPuan.setText("");
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnGuncelle2KeyPressed

    private void btnGuncelle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuncelle2ActionPerformed
        Faaliyet faaliyet = new Faaliyet();

        String msg = "Lütfen Faaliyet Adı alanını boş bırakmayınız!";
        try {
            if ("".equals(txtFaaliyetAdi.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                faaliyet.setFaaliyetAdi(txtFaaliyetAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));

                faaliyet.setPuan(Integer.parseInt(txtPuanHizmetler.getText().trim()));
                faaliyet.setFaaliyetId(faaliyet_id);

                FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

                faaliyetDAO.faaliyetGuncelle(faaliyet);
                faaliyetDAO.fillTable(tblFaaliyet);
                txtFaaliyetAdi.setText("");

                txtPuanHizmetler.setText("");
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnGuncelle2ActionPerformed

    private void btnEkleModelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEkleModelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            AracModel aracModel = new AracModel();

            String msg1 = "Lütfen Araç Kategorisi alanını boş bırakmayınız!";
            String msg2 = "Lütfen Marka Adı alanını boş bırakmayınız!";
            String msg3 = "Lütfen Model Adı alanını boş bırakmayınız!";
            try {
                if (aracKategoriId == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (aracMarkaId == 0) {
                    JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(txtModelAdi.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {

                    itAracKategori = entrySetAracKategori.iterator();
                    while (itAracKategori.hasNext()) {
                        Map.Entry me = (Map.Entry) itAracKategori.next();
                        if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                            aracKategoriId = (int) me.getKey();
                        }
                    }

                    aracModel.setAracKategoriId(aracKategoriId);
                    aracModel.setAracMarkaId(aracMarkaId);
                    aracModel.setAracModelAdi(txtModelAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));

                    ModelDAO modelDAO = new ModelDAO();

                    modelDAO.modelEkle(aracModel);
                    //modelDAO.fillTable(tblAracModel);
                    modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
                    txtModelAdi.setText("");
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg1 = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }//GEN-LAST:event_btnEkleModelKeyPressed

    private void btnEkleModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEkleModelActionPerformed
        AracModel aracModel = new AracModel();

        String msg1 = "Lütfen Araç Kategorisi alanını boş bırakmayınız!";
        String msg2 = "Lütfen Marka Adı alanını boş bırakmayınız!";
        String msg3 = "Lütfen Model Adı alanını boş bırakmayınız!";
        try {
            if (aracKategoriId == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (aracMarkaId == 0) {
                JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(txtModelAdi.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                itAracKategori = entrySetAracKategori.iterator();
                while (itAracKategori.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracKategori.next();
                    if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                        aracKategoriId = (int) me.getKey();
                    }
                }

                aracModel.setAracKategoriId(aracKategoriId);
                aracModel.setAracMarkaId(aracMarkaId);
                aracModel.setAracModelAdi(txtModelAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));

                ModelDAO modelDAO = new ModelDAO();

                modelDAO.modelEkle(aracModel);
                //modelDAO.fillTable(tblAracModel);
                modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
                txtModelAdi.setText("");
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg1 = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

            }
        }
    }//GEN-LAST:event_btnEkleModelActionPerformed

    private void btnSilModelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSilModelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            AracModel aracModel = new AracModel();

            aracModel.setAracModelId(aracModelId);

            ModelDAO modelDAO = new ModelDAO();

            try {
                modelDAO.modelSil(aracModel);
                //modelDAO.fillTable(tblAracModel);
                modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
                txtModelAdi.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSilModelKeyPressed

    private void btnSilModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilModelActionPerformed
        AracModel aracModel = new AracModel();

        aracModel.setAracModelId(aracModelId);

        ModelDAO modelDAO = new ModelDAO();

        try {
            modelDAO.modelSil(aracModel);
            //modelDAO.fillTable(tblAracModel);
            modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
            txtModelAdi.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSilModelActionPerformed

    private void cmbAracMarkaModelTanimiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAracMarkaModelTanimiItemStateChanged
        try {
            if (cmbAracMarkaModelTanimi.getSelectedIndex() != 0) {
                itAracKategori = entrySetAracKategori.iterator();
                while (itAracKategori.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracKategori.next();
                    if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                        aracKategoriId = (int) me.getKey();
                    }
                }

                itAracMarka = entrySetAracMarka.iterator();
                while (itAracMarka.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracMarka.next();
                    if (me.getValue().toString().equals(cmbAracMarkaModelTanimi.getSelectedItem().toString())) {
                        aracMarkaId = (int) me.getKey();
                    }
                }

                ModelDAO modelDAO = new ModelDAO();
                modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
            } else {
                aracMarkaId = 0;
                itAracKategori = entrySetAracKategori.iterator();
                while (itAracKategori.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracKategori.next();
                    if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                        aracKategoriId = (int) me.getKey();
                    }
                }

                itAracMarka = entrySetAracMarka.iterator();
                while (itAracMarka.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracMarka.next();
                    if (me.getValue().toString().equals(cmbAracMarkaModelTanimi.getSelectedItem().toString())) {
                        aracMarkaId = (int) me.getKey();
                    }
                }

                ModelDAO modelDAO = new ModelDAO();
                modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbAracMarkaModelTanimiItemStateChanged

    private void cmbAracKategorisiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAracKategorisiItemStateChanged
        try {
            if (cmbAracKategorisi.getSelectedIndex() != 0) {
                itAracKategori = entrySetAracKategori.iterator();
                while (itAracKategori.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracKategori.next();
                    if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                        aracKategoriId = (int) me.getKey();
                    }
                }

                itAracMarka = entrySetAracMarka.iterator();
                while (itAracMarka.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracMarka.next();
                    if (me.getValue().toString().equals(cmbAracMarkaModelTanimi.getSelectedItem().toString())) {
                        aracMarkaId = (int) me.getKey();
                    }
                }

                ModelDAO modelDAO = new ModelDAO();
                modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
            } else {
                aracKategoriId = 0;
                //                ///////// null pointer hatası almamak için eklendi
                //                hmAracKategori = modelDAO.hmAracKategori();
                //                cmbAracKategorisi.addItem("Seçiniz");
                //                entrySetAracKategori = hmAracKategori.entrySet();
                //                ///////////////////////////

                if (entrySetAracKategori != null) {
                    itAracKategori = entrySetAracKategori.iterator();
                    while (itAracKategori.hasNext()) {
                        Map.Entry me = (Map.Entry) itAracKategori.next();
                        if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                            aracKategoriId = (int) me.getKey();
                        }
                    }
                    //                ///////// null pointer hatası almamak için eklendi
                    //                hmAracMarka = modelDAO.hmAracMarka();
                    //                cmbAracMarkaModelTanimi.addItem("Seçiniz");
                    //                entrySetAracMarka = hmAracMarka.entrySet();
                    //                /////////////////////
                    itAracMarka = entrySetAracMarka.iterator();

                    while (itAracMarka.hasNext()) {
                        Map.Entry me = (Map.Entry) itAracMarka.next();
                        if (me.getValue().toString().equals(cmbAracMarkaModelTanimi.getSelectedItem().toString())) {
                            aracMarkaId = (int) me.getKey();
                        }
                    }

                    ModelDAO modelDAO = new ModelDAO();
                    modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbAracKategorisiItemStateChanged

    private void btnGuncelleModelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuncelleModelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            AracModel aracModel = new AracModel();

            String msg1 = "Lütfen Araç Kategorisi alanını boş bırakmayınız!";
            String msg2 = "Lütfen Marka Adı alanını boş bırakmayınız!";
            String msg3 = "Lütfen Model Adı alanını boş bırakmayınız!";
            String msg4 = "Lütfen tablodan bir kayıt seçiniz!";
            try {
                if (aracModelId == 0) {
                    JOptionPane.showMessageDialog(null, msg4, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (aracKategoriId == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (aracMarkaId == 0) {
                    JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(txtModelAdi.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {

                    itAracKategori = entrySetAracKategori.iterator();
                    while (itAracKategori.hasNext()) {
                        Map.Entry me = (Map.Entry) itAracKategori.next();
                        if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                            aracKategoriId = (int) me.getKey();
                        }
                    }

                    aracModel.setAracModelId(aracModelId);
                    aracModel.setAracKategoriId(aracKategoriId);
                    aracModel.setAracMarkaId(aracMarkaId);
                    aracModel.setAracModelAdi(txtModelAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));

                    ModelDAO modelDAO = new ModelDAO();

                    modelDAO.modelGuncelle(aracModel);
                    //modelDAO.fillTable(tblAracModel);
                    modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
                    txtModelAdi.setText("");
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg1 = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }//GEN-LAST:event_btnGuncelleModelKeyPressed

    private void btnGuncelleModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuncelleModelActionPerformed
        AracModel aracModel = new AracModel();

        String msg1 = "Lütfen Araç Kategorisi alanını boş bırakmayınız!";
        String msg2 = "Lütfen Marka Adı alanını boş bırakmayınız!";
        String msg3 = "Lütfen Model Adı alanını boş bırakmayınız!";
        String msg4 = "Lütfen tablodan bir kayıt seçiniz!";
        try {
            if (aracModelId == 0) {
                JOptionPane.showMessageDialog(null, msg4, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (aracKategoriId == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (aracMarkaId == 0) {
                JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(txtModelAdi.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                itAracKategori = entrySetAracKategori.iterator();
                while (itAracKategori.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracKategori.next();
                    if (me.getValue().toString().equals(cmbAracKategorisi.getSelectedItem().toString())) {
                        aracKategoriId = (int) me.getKey();
                    }
                }

                aracModel.setAracModelId(aracModelId);
                aracModel.setAracKategoriId(aracKategoriId);
                aracModel.setAracMarkaId(aracMarkaId);
                aracModel.setAracModelAdi(txtModelAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));

                ModelDAO modelDAO = new ModelDAO();

                modelDAO.modelGuncelle(aracModel);
                //modelDAO.fillTable(tblAracModel);
                modelDAO.fillTable(tblAracModel, aracKategoriId, aracMarkaId);
                txtModelAdi.setText("");
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg1 = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

            }
        }
    }//GEN-LAST:event_btnGuncelleModelActionPerformed

    private void tblAracModelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAracModelMouseClicked
        aracModelId = (int) tblAracModel.getValueAt(tblAracModel.getSelectedRow(), 5);
        txtModelAdi.setText(tblAracModel.getValueAt(tblAracModel.getSelectedRow(), 6).toString());
    }//GEN-LAST:event_tblAracModelMouseClicked

    private void cmbMusteriAdiPlakaRaporKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMusteriAdiPlakaRaporKeyTyped
        MusteriDAO musteriDAO = new MusteriDAO();

        try {
            hmMusteri = musteriDAO.hmMusteri();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        entrySetMusteri = hmMusteri.entrySet();

        itMusteri = entrySetMusteri.iterator();

        while (itMusteri.hasNext()) {
            Map.Entry me = (Map.Entry) itMusteri.next();
            cmbMusteriAdiPlakaRapor.addItem(me.getValue().toString());
        }
        AutoCompleteDecorator.decorate(cmbMusteriAdiPlakaRapor);
    }//GEN-LAST:event_cmbMusteriAdiPlakaRaporKeyTyped

    private void cmbMusteriAdiPlakaRaporItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMusteriAdiPlakaRaporItemStateChanged
        arac = new Arac();
        AracDAO aracDAO = new AracDAO();

        plaka = new String();

        if (cmbMusteriAdiPlakaRapor.toString().contains("#")) {
            plaka = cmbMusteriAdiPlakaRapor.toString().substring(cmbMusteriAdiPlakaRapor.toString().indexOf("#")).replace("]", "").replace("#", "").trim();
        }

        try {
            arac = aracDAO.getArac(plaka);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cmbMusteriAdiPlakaRaporItemStateChanged

    private void tblRaporMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRaporMouseClicked

    }//GEN-LAST:event_tblRaporMouseClicked

    private void btnRaporGetirMstRaporActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRaporGetirMstRaporActionPerformed
        HizmetDAO hizmetDAO = new HizmetDAO();

        String msg2 = "Lütfen tarih alanlarını boş geçmeyiniz!";

        try {

            if (dtIlkTarihMstRapor.getDate() == null) {
                JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (dtSonTarihMstRapor.getDate() == null) {
                JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(dtIlkTarihMstRapor.getDate());

                java.sql.Date ilkTarih = new java.sql.Date(calendar.getTimeInMillis());

                calendar = new GregorianCalendar();
                calendar.setTime(dtSonTarihMstRapor.getDate());

                java.sql.Date sonTarih = new java.sql.Date(calendar.getTimeInMillis());

                if (!chkTumMusteriler.isSelected()) {
                    musteri_id = arac.getMusteriId();
                    musteriAdi = cmbMusteriAdiPlakaRapor.getSelectedItem().toString().substring(0, cmbMusteriAdiPlakaRapor.getSelectedItem().toString().indexOf("#")).replace("]", "").replace("#", "").trim();

                    hizmetDAO.musteriBazliRapor(tblRapor, musteri_id, format.format(ilkTarih), format.format(sonTarih));
                } else {
                    hizmetDAO.genelRapor(tblRapor, format.format(ilkTarih), format.format(sonTarih));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRaporGetirMstRaporActionPerformed

    private void btnExceleGonderMstRaporActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceleGonderMstRaporActionPerformed
        try {

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(dtIlkTarihMstRapor.getDate());

            java.sql.Date ilkTarih = new java.sql.Date(calendar.getTimeInMillis());

            calendar = new GregorianCalendar();
            calendar.setTime(dtSonTarihMstRapor.getDate());

            java.sql.Date sonTarih = new java.sql.Date(calendar.getTimeInMillis());

            if (!chkTumMusteriler.isSelected()) {
                exportTable(tblRapor, new File("Raporlar/" + musteriAdi + "-Rapor" + "-" + format.format(ilkTarih) + "-" + format.format(sonTarih) + ".xls"));
            } else {
                exportTable(tblRapor, new File("Raporlar/GenelRapor" + "-" + format.format(ilkTarih) + "-" + format.format(sonTarih) + ".xls"));
            }
            JOptionPane.showMessageDialog(null, "Rapor oluşturuldu.", "Rapor", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExceleGonderMstRaporActionPerformed

    private void btnSilAracKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSilAracKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Arac arac = new Arac();
            arac.setAracId(arac_id);
            AracDAO aracDAO = new AracDAO();
            String msg = "";

            try {
                aracDAO.aracSil(arac);
                aracDAO.fillTable(tblArac, "");
                txtMusteriAdi.setText("");
                txtPlaka.setText("");
                cmbAracModel.setSelectedIndex(0);
                cmbDisRenk.setSelectedIndex(0);
                cmbIcRenk.setSelectedIndex(0);
                cmbAracDurumu.setSelectedIndex(0);
                txtAciklamaArac.setText("");
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSilAracKeyPressed

    private void btnSilAracActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilAracActionPerformed
        Arac arac = new Arac();
        String msg = "";
        try {
            arac.setAracId(arac_id);

            AracDAO aracDAO = new AracDAO();

            aracDAO.aracSil(arac);
            aracDAO.fillTable(tblArac, "");
            txtMusteriAdi.setText("");
            txtPlaka.setText("");
            cmbAracModel.setSelectedIndex(0);
            cmbDisRenk.setSelectedIndex(0);
            cmbIcRenk.setSelectedIndex(0);
            cmbAracDurumu.setSelectedIndex(0);
            txtAciklamaArac.setText("");

            cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
            cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSilAracActionPerformed

    private void btnEkleMstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEkleMstKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Musteri musteri = new Musteri();
            String msg = "Lütfen Müşteri Adı alanını boş bırakmayınız!";
            try {
                if ("".equals(txtMusteriAdi.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    musteri.setMusteriAdi(txtMusteriAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));
                    musteri.setTelefonNo(txtTelefonNo.getText());
                    musteri.setAciklama(txtAciklamaMst.getText());

                    MusteriDAO musteriDAO = new MusteriDAO();

                    musteriDAO.musteriEkle(musteri);
                    musteriDAO.fillTable(tblMusteri);
                    txtMusteriAdi.setText("");
                    txtTelefonNo.setText("");
                    txtAciklamaMst.setText("");

                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }//GEN-LAST:event_btnEkleMstKeyPressed

    private void btnEkleMstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEkleMstActionPerformed
        Musteri musteri = new Musteri();
        String msg = "Lütfen Müşteri Adı alanını boş bırakmayınız!";
        try {
            if ("".equals(txtMusteriAdi.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {
                musteri.setMusteriAdi(txtMusteriAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));
                musteri.setTelefonNo(txtTelefonNo.getText());
                musteri.setAciklama(txtAciklamaMst.getText());

                MusteriDAO musteriDAO = new MusteriDAO();

                musteriDAO.musteriEkle(musteri);
                musteriDAO.fillTable(tblMusteri);
                txtMusteriAdi.setText("");
                txtTelefonNo.setText("");
                txtAciklamaMst.setText("");

                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

            }
        }
    }//GEN-LAST:event_btnEkleMstActionPerformed

    private void tblMusteriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMusteriMouseClicked
        musteri_id = (int) tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 1);
        txtMusteriAdi.setText(tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 2).toString());
        if (tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 3) != null) {
            txtTelefonNo.setText(tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 3).toString());
        } else {
            txtTelefonNo.setText("");
        }

        if (tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 4) != null) {
            txtAciklamaMst.setText(tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 4).toString());
        } else {
            txtAciklamaMst.setText("");
        }

        try {
            aracDAO.fillTable(tblArac, musteri_id);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblMusteriMouseClicked

    private void txtMusteriAdiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMusteriAdiKeyTyped
        MusteriDAO musteriDAO = new MusteriDAO();

        try {
            musteriDAO.fillTable(tblMusteri, txtMusteriAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMusteriAdiKeyTyped

    private void cmbAracMarkaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAracMarkaItemStateChanged
        try {
            hmAracModel = modelDAO.hmAracModel(cmbAracMarka.getSelectedItem().toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        cmbAracModel.removeAllItems();

        cmbAracModel.addItem("Seçiniz");
        entrySetAracModel = hmAracModel.entrySet();
        itAracModel = entrySetAracModel.iterator();

        while (itAracModel.hasNext()) {
            Map.Entry me = (Map.Entry) itAracModel.next();
            cmbAracModel.addItem(me.getValue().toString());
        }
    }//GEN-LAST:event_cmbAracMarkaItemStateChanged

    private void txtPlakaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlakaKeyTyped
        AracDAO aracDAO = new AracDAO();

        try {
            aracDAO.fillTable(tblArac, txtPlaka.getText().trim().toUpperCase(new Locale("tr", "TR")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_txtPlakaKeyTyped

    private void btnGuncelle1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuncelle1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Arac arac = new Arac();
            String msg1 = "Lütfen araç tablosundan bir araç seçiniz ve plaka alanını boş bırakmayınız!";

            int aracModelId = 1;
            int aracDisRenkId = 1;
            int aracIcRenkId = 1;

            try {
                if ("".equals(txtPlaka.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (arac_id == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {

                    arac.setMusteriId(musteri_id);

                    arac.setAracId(arac_id);

                    arac.setPlaka(txtPlaka.getText().trim().toUpperCase(new Locale("tr", "TR")));

                    itAracModel = entrySetAracModel.iterator();

                    while (itAracModel.hasNext()) {
                        Map.Entry me = (Map.Entry) itAracModel.next();
                        if (me.getValue().toString().equals(cmbAracModel.getSelectedItem().toString())) {
                            aracModelId = (int) me.getKey();
                        }
                    }
                    arac.setModelId(aracModelId);

                    itRenk = entrySetRenk.iterator();

                    while (itRenk.hasNext()) {
                        Map.Entry me = (Map.Entry) itRenk.next();
                        if (me.getValue().toString().equals(cmbDisRenk.getSelectedItem().toString())) {
                            aracDisRenkId = (int) me.getKey();
                        }
                        if (me.getValue().toString().equals(cmbIcRenk.getSelectedItem().toString())) {
                            aracIcRenkId = (int) me.getKey();
                        }
                    }

                    arac.setDisRenkId(aracDisRenkId);
                    arac.setIcRenkId(aracIcRenkId);

                    if (cmbAracDurumu.getSelectedIndex() == 1) {
                        arac.setAracDurumId(1);
                    } else if (cmbAracDurumu.getSelectedIndex() == 2) {
                        arac.setAracDurumId(2);
                    }

                    arac.setAciklama(txtAciklamaArac.getText());

                    AracDAO aracDAO = new AracDAO();

                    aracDAO.aracGuncelle(arac);
                    aracDAO.fillTable(tblArac, "");
                    txtPlaka.setText("");
                    cmbAracModel.setSelectedIndex(0);
                    cmbDisRenk.setSelectedIndex(0);
                    cmbIcRenk.setSelectedIndex(0);
                    cmbAracDurumu.setSelectedIndex(0);
                    txtAciklamaArac.setText("");

                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnGuncelle1KeyPressed

    private void btnGuncelle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuncelle1ActionPerformed
        Arac arac = new Arac();
        String msg1 = "Lütfen araç tablosundan bir araç seçiniz ve plaka alanını boş bırakmayınız!";

        int aracModelId = 1;
        int aracDisRenkId = 1;
        int aracIcRenkId = 1;

        try {
            if ("".equals(txtPlaka.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (arac_id == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                arac.setMusteriId(musteri_id);

                arac.setAracId(arac_id);

                arac.setPlaka(txtPlaka.getText().trim().toUpperCase(new Locale("tr", "TR")));

                itAracModel = entrySetAracModel.iterator();

                while (itAracModel.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracModel.next();
                    if (me.getValue().toString().equals(cmbAracModel.getSelectedItem().toString())) {
                        aracModelId = (int) me.getKey();
                    }
                }
                arac.setModelId(aracModelId);

                itRenk = entrySetRenk.iterator();

                while (itRenk.hasNext()) {
                    Map.Entry me = (Map.Entry) itRenk.next();
                    if (me.getValue().toString().equals(cmbDisRenk.getSelectedItem().toString())) {
                        aracDisRenkId = (int) me.getKey();
                    }
                    if (me.getValue().toString().equals(cmbIcRenk.getSelectedItem().toString())) {
                        aracIcRenkId = (int) me.getKey();
                    }
                }

                arac.setDisRenkId(aracDisRenkId);
                arac.setIcRenkId(aracIcRenkId);

                if (cmbAracDurumu.getSelectedIndex() == 1) {
                    arac.setAracDurumId(1);
                } else if (cmbAracDurumu.getSelectedIndex() == 2) {
                    arac.setAracDurumId(2);
                }

                arac.setAciklama(txtAciklamaArac.getText());

                AracDAO aracDAO = new AracDAO();

                aracDAO.aracGuncelle(arac);
                aracDAO.fillTable(tblArac, "");
                txtPlaka.setText("");
                cmbAracModel.setSelectedIndex(0);
                cmbDisRenk.setSelectedIndex(0);
                cmbIcRenk.setSelectedIndex(0);
                cmbAracDurumu.setSelectedIndex(0);
                txtAciklamaArac.setText("");

                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnGuncelle1ActionPerformed

    private void btnGuncelleMstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuncelleMstKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Musteri musteri = new Musteri();
            String msg = "Lütfen Müşteri Adı alanını boş bırakmayınız!";
            try {
                if ("".equals(txtMusteriAdi.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    musteri.setMusteriAdi(txtMusteriAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));
                    musteri.setTelefonNo(txtTelefonNo.getText());
                    musteri.setAciklama(txtAciklamaMst.getText());
                    musteri.setMusteriId(musteri_id);

                    MusteriDAO musteriDAO = new MusteriDAO();

                    musteriDAO.musteriGuncelle(musteri);
                    musteriDAO.fillTable(tblMusteri);
                    txtMusteriAdi.setText("");
                    txtTelefonNo.setText("");
                    txtAciklamaMst.setText("");

                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
                }

            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }//GEN-LAST:event_btnGuncelleMstKeyPressed

    private void btnGuncelleMstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuncelleMstActionPerformed
        Musteri musteri = new Musteri();
        String msg = "Lütfen Müşteri Adı alanını boş bırakmayınız!";
        try {
            if ("".equals(txtMusteriAdi.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {
                musteri.setMusteriAdi(txtMusteriAdi.getText().trim().toUpperCase(new Locale("tr", "TR")));
                musteri.setTelefonNo(txtTelefonNo.getText());
                musteri.setAciklama(txtAciklamaMst.getText());
                musteri.setMusteriId(musteri_id);

                MusteriDAO musteriDAO = new MusteriDAO();

                musteriDAO.musteriGuncelle(musteri);
                musteriDAO.fillTable(tblMusteri);
                txtMusteriAdi.setText("");
                txtTelefonNo.setText("");
                txtAciklamaMst.setText("");

                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
            }

        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);

            }
        }
    }//GEN-LAST:event_btnGuncelleMstActionPerformed

    private void btnSilMstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSilMstKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Musteri musteri = new Musteri();
            musteri.setMusteriId(musteri_id);
            MusteriDAO musteriDAO = new MusteriDAO();
            String msg = "";

            try {
                musteriDAO.musteriSil(musteri);
                musteriDAO.fillTable(tblMusteri);
                txtMusteriAdi.setText("");
                txtTelefonNo.setText("");
                txtAciklamaMst.setText("");
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSilMstKeyPressed

    private void btnSilMstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilMstActionPerformed
        Musteri musteri = new Musteri();
        musteri.setMusteriId(musteri_id);
        MusteriDAO musteriDAO = new MusteriDAO();
        String msg = "";

        try {
            musteriDAO.musteriSil(musteri);
            musteriDAO.fillTable(tblMusteri);
            txtMusteriAdi.setText("");
            txtTelefonNo.setText("");
            txtAciklamaMst.setText("");

            cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
            cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSilMstActionPerformed

    private void tblAracMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAracMouseClicked
        musteri_id = (int) tblArac.getValueAt(tblArac.getSelectedRow(), 1);
        arac_id = (int) tblArac.getValueAt(tblArac.getSelectedRow(), 3);
        txtPlaka.setText(tblArac.getValueAt(tblArac.getSelectedRow(), 4).toString());

        for (int i = 0; i < cmbAracMarka.getItemCount(); i++) {
            if (cmbAracMarka.getItemAt(i).equals(tblArac.getValueAt(tblArac.getSelectedRow(), 5))) {
                cmbAracMarka.setSelectedIndex(i);
            }
        }

        for (int i = 0; i < cmbAracModel.getItemCount(); i++) {
            if (cmbAracModel.getItemAt(i).equals(tblArac.getValueAt(tblArac.getSelectedRow(), 6))) {
                cmbAracModel.setSelectedIndex(i);
            }
        }

        for (int i = 0; i < cmbDisRenk.getItemCount(); i++) {
            if (cmbDisRenk.getItemAt(i).equals(tblArac.getValueAt(tblArac.getSelectedRow(), 7))) {
                cmbDisRenk.setSelectedIndex(i);
            }
        }

        for (int i = 0; i < cmbIcRenk.getItemCount(); i++) {
            if (cmbIcRenk.getItemAt(i).equals(tblArac.getValueAt(tblArac.getSelectedRow(), 8))) {
                cmbIcRenk.setSelectedIndex(i);
            }
        }

        if (tblArac.getValueAt(tblArac.getSelectedRow(), 9).equals("AKTİF")) {
            cmbAracDurumu.setSelectedIndex(1);
        } else {
            cmbAracDurumu.setSelectedIndex(2);
        }

        if (tblArac.getValueAt(tblArac.getSelectedRow(), 10) != null) {
            txtAciklamaArac.setText(tblArac.getValueAt(tblArac.getSelectedRow(), 10).toString());
        } else {
            txtAciklamaArac.setText("");
        }
    }//GEN-LAST:event_tblAracMouseClicked

    private void btnEkle1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEkle1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Arac arac = new Arac();
            String msg1 = "Lütfen müşteri tablosundan bir müşteri seçiniz!";
            String msg2 = "Lütfen aracın marka ve modelini seçiniz!";
            String msg3 = "Lütfen aracın dış rengini seçiniz!";
            String msg4 = "Lütfen aracın iç rengini seçiniz!";
            String msg5 = "Lütfen aracın durumunu seçiniz!";
            String msg6 = "Lütfen plaka alanını boş bırakmayınız!";

            int aracModelId = 1;
            int aracDisRenkId = 1;
            int aracIcRenkId = 1;

            try {
                if (txtMusteriAdi.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(txtPlaka.getText().trim())) {
                    JOptionPane.showMessageDialog(null, msg6, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (cmbAracModel.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (cmbDisRenk.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (cmbIcRenk.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg4, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (cmbAracDurumu.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg5, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {

                    arac.setMusteriId((int) tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 1));

                    arac.setPlaka(txtPlaka.getText().trim().toUpperCase(new Locale("tr", "TR")));

                    itAracModel = entrySetAracModel.iterator();
                    while (itAracModel.hasNext()) {
                        Map.Entry me = (Map.Entry) itAracModel.next();
                        if (me.getValue().toString().equals(cmbAracModel.getSelectedItem().toString())) {
                            aracModelId = (int) me.getKey();
                        }
                    }
                    arac.setModelId(aracModelId);

                    itRenk = entrySetRenk.iterator();
                    while (itRenk.hasNext()) {
                        Map.Entry me = (Map.Entry) itRenk.next();
                        if (me.getValue().toString().equals(cmbDisRenk.getSelectedItem().toString())) {
                            aracDisRenkId = (int) me.getKey();
                        }
                        if (me.getValue().toString().equals(cmbIcRenk.getSelectedItem().toString())) {
                            aracIcRenkId = (int) me.getKey();
                        }
                    }

                    arac.setDisRenkId(aracDisRenkId);
                    arac.setIcRenkId(aracIcRenkId);

                    if (cmbAracDurumu.getSelectedIndex() == 1) {
                        arac.setAracDurumId(1);
                    } else if (cmbAracDurumu.getSelectedIndex() == 2) {
                        arac.setAracDurumId(2);
                    }

                    arac.setAciklama(txtAciklamaArac.getText());

                    AracDAO aracDAO = new AracDAO();

                    aracDAO.aracEkle(arac);
                    aracDAO.fillTable(tblArac, "");
                    txtPlaka.setText("");
                    cmbAracModel.setSelectedIndex(0);
                    cmbDisRenk.setSelectedIndex(0);
                    cmbIcRenk.setSelectedIndex(0);
                    cmbAracDurumu.setSelectedIndex(0);

                    txtAciklamaArac.setText("");

                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                    cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg1 = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
                }
            }
    }//GEN-LAST:event_btnEkle1KeyPressed
    }
    private void btnEkle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEkle1ActionPerformed
        Arac arac = new Arac();
        String msg1 = "Lütfen müşteri tablosundan bir müşteri seçiniz!";
        String msg6 = "Lütfen plaka alanını boş bırakmayınız!";

        int aracModelId = 1486;
        int aracDisRenkId = 1;
        int aracIcRenkId = 1;

        try {
            if (txtMusteriAdi.getText().equals("")) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(txtPlaka.getText().trim())) {
                JOptionPane.showMessageDialog(null, msg6, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                arac.setMusteriId((int) tblMusteri.getValueAt(tblMusteri.getSelectedRow(), 1));

                arac.setPlaka(txtPlaka.getText().trim().toUpperCase(new Locale("tr", "TR")));

                itAracModel = entrySetAracModel.iterator();
                while (itAracModel.hasNext()) {
                    Map.Entry me = (Map.Entry) itAracModel.next();
                    if (me.getValue().toString().equals(cmbAracModel.getSelectedItem().toString())) {
                        aracModelId = (int) me.getKey();
                    }
                }
                arac.setModelId(aracModelId);

                itRenk = entrySetRenk.iterator();
                while (itRenk.hasNext()) {
                    Map.Entry me = (Map.Entry) itRenk.next();
                    if (me.getValue().toString().equals(cmbDisRenk.getSelectedItem().toString())) {
                        aracDisRenkId = (int) me.getKey();
                    }
                    if (me.getValue().toString().equals(cmbIcRenk.getSelectedItem().toString())) {
                        aracIcRenkId = (int) me.getKey();
                    }
                }

                arac.setDisRenkId(aracDisRenkId);
                arac.setIcRenkId(aracIcRenkId);

                switch (cmbAracDurumu.getSelectedIndex()) {
                    case 1:
                        arac.setAracDurumId(1);
                        break;
                    case 2:
                        arac.setAracDurumId(2);
                        break;
                    default:
                        arac.setAracDurumId(1);
                        break;
                }

                arac.setAciklama(txtAciklamaArac.getText());

                AracDAO aracDAO = new AracDAO();

                aracDAO.aracEkle(arac);
                aracDAO.fillTable(tblArac, "");
                txtPlaka.setText("");
                cmbAracModel.setSelectedIndex(0);
                cmbDisRenk.setSelectedIndex(0);
                cmbIcRenk.setSelectedIndex(0);
                cmbAracDurumu.setSelectedIndex(0);

                txtAciklamaArac.setText("");

                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlaka);
                cmbMusteriAdiPlakaDoldur(cmbMusteriAdiPlakaRapor);
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg1 = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEkle1ActionPerformed

    private void txtTelefonNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonNoKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelefonNoKeyTyped

    private void tblHizmetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHizmetMouseClicked
        try {

            for (int i = 0; i < cmbMusteriAdiPlaka.getItemCount(); i++) {
                String cmbIsim = cmbMusteriAdiPlaka.getItemAt(i).substring(0, cmbMusteriAdiPlaka.getItemAt(i).indexOf("#")).trim();
                String tblIsim = tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 2).toString();

                if (cmbIsim.equals(tblIsim)) {
                    String cmbPlaka = cmbMusteriAdiPlaka.getItemAt(i).substring(cmbMusteriAdiPlaka.getItemAt(i).indexOf("#")).replace("#", "").trim();
                    String tblPlaka = tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 3).toString();

                    if (cmbPlaka.equals(tblPlaka)) {
                        cmbMusteriAdiPlaka.setSelectedIndex(i);
                        break;
                    }
                }
            }

            hizmetId = (int) tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 1);

            for (int i = 0; i < cmbFaaliyet.getItemCount(); i++) {
                if (cmbFaaliyet.getItemAt(i).equals(tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 4))) {
                    cmbFaaliyet.setSelectedIndex(i);
                    break;
                }
            }

            txtUcret.setText(tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 5).toString());
            txtPuan.setText(tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 6).toString());

            if (tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 6).toString().startsWith("-")) {
                rdbPuanKullan.setSelected(true);
                rdbPuanEkle.setSelected(false);
            } else {
                rdbPuanKullan.setSelected(false);
                rdbPuanEkle.setSelected(true);
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date date = null;

            try {
                date = (java.util.Date) format.parse(tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 7).toString());
            } catch (ParseException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            }
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            dtIslemTarihi.setDate(date);

            txtAciklama.setText(tblHizmet.getValueAt(tblHizmet.getSelectedRow(), 8).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblHizmetMouseClicked

    private void btnSilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSilKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Hizmet hizmet = new Hizmet();
            String msg1 = "Lütfen araç tablosundan bir araç seçiniz!";
            try {

                if (arac.getAracId() == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    hizmet.setHizmetId(hizmetId);

                    HizmetDAO hizmetDAO = new HizmetDAO();

                    hizmetDAO.hizmetSil(hizmet);
                    hizmetDAO.hizmetGetir(tblHizmet);

                    cmbFaaliyet.setSelectedIndex(0);
                    txtUcret.setText("0");
                    txtPuan.setText("0");
                    rdbPuanEkle.setSelected(true);
                    rdbPuanKullan.setSelected(false);
                    txtAciklama.setText("");

                    //int musteriId = (int) tblArac.getValueAt(tblArac.getSelectedRow(), 1);
                    lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByAracID(arac.getAracId())));
                    getToplamGunlukKazanc();
                }

            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_btnSilKeyPressed

    private void btnSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilActionPerformed
        Hizmet hizmet = new Hizmet();
        String msg1 = "Lütfen araç tablosundan bir araç seçiniz!";

        int silmeUyarisiCevabi = 1;
        String msg2 = "Seçmiş olduğunuz hizmet kaydını silmek istediğinizden emin misiniz?";
        try {

            if (arac.getAracId() == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {
                silmeUyarisiCevabi = JOptionPane.showConfirmDialog(null, msg2, "Kayıt Silme Uyarısı", JOptionPane.YES_NO_OPTION);
                if (silmeUyarisiCevabi == 0) {
                    hizmet.setHizmetId(hizmetId);

                    HizmetDAO hizmetDAO = new HizmetDAO();

                    hizmetDAO.hizmetSil(hizmet);
                    hizmetDAO.hizmetGetir(tblHizmet);

                    cmbFaaliyet.setSelectedIndex(0);
                    txtUcret.setText("0");
                    txtPuan.setText("0");
                    rdbPuanEkle.setSelected(true);
                    rdbPuanKullan.setSelected(false);
                    txtAciklama.setText("");

                    //int musteriId = (int) tblArac.getValueAt(tblArac.getSelectedRow(), 1);
                    lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByAracID(arac.getAracId())));
                    getToplamGunlukKazanc();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSilActionPerformed

    private void btnGuncelleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuncelleKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Hizmet hizmet = new Hizmet();
            String msg1 = "Lütfen hizmet tablosundan bir kayıt seçiniz!";
            String msg2 = "Lütfen işlem seçiniz!";
            String msg3 = "Lütfen tarih giriniz!";

            int faaliyetId = 0;
            float puan = 0;

            try {
                if (hizmetId == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (cmbFaaliyet.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (dtIslemTarihi.getDate().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {

                    hizmet.setHizmetId(hizmetId);

                    itFaaliyet = entrySetFaaliyet.iterator();
                    while (itFaaliyet.hasNext()) {
                        Map.Entry me = (Map.Entry) itFaaliyet.next();
                        if (me.getValue().toString().equals(cmbFaaliyet.getSelectedItem().toString())) {
                            faaliyetId = (int) me.getKey();
                            break;
                        }
                    }
                    hizmet.setFaaliyetId(faaliyetId);
                    hizmet.setUcret(Integer.parseInt(txtUcret.getText()));
                    puan = Float.valueOf(txtPuan.getText());

                    if (rdbPuanKullan.isSelected() && puan > 0) {
                        puan = -puan;
                    }

                    if (rdbPuanEkle.isSelected() && puan < 0) {
                        puan = -puan;
                    }

                    hizmet.setPuan(puan);

                    java.sql.Date sqlDate = new java.sql.Date(dtIslemTarihi.getDate().getTime());

                    hizmet.setHizmetTarihi(sqlDate);

                    hizmet.setAciklama(txtAciklama.getText());

                    HizmetDAO hizmetDAO = new HizmetDAO();

                    hizmetDAO.hizmetGuncelle(hizmet);
                    hizmetDAO.hizmetGetir(tblHizmet);

                    cmbFaaliyet.setSelectedIndex(0);

                    txtAciklama.setText("");

                    int musteriId = 0;
                    musteriId = arac.getMusteriId();
                    lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByMusteriID(musteriId)));
                    getToplamGunlukKazanc();
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg1 = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    ex.printStackTrace();

                    msg1 = "Veritabanına yazarken hata alındı!";
                    JOptionPane.showMessageDialog(null, msg1, "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnGuncelleKeyPressed

    private void btnGuncelleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuncelleActionPerformed
        Hizmet hizmet = new Hizmet();
        String msg1 = "Lütfen hizmet tablosundan bir kayıt seçiniz!";
        String msg2 = "Lütfen işlem seçiniz!";
        String msg3 = "Lütfen tarih giriniz!";

        int faaliyetId = 0;
        float puan = 0;

        try {
            if (hizmetId == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (cmbFaaliyet.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (dtIslemTarihi.getDate().toString().equals("")) {
                JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                hizmet.setHizmetId(hizmetId);

                itFaaliyet = entrySetFaaliyet.iterator();
                while (itFaaliyet.hasNext()) {
                    Map.Entry me = (Map.Entry) itFaaliyet.next();
                    if (me.getValue().toString().equals(cmbFaaliyet.getSelectedItem().toString())) {
                        faaliyetId = (int) me.getKey();
                        break;
                    }
                }
                hizmet.setFaaliyetId(faaliyetId);
                hizmet.setUcret(Integer.parseInt(txtUcret.getText()));
                puan = Float.valueOf(txtPuan.getText());

                if (rdbPuanKullan.isSelected() && puan > 0) {
                    puan = -puan;
                }

                if (rdbPuanEkle.isSelected() && puan < 0) {
                    puan = -puan;
                }

                hizmet.setPuan(puan);

                //                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                //                GregorianCalendar calendar = new GregorianCalendar();
                //                calendar.setTime(dtIslemTarihi.getDateFormat().getCalendar().getTime());
                java.sql.Date sqlDate = new java.sql.Date(dtIslemTarihi.getDate().getTime());

                hizmet.setHizmetTarihi(sqlDate);

                hizmet.setAciklama(txtAciklama.getText());

                HizmetDAO hizmetDAO = new HizmetDAO();

                hizmetDAO.hizmetGuncelle(hizmet);
                hizmetDAO.hizmetGetir(tblHizmet);

                cmbFaaliyet.setSelectedIndex(0);

                txtAciklama.setText("");

                int musteriId = 0;

                musteriId = arac.getMusteriId();
                lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByMusteriID(musteriId)));
                getToplamGunlukKazanc();
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg1 = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {
                ex.printStackTrace();

                msg1 = "Veritabanına yazarken hata alındı!";
                JOptionPane.showMessageDialog(null, msg1, "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnGuncelleActionPerformed

    private void btnEkleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEkleKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Hizmet hizmet = new Hizmet();
            String msg1 = "Lütfen araç tablosundan bir araç seçiniz!";
            String msg2 = "Lütfen işlem seçiniz!";
            String msg3 = "Lütfen tarih giriniz!";

            int faaliyetId = 0;
            float puan = 0;

            try {
                if (arac.getAracId() == 0) {
                    JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (cmbFaaliyet.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else if (dtIslemTarihi.getDate().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {

                    hizmet.setAracId(arac.getAracId());

                    itFaaliyet = entrySetFaaliyet.iterator();
                    while (itFaaliyet.hasNext()) {
                        Map.Entry me = (Map.Entry) itFaaliyet.next();
                        if (me.getValue().toString().equals(cmbFaaliyet.getSelectedItem().toString())) {
                            faaliyetId = (int) me.getKey();
                            break;
                        }
                    }
                    hizmet.setFaaliyetId(faaliyetId);
                    hizmet.setUcret(Integer.parseInt(txtUcret.getText()));
                    puan = Float.valueOf(txtPuan.getText());

                    if (rdbPuanKullan.isSelected() && puan > 0) {
                        puan = -puan;
                    }

                    if (rdbPuanEkle.isSelected() && puan < 0) {
                        puan = -puan;
                    }

                    hizmet.setPuan(puan);

                    java.sql.Date sqlDate = new java.sql.Date(dtIslemTarihi.getDate().getTime());

                    hizmet.setHizmetTarihi(sqlDate);

                    hizmet.setAciklama(txtAciklama.getText());

                    HizmetDAO hizmetDAO = new HizmetDAO();

                    hizmetDAO.hizmetEkle(hizmet);
                    hizmetDAO.hizmetGetir(tblHizmet);

                    cmbFaaliyet.setSelectedIndex(0);

                    txtAciklama.setText("");

                    int musteriId = arac.getMusteriId();

                    lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByMusteriID(musteriId)));
                    getToplamGunlukKazanc();
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    System.out.println("Bu kayıt veritabanında mevcut!");

                    msg1 = "Bu kayıt veritabanında mevcut!";

                    JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
                } else {
                    ex.printStackTrace();

                    msg1 = "Veritabanına yazarken hata alındı!";
                    JOptionPane.showMessageDialog(null, msg1, "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnEkleKeyPressed

    private void btnEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEkleActionPerformed
        Hizmet hizmet = new Hizmet();
        String msg1 = "Lütfen araç tablosundan bir araç seçiniz!";
        String msg2 = "Lütfen işlem seçiniz!";
        String msg3 = "Lütfen tarih giriniz!";

        int faaliyetId = 0;
        float puan = 0;

        try {
            if (arac.getAracId() == 0) {
                JOptionPane.showMessageDialog(null, msg1, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (cmbFaaliyet.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, msg2, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else if (dtIslemTarihi.getDate().toString().equals("")) {
                JOptionPane.showMessageDialog(null, msg3, "Gerekli Alan Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {

                hizmet.setAracId(arac.getAracId());

                itFaaliyet = entrySetFaaliyet.iterator();
                while (itFaaliyet.hasNext()) {
                    Map.Entry me = (Map.Entry) itFaaliyet.next();
                    if (me.getValue().toString().equals(cmbFaaliyet.getSelectedItem().toString())) {
                        faaliyetId = (int) me.getKey();
                        break;
                    }
                }
                hizmet.setFaaliyetId(faaliyetId);

                hizmet.setUcret(Integer.parseInt(txtUcret.getText()));

                puan = Float.valueOf(txtPuan.getText());

                if (rdbPuanKullan.isSelected() && puan > 0) {
                    puan = -puan;
                }

                if (rdbPuanEkle.isSelected() && puan < 0) {
                    puan = -puan;
                }

                hizmet.setPuan(puan);

                //                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                //                GregorianCalendar calendar = new GregorianCalendar();
                //                calendar.setTime(format.parse(dtIslemTarihi.getText()));
                java.sql.Date sqlDate = new java.sql.Date(dtIslemTarihi.getDate().getTime());

                hizmet.setHizmetTarihi(sqlDate);

                hizmet.setAciklama(txtAciklama.getText());

                HizmetDAO hizmetDAO = new HizmetDAO();

                hizmetDAO.hizmetEkle(hizmet);
                //hizmetDAO.hizmetGetir(tblHizmet, arac.getAracId());
                hizmetDAO.hizmetGetir(tblHizmet);

                cmbFaaliyet.setSelectedIndex(0);
                txtUcret.setText("");
                txtPuan.setText("");
                txtAciklama.setText("");

                int musteriId = arac.getMusteriId();

                lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByMusteriID(musteriId)));
                getToplamGunlukKazanc();
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Duplicate")) {
                System.out.println("Bu kayıt veritabanında mevcut!");

                msg1 = "Bu kayıt veritabanında mevcut!";

                JOptionPane.showMessageDialog(null, msg1, "Mükerrer Kayıt Uyarısı", JOptionPane.ERROR_MESSAGE);
            } else {
                ex.printStackTrace();

                msg1 = "Veritabanına yazarken hata alındı!";
                JOptionPane.showMessageDialog(null, msg1, "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEkleActionPerformed

    private void rdbPuanKullanİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdbPuanKullanİtemStateChanged
        if (rdbPuanKullan.isSelected()) {
            rdbPuanEkle.setSelected(false);
            txtUcret.setText("0");
        }
    }//GEN-LAST:event_rdbPuanKullanİtemStateChanged

    private void rdbPuanEkleİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdbPuanEkleİtemStateChanged
        if (rdbPuanEkle.isSelected()) {
            rdbPuanKullan.setSelected(false);
            /////
            Faaliyet faaliyet = new Faaliyet();
            FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

            try {
                faaliyet = faaliyetDAO.getFaaliyet(cmbFaaliyet.getSelectedItem().toString());
                //txtUcret.setText(String.valueOf(faaliyet.getUcret()));//Ucret aracın kategorisine göre cekilecek
                txtPuan.setText(String.valueOf(faaliyet.getPuan()));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
            }

            Ucret ucret = new Ucret();
            FiyatListesiDAO fiyatListesiDAO = new FiyatListesiDAO();

            if (cmbFaaliyet.getSelectedIndex() != 0) {
                itFaaliyet = entrySetFaaliyet.iterator();
                while (itFaaliyet.hasNext()) {
                    Map.Entry me = (Map.Entry) itFaaliyet.next();
                    if (me.getValue().toString().equals(cmbFaaliyet.getSelectedItem().toString())) {
                        faaliyetId = (int) me.getKey();
                        break;
                    }
                }
            }

            if (arac != null) {
                ucret = fiyatListesiDAO.getUcret(arac.getAracKategoriId(), faaliyetId);
                txtUcret.setText(String.valueOf(ucret.getUcret()));
            }
            //////
        }
    }//GEN-LAST:event_rdbPuanEkleİtemStateChanged

    private void txtPuanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPuanKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPuanKeyTyped

    private void txtPuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuanActionPerformed

    private void txtUcretKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUcretKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtUcretKeyTyped

    private void txtUcretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUcretActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUcretActionPerformed

    private void cmbFaaliyetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFaaliyetItemStateChanged

        Faaliyet faaliyet = new Faaliyet();
        FaaliyetDAO faaliyetDAO = new FaaliyetDAO();

        try {
            faaliyet = faaliyetDAO.getFaaliyet(cmbFaaliyet.getSelectedItem().toString());
            //txtUcret.setText(String.valueOf(faaliyet.getUcret()));//Ucret aracın kategorisine göre cekilecek
            txtPuan.setText(String.valueOf(faaliyet.getPuan()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        Ucret ucret = new Ucret();
        FiyatListesiDAO fiyatListesiDAO = new FiyatListesiDAO();

        if (cmbFaaliyet.getSelectedIndex() != 0) {
            itFaaliyet = entrySetFaaliyet.iterator();
            while (itFaaliyet.hasNext()) {
                Map.Entry me = (Map.Entry) itFaaliyet.next();
                if (me.getValue().toString().equals(cmbFaaliyet.getSelectedItem().toString())) {
                    faaliyetId = (int) me.getKey();
                    break;
                }
            }
        }

        if (arac != null) {
            ucret = fiyatListesiDAO.getUcret(arac.getAracKategoriId(), faaliyetId);
            txtUcret.setText(String.valueOf(ucret.getUcret()));
        }
    }//GEN-LAST:event_cmbFaaliyetItemStateChanged

    private void cmbMusteriAdiPlakaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMusteriAdiPlakaKeyTyped
        ///////////////////YENİ EKLENEN MÜŞTERİYİ GÖREBİLMEK İÇİN
        MusteriDAO musteriDAO = new MusteriDAO();

        try {
            hmMusteri = musteriDAO.hmMusteri();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        entrySetMusteri = hmMusteri.entrySet();

        itMusteri = entrySetMusteri.iterator();

        while (itMusteri.hasNext()) {
            Map.Entry me = (Map.Entry) itMusteri.next();
            cmbMusteriAdiPlaka.addItem(me.getValue().toString());
        }
        AutoCompleteDecorator.decorate(cmbMusteriAdiPlaka);
        /////////////////////

        arac = new Arac();
        AracDAO aracDAO = new AracDAO();
        HizmetDAO hizmetDAO = new HizmetDAO();

        plaka = new String();

        if (cmbMusteriAdiPlaka.toString().contains("#")) {
            plaka = cmbMusteriAdiPlaka.toString().substring(cmbMusteriAdiPlaka.toString().indexOf("#")).replace("]", "").replace("#", "").trim();
        }

        try {
            arac = aracDAO.getArac(plaka);
            lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByMusteriID(arac.getMusteriId())));
        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cmbMusteriAdiPlakaKeyTyped

    private void cmbMusteriAdiPlakaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMusteriAdiPlakaItemStateChanged

        arac = new Arac();
        AracDAO aracDAO = new AracDAO();
        HizmetDAO hizmetDAO = new HizmetDAO();

        plaka = new String();

        if (cmbMusteriAdiPlaka.toString().contains("#")) {
            plaka = cmbMusteriAdiPlaka.toString().substring(cmbMusteriAdiPlaka.toString().indexOf("#")).replace("]", "").replace("#", "").trim();
        }

        try {
            arac = aracDAO.getArac(plaka);
            lblToplamPuan.setText(String.valueOf(hizmetDAO.toplamPuanGetirByMusteriID(arac.getMusteriId())));

        } catch (IOException ex) {
            Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (programBasladi == true) {
            cmbFaaliyet.setSelectedIndex(0);
            txtUcret.setText("0");
            rdbPuanEkle.setSelected(true);
        }
    }//GEN-LAST:event_cmbMusteriAdiPlakaItemStateChanged
    public void exportTable(JTable table, File file) throws IOException {

        TableModel model = table.getModel();
        FileWriter out = new FileWriter(file);
        for (int i = 0; i < model.getColumnCount(); i++) {
            out.write(model.getColumnName(i) + "\t");
        }
        out.write("\n");

        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                out.write(model.getValueAt(i, j).toString() + "\t");
            }
            out.write("\n");
        }

        out.close();
        System.out.println("write out to: " + file);
    }

    String path = null;

    public void getToplamGunlukKazanc() {
        HizmetDAO hizmetDAO = new HizmetDAO();
        int toplamGunlukKazanc = 0;

        toplamGunlukKazanc = hizmetDAO.toplamGunlukKazanc();
        lblToplamGunlukKazanc.setText(String.valueOf(toplamGunlukKazanc) + " TL");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Temizis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Temizis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Temizis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Temizis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Properties props = new Properties();
                    FileInputStream fis = null;
                    String pcMacAddress = "";
                    String decryptedMacAddress = "";

                    try {
                        fis = new FileInputStream("app.properties");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        props.load(fis);
                    } catch (IOException ex) {
                        Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        decryptedMacAddress = AESenc.decrypt(props.getProperty("pc.mac_address").trim());
                    } catch (Exception ex) {
                        Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        pcMacAddress = MacAddress.getMacAddress();
                    } catch (IOException ex) {
                        Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (pcMacAddress.equals(decryptedMacAddress)) {
                        new Temizis().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Bilgisayarınızın mac adresi ile şifrelenmiş mac adresi uyuşmuyor!", "Mac Adresi Hatası", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Temizis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEkle;
    private javax.swing.JButton btnEkle1;
    private javax.swing.JButton btnEkle2;
    private javax.swing.JButton btnEkleModel;
    private javax.swing.JButton btnEkleMst;
    private javax.swing.JButton btnExceleGonder1;
    private javax.swing.JButton btnExceleGonderMstRapor;
    private javax.swing.JButton btnGuncelle;
    private javax.swing.JButton btnGuncelle1;
    private javax.swing.JButton btnGuncelle2;
    private javax.swing.JButton btnGuncelleModel;
    private javax.swing.JButton btnGuncelleMst;
    private javax.swing.JButton btnKaydet;
    private javax.swing.JButton btnRaporGetirMstRapor;
    private javax.swing.JButton btnSil;
    private javax.swing.JButton btnSil1;
    private javax.swing.JButton btnSil3;
    private javax.swing.JButton btnSilArac;
    private javax.swing.JButton btnSilModel;
    private javax.swing.JButton btnSilMst;
    private javax.swing.JCheckBox chkTumMusteriler;
    private javax.swing.JComboBox<String> cmbAracDurumu;
    private javax.swing.JComboBox<String> cmbAracKategorisi;
    private javax.swing.JComboBox<String> cmbAracKategorisiFiyatListesi;
    private javax.swing.JComboBox<String> cmbAracMarka;
    private javax.swing.JComboBox<String> cmbAracMarkaModelTanimi;
    private javax.swing.JComboBox<String> cmbAracModel;
    private javax.swing.JComboBox<String> cmbDisRenk;
    private javax.swing.JComboBox<String> cmbFaaliyet;
    private javax.swing.JComboBox<String> cmbFaaliyetFiyatListesi;
    private javax.swing.JComboBox<String> cmbIcRenk;
    private javax.swing.JComboBox<String> cmbMusteriAdiPlaka;
    private javax.swing.JComboBox<String> cmbMusteriAdiPlakaRapor;
    private com.toedter.calendar.JDateChooser dtIlkTarihMstRapor;
    private com.toedter.calendar.JDateChooser dtIslemTarihi;
    private com.toedter.calendar.JDateChooser dtSonTarihMstRapor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblSonuc;
    private javax.swing.JLabel lblToplamGunlukKazanc;
    private javax.swing.JLabel lblToplamPuan;
    private javax.swing.JRadioButton rdbPuanEkle;
    private javax.swing.JRadioButton rdbPuanKullan;
    private javax.swing.JTable tblArac;
    private javax.swing.JTable tblAracModel;
    private javax.swing.JTable tblFaaliyet;
    private javax.swing.JTable tblFiyatListesi;
    private javax.swing.JTable tblHizmet;
    private javax.swing.JTable tblMusteri;
    private javax.swing.JTable tblRapor;
    private javax.swing.JTextField txtAciklama;
    private javax.swing.JTextField txtAciklamaArac;
    private javax.swing.JTextField txtAciklamaFiyatListesi;
    private javax.swing.JTextField txtAciklamaMst;
    private javax.swing.JTextField txtFaaliyetAdi;
    private javax.swing.JTextField txtModelAdi;
    private javax.swing.JTextField txtMusteriAdi;
    private javax.swing.JTextField txtPlaka;
    private javax.swing.JTextField txtPuan;
    private javax.swing.JTextField txtPuanHizmetler;
    private javax.swing.JTextField txtTelefonNo;
    private javax.swing.JTextField txtUcret;
    private javax.swing.JTextField txtUcretFiyatListesi;
    // End of variables declaration//GEN-END:variables
}
