package user;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;

public class CurrentUserProfile extends UserProfile
{
    /**
     * Gets the saved file for this user's last state
     * @return the file storing the user's last state
     * @throws IOException unable to access or replace file
     */
    private File getFile() throws IOException
    {
        try
        {
            File file;
            String operatingSystem = System.getProperty("os.name").toLowerCase();
            if (operatingSystem.startsWith("windows"))
            {
                file = new File(System.getProperty("user.home")+"\\AppData\\Roaming\\ZazData\\LaminaProfile.json");
            }
            else
            {
                file = new File(System.getProperty("user.home")+"/.ZazData/LaminaProfile.json");
            }
            file.getParentFile().mkdirs();
            file.createNewFile(); //If the file does not exist, create it.
            return file;
        }
        catch (IOException e)
        {
            System.err.println("Unable to access stored data in any way.");
            throw new IOException();
        }

    }

    /**
     * Restores the user's state from the previous session
     * @throws IOException unable to restore from file
     */
    public void restore() throws IOException
    {
        try
        {
            applyJson(new String(Files.readAllBytes(getFile().toPath())));
        }
        catch (IOException | NullPointerException | IllegalArgumentException e) //IAE is thrown is the JSON is invalid (File corrupted/modified)
        {
            System.err.println("Could not restore user profile.");
            throw new IOException();
        }

    }

    /**
     * Saves the user's current state.
     */
    public void save()
    {
        try
        {
            String json = getJson();
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(getFile(), false));
            printWriter.print(json);
            printWriter.close();
        }
        catch (IOException | NullPointerException e)
        {
            System.err.println("Could not save to file.");
        }

    }

    /**
     * Deletes the user's stored state, and replaces it with an empty file.
     */
    public void deleteFile()
    {
        try
        {
            getFile().delete();
            getFile().createNewFile();
        }
        catch (IOException e)
        {
            System.err.println("Unable to recreate file.");
        }

    }
}
