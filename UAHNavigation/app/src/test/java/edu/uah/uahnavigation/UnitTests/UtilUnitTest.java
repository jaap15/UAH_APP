package edu.uah.uahnavigation.UnitTests;

import android.content.Context;
import android.content.res.AssetManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.InputStream;

import edu.uah.uahnavigation.Util;

import static org.mockito.Mockito.doReturn;

/**
 * Created by Jairo on 2/28/2017.
 */


@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(Util.class)
public class UtilUnitTest {

    @Mock
    Context context;
    @Mock
    AssetManager assetManager;
    @Mock
    InputStream inputStream;

    @Test
    public void testgetPropertyAssertManagerThrowsException() throws Exception
    {
        MockitoAnnotations.initMocks(this);//create all @Mock objetcs
        doReturn(assetManager).when(context).getAssets();

//        Util.getProperty("URL", context);

//        URL resource = UtilUnitTest.class.getClassLoader().getResource("\\src\\test\\java\\edu\\uah\\uahnavigation\\UnitTests\\test.txt");

//        assertEquals(null, Util.getProperty("URL", context));

    }
}
