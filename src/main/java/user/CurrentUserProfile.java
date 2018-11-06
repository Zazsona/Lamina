package user;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;

public class CurrentUserProfile extends UserProfile
{
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

    public void restore() throws IOException
    {
        try
        {
            Gson gson = new Gson();
            CurrentUserProfile profileFromFile = gson.fromJson(new String(Files.readAllBytes(getFile().toPath())), getClass());
            setName(profileFromFile.getName());
            setImage(profileFromFile.getImagePath());
            setHitPoints(profileFromFile.getHitPoints());
            setStamina(profileFromFile.getStamina());
            setStatusConditions(profileFromFile.getStatusConditions());
        }
        catch (IOException | NullPointerException e)
        {
            System.err.println("Could not restore user profile.");
            throw new IOException();
        }

    }

    public void save()
    {
        try
        {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(getFile(), false));
            printWriter.print(json);
            printWriter.close();
        }
        catch (IOException | NullPointerException e)
        {
            System.err.println("Could not save to file.");
        }

    }

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
