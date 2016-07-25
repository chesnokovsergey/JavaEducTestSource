package com.sschesnokov;

import org.w3c.dom.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.parsers.*;
import java.io.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws Exception, SQLException, IOException {
        final Timer time = new Timer();

        time.schedule(new TimerTask() {
            int i = 0;
            @Override
            public void run() { //ПЕРЕЗАГРУЖАЕМ МЕТОД RUN
                if(i>0){
                    System.out.println("Programm is closed");
                    time.cancel();
                    System.exit(0);
                    return;
                }
                System.out.println("5 minutes ago.");
                i = i + 1;
            }
        }, 300000, 10);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input BD address:");
        String db_address = reader.readLine();
        System.out.println("Input BD name:");
        String db_name = reader.readLine();
        System.out.println("Input BD user:");
        String db_userid = reader.readLine();
        System.out.println("Input BD password:");
        String db_password = reader.readLine();
        System.out.println("Input count N:");
        int N = Integer.parseInt(reader.readLine());

        WriteDbMakeXML make1xml = new WriteDbMakeXML();
        make1xml.setDb_name(db_name);//UIDB
        make1xml.setDb_address(db_address);//nsk-rabistest01
        make1xml.setDb_password(db_password);//qdpt#rfg78%A$
        make1xml.setDb_userid(db_userid);//Rabis
        make1xml.setN(N);
        make1xml.setNameXml("1.xml");
        //Записываю в БД 1..N значений  и формирую XML:
        make1xml.makeXml();

        TransXML tr = new TransXML();
        tr.setSourcexml("1.xml");
        tr.setDestinxml("2.xml");
        tr.setNamexsl("trans.xsl");
        //Преобразование:
        tr.trans();

        //Подсчёт арифметической суммы всех чисел:
        String sField;
        int field, sum_field = 0;
        File fXml=new File("2.xml");
        try
        {
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=dbf.newDocumentBuilder();
            Document doc=db.parse(fXml);
            NodeList nodeLst=doc.getElementsByTagName("entry");
            
            for(int je=0;je<nodeLst.getLength();je++)
            {
                Node fstNode=nodeLst.item(je);
                NamedNodeMap attr = fstNode.getAttributes();
                Node nodeAttr = attr.getNamedItem("field");
                sField=nodeAttr.getNodeValue();
                field=Integer.parseInt(sField);
                sum_field=sum_field+field;
            }
        }
        catch(Exception ei){
            System.out.println("Error reading xml");
        }
        System.out.println("Arithmetic sum of the numbers in the 2.xml = "+sum_field+"]");
        System.exit(0);
    }
}

