package com.sschesnokov;

import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by SSCHesnokov on 24.07.2016.
 */
public class WriteDbMakeXML {

    private String db_userid = "";
    private String db_password = "";
    private String db_address = "";
    private String db_name = "";
    private String nameXml = "";
    private int N;
    private boolean flag=false;

    public void setDb_address(String val){
        db_address = val;
    }
    public void setDb_name(String val){
        db_name = val;
    }
    public void setDb_userid(String val){
        db_userid = val;

    }
    public void setDb_password(String val){
        db_password = val;
    }
    public void setNameXml(String val){
        nameXml = val;
    }
    public void setN(int x){
        N = x;
    }
    //Подключение
    public void makeXml() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs_col = null;
        String db_connect_string = "jdbc:sqlserver://" + db_address + ";databaseName=" + db_name;
        System.out.println(db_connect_string);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(db_connect_string,
                    db_userid, db_password);
            System.out.println("connected");

            try {
                stmt = conn.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.execute("delete from sstest");
            for (int i = 1; i <= N; i++) {
                stmt.execute("insert into sstest (field) values('" + i + "')");
            }
            rs_col = stmt.executeQuery("select count(*) as col from sstest");
            while (rs_col.next()) {
                if (rs_col.getInt("col") > 0) {
                    flag = true;
                }
            }
            if (flag) {
                File xmlFile = new File(nameXml);
                try {
                    FileWriter writer = new FileWriter(xmlFile);
                    String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<entries>\n";
                    writer.write(text);
                    rs = stmt.executeQuery("select field from sstest");
                    while (rs.next()) {
                        writer.append("  <entry>\n" +
                                "    <field>" +
                                rs.getString("field") + "</field>\n" +
                                "  </entry>\n");
                        writer.flush();
                    }
                    writer.append("</entries>");
                    writer.flush();
                    System.out.println("XML created " + nameXml);

                } catch (IOException ex) {

                    System.out.println(ex.getMessage());
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
