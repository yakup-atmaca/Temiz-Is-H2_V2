/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Yakup
 */
public class MacAddress {

    public MacAddress() {
    }

    public static String getMacAddress() throws IOException {
        try {
             /* bu kod calismadi 20240209
            Process p = Runtime.getRuntime().exec("getmac /fo csv /nh");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String line;
            line = in.readLine();
            String[] result = line.split(",");
            return result[0].replace('"', ' ').trim();
*/
            //bu kod sonradan yazildi 20240209
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = ni.getHardwareAddress();

            String[] hexadecimal = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }
            String macAddress = String.join("-", hexadecimal);
            return macAddress;

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }
}
