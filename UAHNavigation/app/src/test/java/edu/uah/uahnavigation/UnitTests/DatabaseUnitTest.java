package edu.uah.uahnavigation.UnitTests;

import android.support.v7.app.AppCompatActivity;
import android.test.AndroidTestCase;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import edu.uah.uahnavigation.DatabaseManager;
import edu.uah.uahnavigation.DatabaseSource;
import static org.mockito.Mockito.when;

/**
 * Created by Daniel on 1/21/2017.
 */

public class DatabaseUnitTest{

    @Before
    public void setUp() throws Exception
    {

    }
//    @Mock
//    DatabaseManager databaseMock;
//
//    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
//
//    @Test
//    public void databaseExistsThrowsException() throws Exception {
//        DatabaseSource test = Mockito.mock(DatabaseSource.class);
//        when(test.exists(this.getContext())).thenReturn(true);
//        assertEquals(test.exists(this.getContext()), true);
//
//    }
//
//    @Test
//    public void databaseExistsTrue() throws Exception
//    {
//        DatabaseSource test = Mockito.mock(DatabaseSource.class);
//        when(test.exists(this.getContext())).thenReturn(false);
//        assertEquals(test.exists(this.getContext()), false);
//    }
    @After
    public void tearDown() throws Exception
    {

    }
}
