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

/**
 *
 * @author Jairo
 */
public class JsoupLearningNetbeans {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String domainName = "http://www.uah.edu";
        String url1 = domainName + "/cgi-bin/schedule.pl?file=sprg2017.html&segment=NDX";
        Document doc;
       
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
                BufferedWriter writer = new BufferedWriter(new FileWriter("scrapedMajorsClasses/" + link.ownText() + ".txt"));
                writer.write(info.text());
                writer.close();
              }
              i++;
            }
            
            System.out.println("Finished..WebScraping...");
        } catch (IOException ex) {
            System.out.println("Error Getting URL");
        }
       
    }
    
}
