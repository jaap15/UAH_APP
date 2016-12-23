package edu.uah.uahnavigation.UnitTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;

import edu.uah.uahnavigation.NetworkManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jairo on 12/22/2016.
 */
//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkManager.class})
public class NetworkManagerUnitTest {

    //https://raseshmori.wordpress.com/2015/01/07/mockito-and-power-mockito-cheatsheet/

    @Test
    public void testHasInternetConnectionThrowsException() throws Exception
    {
        PowerMockito.mockStatic(InetAddress.class);
        PowerMockito.doThrow(new Exception()).when(InetAddress.class);
        assertEquals(false, NetworkManager.hasInternetConnection());
    }

    @Test
    public void testHasInternetConnectionTrue() throws Exception
    {
        InetAddress temp = InetAddress.getLocalHost();
        PowerMockito.mockStatic(InetAddress.class);
        PowerMockito.when(InetAddress.getByName("google.com")).thenReturn(temp);
        assertEquals(true, NetworkManager.hasInternetConnection());
    }


}