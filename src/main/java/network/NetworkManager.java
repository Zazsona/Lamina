package network;

import user.UserProfile;
import user.UserProfileManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkManager
{
    private static DatagramSocket socket;

    public static void initialize()
    {
        try
        {
            socket = new DatagramSocket(53719);

            Timer profileSendingTimer = new Timer();
            profileSendingTimer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    sendProfile();
                }
            }, 1000*5, 1000*5); //TODO: Optimise

            runServer();
        }
        catch (IOException e)
        {
            System.err.println("Unable to connect to the network.");
            e.printStackTrace();
        }

    }

    public static void runServer()
    {
        while (true)
        {
            try
            {
                byte[] buffer = new byte[UserProfileManager.getCurrentUserProfile().getJson().length()*2];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                UserProfile profile = new UserProfile(new String(packet.getData()).trim());
                UserProfileManager.addUserProfile(profile);
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

    public static void sendProfile()
    {
        try
        {
            byte[] buffer = UserProfileManager.getCurrentUserProfile().getJson().getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("192.168.1.255"), 53719); //TODO: Not hardcode 192.168.1, get host's network
            socket.send(packet);
        }
        catch (IOException e)
        {
            System.err.println("Unable to send profile.");
            e.printStackTrace();
        }

    }
}
