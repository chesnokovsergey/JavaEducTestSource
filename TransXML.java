package com.sschesnokov;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
/**
 * Created by SSCHesnokov on 24.07.2016.
 */
public class TransXML {
    private String sourcexml;
    private String destinxml;
    private String namexsl;

    public void setSourcexml(String val){
        sourcexml=val;
    }
    public void setDestinxml(String val){
        destinxml=val;
    }
    public void setNamexsl(String val){
        namexsl=val;
    }
    public void trans() throws FileNotFoundException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        String trans = "trans.xsl";
        String sourceId = "1.xml";
        File xmlFileResult = new File("2.xml");
        FileOutputStream os = new FileOutputStream(xmlFileResult);
        Transformer transformer = tFactory.newTransformer(new StreamSource(trans));
        transformer.transform(new StreamSource(sourceId), new StreamResult(os));
    }
}
