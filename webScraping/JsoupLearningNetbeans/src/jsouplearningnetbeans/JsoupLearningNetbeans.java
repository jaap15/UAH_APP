/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsouplearningnetbeans;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
/**
 *
 * @author Jairo
 */
public class JsoupLearningNetbeans {
    public static int START_LINE = 2;
    public static String TEMP_FOLDER_NAME = "tmpFolder";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here 
        
        
        ArrayList courseList = new ArrayList();
        
        String domainName = "http://www.uah.edu";
        String url1 = domainName + "/cgi-bin/schedule.pl?file=sprg2017.html&segment=NDX";
        Document doc;
        
        createTmpDirectory();
       
        try {
            
            doc = Jsoup.connect(url1).get();

            Elements majors = doc.select("pre");
            Elements linkMajors = majors.select("a[href]");
            
            String descriptions[] = majors.first().ownText().split("\\r?\\n");
            int descriptionsLength = descriptions.length;
            int i = 0;

            System.out.println("WebScraping.....Please wait");
            for(Element link : linkMajors){
                
              if(i < descriptionsLength)
              {
                String majorUrl = domainName+ link.attr("href");
                Document docMajor = Jsoup.connect(majorUrl).get();
                Elements info = docMajor.select("pre");
                BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FOLDER_NAME + "/" + link.ownText() + ".txt"));
                courseList.add(TEMP_FOLDER_NAME + "/" + link.ownText() + ".txt");
                writer.write(info.text());
                writer.close();
              }
              i++;
            }
            
            System.out.println("Finished..WebScraping...");
        } catch (IOException ex) {
            System.out.println("Error Getting URL");
        }
 
        parseCourseFile(TEMP_FOLDER_NAME + "/" + "ACC.txt");
        
        
       
    }
    
    static void parseCourseFile(String fileName)
    {
        BufferedReader reader = null;
        
        try {
            System.out.println("Opening file"); 
            reader = new BufferedReader(new FileReader(fileName));
            
        } catch (FileNotFoundException ex) {

            System.out.println("Unable to open file"); 
            Logger.getLogger(JsoupLearningNetbeans.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            System.out.println("Skipping lines"); 
            for(int i = 0; i < START_LINE; i++)
            {
                reader.readLine();
            }
            String[] tokens = reader.readLine().split(" ");
            
            System.out.println("Printing Token sizes Token length: " + tokens.length); 
            for(int i = 0; i < tokens.length; i++)
            {
                System.out.println(tokens[i]);
                System.out.println(tokens[i].length());
            }
            
            System.out.println("Reading file"); 
            String line = reader.readLine();
            System.out.println(line);
        } catch (IOException ex) {
            System.out.println("Unable to read file"); 
            Logger.getLogger(JsoupLearningNetbeans.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    static void createTmpDirectory()
    {
        File file = new File(TEMP_FOLDER_NAME);
        boolean b = false;
        
        if(!file.exists())
        {
            System.out.println("making directory");
            b = file.mkdir();
            
            if(b)
            {
                System.out.println("Directory successfully created");
            }
            else
            {
                System.out.println("Unable to create directory");
            }
        }
        else
        {
            System.out.println("directory already exists");
        }
        
        
    }
    
}
