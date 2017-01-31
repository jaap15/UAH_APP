package edu.uah.uahnavigation.UnitTests;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockTestCase;

import edu.uah.uahnavigation.DatabaseManager;
import edu.uah.uahnavigation.DatabaseSource;

import static org.junit.Assert.assertEquals;

/**
 * Created by Daniel on 1/21/2017.
 */
@PrepareForTest({DatabaseSource.class, Log.class, SQLiteOpenHelper.class, DatabaseManager.class})
@RunWith(PowerMockRunner.class)
public class DatabaseUnitTest  {

    @Mock
    Context context;

    @Test
    public void testCreateDatabaseNotOpened() throws Exception {

        DatabaseSource dbSource = new DatabaseSource(context);
        assertEquals(false, dbSource.isOpen());
    }
}


