package edu.uah.uahnavigation;

/**
 * Created by Jairo on 12/22/2016.
 */

public class webscraper {

    private String url;

    public webscraper()
    {
        url = "http://www.uah.edu/cgi-bin/schedule.pl?";
    }

    public webscraper(String urlToUse)
    {
        url = urlToUse;
    }

    public String getUrl()
    {
        return url;
    }

}
