package edu.uah.uahnavigation;

//import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Jairo on 12/22/2016.
 */

public class networking {


    public static boolean hasInternetConnection() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

}
