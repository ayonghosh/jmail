package jmail.digiSign;



import java.math.BigInteger;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.beans.*;
import jmail.secure.*;
import java.util.Vector;
import javax.mail.*;

public final class DigiSign implements java.io.Serializable
{
    public BigInteger hash;
//     public BigInteger [] hash;
    public Key publicKey;
    public String hashMethod = "SHA-256";
    public String encMethod = "RSA";
//     public Certificate cert;
    private Vector<Part> attch;
    
    public DigiSign(String hashAlgo, Vector<Part> v)
    {
        this.attch = v;
    }
    
    public void exportXML(File f) throws Exception
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("DigitalSignature");
        document.appendChild(rootElement);
        
        Element hash = document.createElement("EncryptedDigest");
        hash.appendChild(document.createTextNode(this.hash.toString(16)));
//         for (int i = 0; i < this.hash.length; i++) {
//             Element em = document.createElement("byte" + (i + 1));
//             em.appendChild(document.createTextNode(this.hash[i].toString(16)));
//             hash.appendChild(em);
//         }
        rootElement.appendChild(hash);
        
        Element pk = document.createElement("PublicKey");
        Element exp = document.createElement("exponent");
        exp.appendChild(document.createTextNode(publicKey.getExponent().toString(16)));
        pk.appendChild(exp);
        Element mod = document.createElement("modulus");
        mod.appendChild(document.createTextNode(publicKey.getModulus().toString(16)));
        pk.appendChild(mod);
        rootElement.appendChild(pk);
        
        Element hm = document.createElement("HashAlgorithm");
        hm.appendChild(document.createTextNode("SHA-256"));
        rootElement.appendChild(hm);
        
        Element enc = document.createElement("EncryptionAlgorithm");
        enc.appendChild(document.createTextNode("RSA"));
        rootElement.appendChild(enc);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result =  new StreamResult(new FileOutputStream(f));
        transformer.transform(source, result);
    }
    
    public void importXML(File f) throws Exception
    {
        
    }
}