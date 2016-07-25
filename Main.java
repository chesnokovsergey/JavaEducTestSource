package com.sschesnokov;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

public class Main {

    public static void main(String[] args) throws Exception, SQLException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String db_connect_string = "jdbc:sqlserver://nsk-rabistest01;databaseName=UIDB";
        String db_userid = "Rabis";
        String db_password = "qdpt#rfg78%A$";
        String db_address = "";
        String db_name = "";

        /*
        System.out.println("Подключение к БД.");
        System.out.println("Введите логин:");
        db_userid = reader.readLine();
        System.out.println("Введите пароль:");
        db_password = reader.readLine();
        System.out.println("Введите адрес БД:");
        db_address = reader.readLine();
        System.out.println("Введите имя БД:");
        db_name = reader.readLine();
        db_connect_string = "jdbc:sqlserver://" + db_address + ";databaseName=" + db_name;
        */
        //Подключение

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs_col = null;
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

            int N=15;
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
            try{

                FileWriter writer = new FileWriter(xmlFile);

                String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<entries>\n";
                writer.write(text);
                rs = stmt.executeQuery("select field from sstest");
                while(rs.next()){
                    writer.append("  <entry>\n" +
                            "    <field>" +
                            rs.getString("field") + "</field>\n" +
                            "  </entry>\n");
                    writer.flush();
                }
                writer.append("</entries>");
                writer.flush();

            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }

            System.out.println("транс1");
            TransformerFactory tFactory = TransformerFactory.newInstance();
            System.out.println("транс2");
            String trans = "trans.xsl";
            String sourceId = "1.xml";
            System.out.println("транс3");
            File xmlFileResult = new File("2.xml");
            System.out.println("транс4");
            FileOutputStream os = new FileOutputStream(xmlFileResult);
            System.out.println("транс5");
            Transformer transformer = tFactory.newTransformer(new StreamSource(trans));
            System.out.println("транс6");
            transformer.transform(new StreamSource(sourceId), new StreamResult(os));
            System.out.println("транс7");


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

