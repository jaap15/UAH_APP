package edu.uah.uahnavigation;

//import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Jairo on 12/22/2016.
 */

public class NetworkManager {

    /**
     * This static function is used to determine if there is an actual internet connection.
     * @return true if the function was able to convert google.com to an ip address which means that there is internet connection otherwise false
     */
    public static boolean hasInternetConnection() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //Trying to get the ip address of google.com
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

}
