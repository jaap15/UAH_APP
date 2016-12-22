package edu.uah.uahnavigation.UnitTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uah.uahnavigation.webscraper;

import static org.junit.Assert.*;

/**
 * Created by Jairo on 12/22/2016.
 */
public class webscraperTest {

    webscraper scraper;
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testWebscraperConstructorNoParameter()
    {
        String url = "http://www.uah.edu/cgi-bin/schedule.pl?";
        scraper = new webscraper();
        assertEquals(url, scraper.getUrl());

    }

    @Test
    public void testWebscraperConstructorWithParameter()
    {
        String url = "http://www.uah.edu";
        scraper = new webscraper(url);
        assertEquals(url, scraper.getUrl());

    }

    @After
    public void tearDown() throws Exception {
        scraper = null;
    }

}