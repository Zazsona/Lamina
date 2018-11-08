package network;

import user.UserProfile;
import user.UserProfileManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkManager
{
    private static DatagramSocket socket;

    /**
     * Starts the network component
     */
    public static void initialize()
    {
        try
        {
            socket = new DatagramSocket(53719);
            runServer();
        }
        catch (IOException e)
        {
            System.err.println("Unable to connect to the network.");
            e.printStackTrace();
        }

    }

    /**
     * Receives profiles and adds them to the profile list
     */
    public static void runServer()
    {
        while (true)
        {
            try
            {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                if (!packet.getAddress().equals(InetAddress.getLocalHost())) //Ignore the current user's profile.
                {
                    UserProfile profile = new UserProfile(new String(packet.getData()).trim());
                    UserProfileManager.addUserProfile(profile);
                }
            }
            catch (IllegalArgumentException e)
            {
                System.err.println("Received invalid profile. Ignoring...");
            }
            catch (IOException e)
            {
                System.err.println("A connection error occurred.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends out this user's profile
     */
    public static void sendProfile()
    {
        try
        {
            String[] ip = InetAddress.getLocalHost().getHostAddress().split("[.]");
            InetAddress broadcastAddress = InetAddress.getByName(ip[0]+"."+ip[1]+"."+ip[2]+"."+"255");
            byte[] buffer = UserProfileManager.getCurrentUserProfile().getJson().getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(broadcastAddress.getHostAddress()), 53719);
            socket.send(packet);
        }
        catch (IOException e)
        {
            System.err.println("Unable to send profile.");
            e.printStackTrace();
        }

    }
}
