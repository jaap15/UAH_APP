package edu.uah.uahnavigation;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jairo on 12/22/2016.
 */

public class Webscraper {

    private Context context;
    private String domainName = null;
    private String courseListingURL = null;
    private String semesterUrlToScrape = null;
    private Semester semesterToScrape;
    private ArrayList<Semester> currentSemesters;
    private String webscrapeResourcesFolderPath;
    private String message = "myMessage";

    public Webscraper()
    {
//        this.homePageUrl = "http://www.uah.edu/cgi-bin/schedule.pl";
    }

    public Webscraper(Context context, String domainName, String courseListingURL, String webscrapeResourcesFolder)
    {
        this.context = context;
        this.domainName = domainName;
        this.courseListingURL = domainName + courseListingURL;
        this.semesterToScrape = new Semester();
        this.webscrapeResourcesFolderPath = context.getFilesDir() + "/" + webscrapeResourcesFolder;
        File direct = new File(this.webscrapeResourcesFolderPath);

        if(!direct.exists()) {
            if(direct.mkdir()); //directory is created;
        }

        this.currentSemesters = new ArrayList<Semester>();
    }

    public void scrapePossibleSemesters()
    {

        try
        {
            Log.d(message, "Trying to connect to url");
            Document doc = Jsoup.connect(this.courseListingURL).get();
            Log.d(message, "After connected to url");
            Log.d(message, "Content: \n"+ doc.text());

            Elements semesters = doc.select("ul");
            Elements linkSemesters = semesters.select("a[href]");
//            Log.d(message, "links: \n"+ linkSemesters.html());
//            Log.d(message, "links: \n"+ linkSemesters);

            for(Element semester : linkSemesters){

                if(semester.text().toLowerCase().contains("spring") || semester.text().toLowerCase().contains("summer") || semester.text().toLowerCase().contains("fall"))
                {
                    this.currentSemesters.add(new Semester(semester.attr("href"), semester.text()));
//                    Log.d(message, "text: "+ semester.text());
//                    Log.d(message, "link: "+ semester.attr("href"));
                }


            }

        }
        catch (IOException e)
        {
            Log.d(message, "Unable to connect to url");
            e.printStackTrace();
        }

    }

    public ArrayList<Semester> getPossibleSemesters()
    {
        return this.currentSemesters;
    }

    public void setSemesterToScrape(Semester semesterToScrape)
    {
        if(semesterToScrape.getURL().startsWith("http") || semesterToScrape.getURL().startsWith("www."))
        {
            this.semesterToScrape.setURL(semesterToScrape.getURL());
            this.semesterToScrape.setSemesterName(semesterToScrape.getSemesterName());
        }
        else
        {
            Log.d(message, "Invalid URL to Scrape!!\n " + semesterToScrape.getURL());
            throw new RuntimeException("Invalid URL to Scrape!!\n " + semesterToScrape.getURL());
        }

    }

    public Semester getSemesterToScrape()
    {
        return this.semesterToScrape;
    }

    public void scrapeSemester()
    {
        Document doc;
        ArrayList courseList = new ArrayList();
        createSemesterDirectory();

        try {

//            String url1 = domainName + courseListingURL;
            String url1 = this.semesterToScrape.getURL();
            String FOLDER_NAME = this.webscrapeResourcesFolderPath + "/" + this.semesterToScrape.getSemesterName();
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
                    Elements t = docMajor.getElementsContainingOwnText(link.ownText()+"/");
                    Log.d(message, "WebScraping "+ t.text());
                    BufferedWriter writer = new BufferedWriter(new FileWriter(FOLDER_NAME + "/" + link.ownText() + ".txt"));
                    courseList.add(FOLDER_NAME + "/" + link.ownText() + ".txt");
                    writer.write(t.text()+"\n");
                    writer.write(info.text());
                    writer.close();
                }
                i++;
            }

            System.out.println("Finished..WebScraping...");
        } catch (Exception ex) {
            System.out.println("Error Getting URL");
        }
    }

    private void createSemesterDirectory()
    {
        File file = new File(this.webscrapeResourcesFolderPath + "/" + this.semesterToScrape.getSemesterName());
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

    public String getSemesterUrlToScrape()
    {
        return this.semesterUrlToScrape;
    }
//    public String getHomePageUrl() { return this.homePageUrl; }

    static class Semester{
        private String URL = null;
        private String semesterName = null;

        public Semester()
        {

        }

        public Semester(String URL, String semesterName)
        {
            this.URL = URL;
            this.semesterName = semesterName;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getSemesterName() {
            return semesterName;
        }

        public void setSemesterName(String semesterName) {
            this.semesterName = semesterName;
        }
    }
}
