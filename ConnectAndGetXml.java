package com.sschesnokov;

import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Администратор on 24.07.2016.
 */
public class ConnectAndGetXml {
    String db_connect_string = "jdbc:sqlserver://nsk-rabistest01;databaseName=UIDB";
    String db_userid = "Rabis";
    String db_password = "qdpt#rfg78%A$";
    String db_address = "";
    String db_name = "";

    public void setDb_address(String val){
        db_address=val;
    }
    public void setDb_name(String val){
        db_name=val;
    }
    public void setDb_userid(String val){
        db_userid=val;
    }
    public void setDb_password(String val){
        db_password=val;
    }
    db_connect_string="jdbc:sqlserver://"+db_address+";databaseName="+db_name;

    //Подключение

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    ResultSet rs_col = null;


        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        conn = DriverManager.getConnection(db_connect_string,
                db_userid, db_password);
        System.out.println("connected");

        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        int N = 15;
        stmt.execute("delete from sstest");
        for (int i = 1; i <= N; i++) {
            stmt.execute("insert into sstest (field) values('" + i + "')");
        }

        rs = stmt.executeQuery("select field from sstest");
        while (rs.next()) {
            System.out.println("field = " + rs.getString("field"));
        }

        rs_col = stmt.executeQuery("select count(*) as col from sstest");

        while (rs_col.next()) {
            if (rs_col.getInt("col") > 0) {
                System.out.println("больше 0");
                //rs.beforeFirst();

                //while(rs.next()){

                //}
            }
        }
        File xmlFile = new File("1.xml");
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

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
}