package edu.uah.uahnavigation;

/**
 * Created by Jairo on 12/22/2016.
 */

public class Webscraper {

    private String url;

    public Webscraper()
    {
        url = "http://www.uah.edu/cgi-bin/schedule.pl?";
    }

    public Webscraper(String urlToUse)
    {
        url = urlToUse;
    }

    public String getUrl()
    {
        return url;
    }

}
