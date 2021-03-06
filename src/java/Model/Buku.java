/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Control.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author budhidarmap
 */
public class Buku {

    private String ISBN;
    private String Judul;
    private String Penulis;
    private String Tahun_Terbit;
    private String Penerbit;
    private int Copy;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String Judul) {
        this.Judul = Judul;
    }

    public String getPenulis() {
        return Penulis;
    }

    public void setPenulis(String Penulis) {
        this.Penulis = Penulis;
    }

    public String getTahun_Terbit() {
        return Tahun_Terbit;
    }

    public void setTahun_Terbit(String Tahun_Terbit) {
        this.Tahun_Terbit = Tahun_Terbit;
    }

    public String getPenerbit() {
        return Penerbit;
    }

    public void setPenerbit(String Penerbit) {
        this.Penerbit = Penerbit;
    }

    public int getCopy() {
        return Copy;
    }

    public void setCopy(int Copy) {
        this.Copy = Copy;
    }

    public static String tambahBuku(Buku b) {
        String text = null;
        Connection conn = null;
        PreparedStatement ps = null;
        conn = DatabaseManager.getDBConnection();
        try {
            ps = conn.prepareCall("INSERT INTO BUKU VALUES"
                    + "(?,?,?,?,?,?)");
            ps.setString(1, b.getISBN());
            ps.setString(2, b.getJudul());
            ps.setString(3, b.getPenulis());
            ps.setString(4, b.getPenerbit());
            ps.setString(5, b.getTahun_Terbit());
            ps.setInt(6, b.getCopy());
            ps.executeUpdate();
            conn.commit();
            text = "Data sudah ditambahkan";

        } catch (SQLException ex) {
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
            }
        }
        return text;
    }

    public static String editBuku(Buku b) {
        String text = null;
        Connection conn = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        conn = DatabaseManager.getDBConnection();
        try {
            ps = conn.prepareCall("UPDATE BUKU SET"
                    + " JUDUL=?, PENULIS=?, PENERBIT=?, TAHUN=?,"
                    + "COPY=? WHERE ISBN=?");
            ps.setString(1, b.getJudul());
            ps.setString(2, b.getPenulis());
            ps.setString(3, b.getPenerbit());
            ps.setString(4, b.getTahun_Terbit());
            ps.setInt(5, b.getCopy());
            ps.setString(6, b.getISBN());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {

            }
        }
        return text;
    }

    public static void hapusBuku(String id) {
        String text = null;
        Connection conn = null;
        PreparedStatement ps = null;
        conn = DatabaseManager.getDBConnection();
        try {
            ps = conn.prepareCall("DELETE FROM BUKU "
                    + "WHERE ISBN='" + id + "'");
            ps.executeUpdate();
            conn.commit();
            text = "Data sudah dihapus";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String UpdateCopy(String isbn, int copy) {
        String text = null;
        Connection conn = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        conn = DatabaseManager.getDBConnection();
        try {
            if (copy >= 0) {
                ps = conn.prepareCall("UPDATE BUKU SET COPY=? WHERE ISBN=?");
                ps.setInt(1, copy);
                ps.setString(2, isbn);
                ps.executeUpdate();
                conn.commit();
            } else {
                return text;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return text;
    }

    public static int cekCopy(String isbn) {
        Connection conn = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        conn = DatabaseManager.getDBConnection();
        Buku b = new Buku();
        int sedia = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT COPY FROM BUKU "
                    + "WHERE ISBN='" + isbn + "'");
            rs.next();
            b.setCopy(rs.getInt(1));
            sedia = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return sedia;
    }

    public static Buku[] getListPencarian(String key) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        conn = DatabaseManager.getDBConnection();
        Buku bk[] = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT COUNT (*) TOTAL FROM BUKU "
                    + "WHERE ("
                    //isbn
                    + "ISBN LIKE '" + key + "%' OR  "
                    + "ISBN LIKE '%" + key + "%' OR  "
                    + "ISBN LIKE '%" + key + "' OR  "
                    //judul
                    + "JUDUL LIKE UPPER('" + key + "%') OR "
                    + "JUDUL LIKE LOWER('" + key + "%') OR "
                    + "JUDUL LIKE '" + key + "%' OR "
                    + "JUDUL LIKE UPPER('%" + key + "%') OR "
                    + "JUDUL LIKE LOWER('%" + key + "%') OR "
                    + "JUDUL LIKE '%" + key + "%' OR "
                    + "JUDUL LIKE UPPER('%" + key + "') OR "
                    + "JUDUL LIKE LOWER('%" + key + "') OR "
                    + "JUDUL LIKE '%" + key + "' OR "
                    //penulis
                    + "PENULIS LIKE UPPER('" + key + "%') OR "
                    + "PENULIS LIKE LOWER('" + key + "%') OR "
                    + "PENULIS LIKE '" + key + "%' OR "
                    + "PENULIS LIKE UPPER('%" + key + "%') OR "
                    + "PENULIS LIKE LOWER('%" + key + "%') OR "
                    + "PENULIS LIKE '%" + key + "%' OR "
                    + "PENULIS LIKE UPPER('%" + key + "') OR "
                    + "PENULIS LIKE LOWER('%" + key + "') OR "
                    + "PENULIS LIKE '%" + key + "' OR "
                    //penerbit
                    + "PENERBIT LIKE UPPER('" + key + "%') OR "
                    + "PENERBIT LIKE LOWER('" + key + "%') OR "
                    + "PENERBIT LIKE '%" + key + "%' OR "
                    + "PENERBIT LIKE UPPER('%" + key + "%') OR "
                    + "PENERBIT LIKE LOWER('%" + key + "%') OR "
                    + "PENERBIT LIKE '%" + key + "' OR "
                    + "PENERBIT LIKE UPPER('%" + key + "') OR "
                    + "PENERBIT LIKE LOWER('%" + key + "') OR "
                    //tahun
                    + "TAHUN LIKE '" + key + "%' OR "
                    + "TAHUN LIKE '%" + key + "%' OR "
                    + "TAHUN LIKE '%" + key + "') AND "
                    //copy
                    + "COPY > 0");
            rs.next();
            bk = new Buku[rs.getInt(1)];
            rs = st.executeQuery("SELECT * FROM BUKU "
                    + "WHERE ("
                    //isbn
                    + "ISBN LIKE '" + key + "%' OR  "
                    + "ISBN LIKE '%" + key + "%' OR  "
                    + "ISBN LIKE '%" + key + "' OR  "
                    //judul
                    + "JUDUL LIKE UPPER('" + key + "%') OR "
                    + "JUDUL LIKE LOWER('" + key + "%') OR "
                    + "JUDUL LIKE '" + key + "%' OR "
                    + "JUDUL LIKE UPPER('%" + key + "%') OR "
                    + "JUDUL LIKE LOWER('%" + key + "%') OR "
                    + "JUDUL LIKE '%" + key + "%' OR "
                    + "JUDUL LIKE UPPER('%" + key + "') OR "
                    + "JUDUL LIKE LOWER('%" + key + "') OR "
                    + "JUDUL LIKE '%" + key + "' OR "
                    //penulis
                    + "PENULIS LIKE UPPER('" + key + "%') OR "
                    + "PENULIS LIKE LOWER('" + key + "%') OR "
                    + "PENULIS LIKE '" + key + "%' OR "
                    + "PENULIS LIKE UPPER('%" + key + "%') OR "
                    + "PENULIS LIKE LOWER('%" + key + "%') OR "
                    + "PENULIS LIKE '%" + key + "%' OR "
                    + "PENULIS LIKE UPPER('%" + key + "') OR "
                    + "PENULIS LIKE LOWER('%" + key + "') OR "
                    + "PENULIS LIKE '%" + key + "' OR "
                    //penerbit
                    + "PENERBIT LIKE UPPER('" + key + "%') OR "
                    + "PENERBIT LIKE LOWER('" + key + "%') OR "
                    + "PENERBIT LIKE '%" + key + "%' OR "
                    + "PENERBIT LIKE UPPER('%" + key + "%') OR "
                    + "PENERBIT LIKE LOWER('%" + key + "%') OR "
                    + "PENERBIT LIKE '%" + key + "' OR "
                    + "PENERBIT LIKE UPPER('%" + key + "') OR "
                    + "PENERBIT LIKE LOWER('%" + key + "') OR "
                    //tahun
                    + "TAHUN LIKE '" + key + "%' OR "
                    + "TAHUN LIKE '%" + key + "%' OR "
                    + "TAHUN LIKE '%" + key + "') AND "
                    //copy
                    + "COPY > 0");
            int index = 0;
            while (rs.next()) {
                bk[index] = new Buku();
                bk[index].setISBN(rs.getString(1));
                bk[index].setJudul(rs.getString(2));
                bk[index].setPenulis(rs.getString(3));
                bk[index].setPenerbit(rs.getString(4));
                bk[index].setTahun_Terbit(rs.getString(5));
                bk[index].setCopy(rs.getInt(6));
                index++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bk;
    }

    public static Buku getBuku(String ISBN) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        conn = DatabaseManager.getDBConnection();
        Buku bk = new Buku();
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT COUNT (*) TOTAL FROM BUKU "
                    + "WHERE ("
                    + "ISBN='" + ISBN + "' AND "
                    + "COPY > 0)");
            rs.next();
            rs = st.executeQuery("SELECT * FROM BUKU "
                    + "WHERE ("
                    + "ISBN='" + ISBN + "' AND "
                    + "COPY > 0)");
            int index = 0;
            while (rs.next()) {
                bk.setISBN(rs.getString(1));
                bk.setJudul(rs.getString(2));
                bk.setPenulis(rs.getString(3));
                bk.setPenerbit(rs.getString(4));
                bk.setTahun_Terbit(rs.getString(5));
                bk.setCopy(rs.getInt(6));
                index++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bk;
    }

    public static boolean cekBuku(String key) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        conn = DatabaseManager.getDBConnection();
        Anggota a = new Anggota();
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT COUNT (*) TOTAL FROM BUKU "
                    + "WHERE ("
                    + "ISBN LIKE '%" + key + "%' OR  "
                    + "JUDUL LIKE '%" + key + "%' OR "
                    + "PENULIS LIKE '%" + key + "%' OR "
                    + "PENERBIT LIKE '%" + key + "%' OR "
                    + "TAHUN LIKE '%" + key + "%' AND "
                    + "COPY > 0)");
            rs.next();
            int nilai = Integer.parseInt(rs.getString(1));
            if (nilai == 0) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return true;
    }
}
