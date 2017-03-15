package edu.uah.uahnavigation.UnitTests;

import android.content.Context;

import org.bouncycastle.jce.provider.asymmetric.ec.KeyFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import edu.uah.uahnavigation.CoursesListParser;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Jairo on 3/15/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CoursesListParserUnitTests {

    CoursesListParser courses;
    private static final String FAKE_PATH = "data/data/files";

    public void setUp() throws Exception {

    }

    @Test
    public void testCoursesListParserConstructorNoParameter()
    {

    }

    @Mock
    Context mMockContext;

    @Test
    public void testCoursesListParserConstructorWithParameterNoDirectory()
    {
        boolean thrown = false;
        String dirName = "Webscrape_Resources";
        when(mMockContext.getFilesDir())
                .thenReturn(new File(FAKE_PATH));

        try {
            courses = new CoursesListParser(mMockContext, dirName);
        } catch (RuntimeException e) {
            thrown = true;
        }
        catch (Exception e)
        {
            thrown = false;
        }

        assertTrue(thrown);
    }


}
