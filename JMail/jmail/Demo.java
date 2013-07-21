package jmail;

import java.io.*;
import java.util.*;

/**
 * Write a description of class Demo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Demo
{
    public static void main(String [] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("contacts.csv")));
        String line = new String();
        while ((line = br.readLine()) != null) {
            String [] arr = line.split(",");
            System.out.println(arr.length);
//             StringTokenizer tok = new StringTokenizer(line, ",");
//             System.out.println(tok.countTokens());
//             String nom = tok.nextToken();
//             String em = tok.nextToken();
// //                 System.out.println("|" + email + "|\n|" + em + "|" + em.equalsIgnoreCase(email));
//             if (em.equalsIgnoreCase(email)) {
//                 System.out.println("match");
//                 tok.nextToken();
//                 tok.nextToken();
//                 String pke = tok.nextToken();
// //                     System.out.println(pke);
//                 String pkm = tok.nextToken();
// //                     System.out.println(pkm);
// //                     System.out.println("match: " + pke + " : " + pkm);
//                 return new Key(pke, pkm);
//             }
        }
    }
}
