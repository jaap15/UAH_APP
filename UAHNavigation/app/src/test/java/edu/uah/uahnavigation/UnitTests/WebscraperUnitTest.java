package edu.uah.uahnavigation.UnitTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uah.uahnavigation.Webscraper;

import static org.junit.Assert.*;

/**
 * Created by Jairo on 12/22/2016.
 */
public class WebscraperUnitTest {

    Webscraper scraper;
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testWebscraperConstructorNoParameter()
    {
        String url = "http://www.uah.edu/cgi-bin/schedule.pl";
        scraper = new Webscraper();
//        assertEquals(url, scraper.getHomePageUrl());

    }

//    @Test
//    public void testWebscraperConstructorWithParameter()
//    {
//        String url = "http://www.Arreola.com";
//        scraper = new Webscraper(url);
//        assertEquals(url, scraper.getHomePageUrl());
//
//    }
//
//    @Test
//    public void testWebscraperSetAndGetSemesterUrlToScrape()
//    {
//        String url = "";
//        scraper = new Webscraper();
//        scraper.setSemesterUrlToScrape(url);
//        assertEquals(null, scraper.getSemesterUrlToScrape());
//
//        url = "123456";
//        scraper.setSemesterUrlToScrape(url);
//        assertEquals(null, scraper.getSemesterUrlToScrape());
//
//        url = "Jairo.com";
//        scraper.setSemesterUrlToScrape(url);
//        assertEquals(null, scraper.getSemesterUrlToScrape());
//
//        url = "http://www.Jairo.com";
//        scraper.setSemesterUrlToScrape(url);
//        assertEquals(url, scraper.getSemesterUrlToScrape());
//
//        url = "www.Jairo.com";
//        scraper.setSemesterUrlToScrape(url);
//        assertEquals(url, scraper.getSemesterUrlToScrape());
//
//    }

    @After
    public void tearDown() throws Exception {
        scraper = null;
    }

}