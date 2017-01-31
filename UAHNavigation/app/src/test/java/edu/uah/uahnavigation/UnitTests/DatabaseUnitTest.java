package edu.uah.uahnavigation.UnitTests;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.uah.uahnavigation.DatabaseManager;
import edu.uah.uahnavigation.DatabaseSource;

import static junit.framework.Assert.assertNotNull;

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

    public DatabaseUnitTest() {

    }

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Log.class);
        databaseSource = new DatabaseSource(aca);
        databaseSource.open();
    }

    @After
    public void finish() {
        databaseSource.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(databaseSource);
    }
}
