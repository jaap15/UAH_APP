package edu.uah.uahnavigation;

/**
 * Created by Jairo on 12/22/2016.
 */

public class Vebscraper {

    private String url;

    public Vebscraper()
    {
        url = "http://www.uah.edu/cgi-bin/schedule.pl?";
    }

    public Vebscraper(String urlToUse)
    {
        url = urlToUse;
    }

    public String getUrl()
    {
        return url;
    }

}
