package edu.uah.uahnavigation.UnitTests;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.uah.uahnavigation.DatabaseManager;
import edu.uah.uahnavigation.DatabaseSource;
import edu.uah.uahnavigation.NetworkManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.*;
import android.util.Log;

/**
 * Created by Daniel on 1/21/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseSource.class, Log.class, android.util.Log.class, DatabaseManager.class, SQLiteOpenHelper.class})
public class DatabaseUnitTest {

    @Mock
    AppCompatActivity aca = new AppCompatActivity();

    @Mock
    DatabaseManager dbManager;

    private DatabaseSource databaseSource;



    @Before
    public void setUp() {
        PowerMockito.mockStatic(Log.class);
        databaseSource = new DatabaseSource(aca);
        databaseSource.open();
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
