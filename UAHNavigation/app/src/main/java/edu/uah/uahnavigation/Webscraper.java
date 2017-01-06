package edu.uah.uahnavigation;

/**
 * Created by Jairo on 12/22/2016.
 */

public class Webscraper {

    private String homePageUrl = null;
    private String semesterUrlToScrape = null;

    public Webscraper()
    {
        this.homePageUrl = "http://www.uah.edu/cgi-bin/schedule.pl";
    }

    public Webscraper(String homePageUrl)
    {
        this.homePageUrl = homePageUrl;
    }

    public String getHomePageUrl()
    {
        return this.homePageUrl;
    }

    public void setSemesterUrlToScrape(String semesterUrlToScrape)
    {
        if(semesterUrlToScrape.startsWith("http") || semesterUrlToScrape.startsWith("www."))
        {
            this.semesterUrlToScrape = semesterUrlToScrape;
        }
    }

    public String getSemesterUrlToScrape()
    {
        return this.semesterUrlToScrape;
    }

}
