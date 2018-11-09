package user;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.scene.image.Image;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class UserProfile
{
    /**
     * The user's name
     */
    private String name;
    /**
     * The user's profile picture path
     */
    private String imagePath; //This is used instead of Image as Gson can't save self-referential objects.
    /**
     * The user's HP
     */
    private double hitPoints;
    /**
     * The user's stamina
     */
    private double stamina;
    /**
     * The user's status conditions
     */
    private StatusCondition[] statusConditions;


    /**
     * Constructor
     * @param name the user's name
     * @param imagePath the user's profile image path
     * @param hitPoints the user's HP
     * @param stamina the user's stamina
     * @param statusConditions the user's conditions
     */
    public UserProfile(String name, String imagePath, double hitPoints, double stamina, StatusCondition[] statusConditions)
    {
        setName(name);
        setImage(imagePath);
        setHitPoints(hitPoints);
        setStamina(stamina);
        setStatusConditions(statusConditions);
    }

    /**
     * Constructor
     * Creates a user profile with default values.
     */
    public UserProfile()
    {
        this.name = "Anonymous";
        this.imagePath = "";
        this.hitPoints = 100;
        this.stamina = 100;
        this.statusConditions = new StatusCondition[9];
        Arrays.fill(this.statusConditions, StatusCondition.NONE);
    }

    /**
     * Constructor
     * @param json the json to create from
     * @throws IllegalArgumentException invalid JSON
     */
    public UserProfile(String json) throws IllegalArgumentException
    {
        applyJson(json);
    }

    /**
     * Gets name
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets image
     * @return the user's profile image
     */
    public Image getImage()
    {
        return (imagePath.equals("")) ? new Image(getClass().getClassLoader().getResourceAsStream("hubGraphics/placeholderProfileImage.png")) : new Image(imagePath);
    }

    /**
     * Gets image path
     * @return the path for the profile image
     */
    public String getImagePath()
    {
        return imagePath;
    }

    /**
     * Gets hitPoints
     * @return the usr's HP
     */
    public double getHitPoints()
    {
        return hitPoints;
    }

    /**
     * Gets stamina
     * @return the user's stamina
     */
    public double getStamina()
    {
        return stamina;
    }

    /**
     * Gets statusConditions
     * @return array holding all status conditions
     */
    public StatusCondition[] getStatusConditions()
    {
        return statusConditions;
    }

    /**
     * Sets the value of name
     * @param name the name to set, of up to 20 characters.
     * @throws IllegalArgumentException
     */
    public void setName(String name)
    {
        if ((name.length() <= 20) && (name.length() > 0))
        {
            this.name = name;
        }
        else
        {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Sets the value of image
     * @param imagePath the path of the image to set, of a maximum size 2000x2000
     * @throws IllegalArgumentException
     */
    public void setImage(String imagePath)
    {
        Image image = (imagePath.equals("")) ? new Image(getClass().getClassLoader().getResourceAsStream("hubGraphics/placeholderProfileImage.png")) : new Image(imagePath);
        if (((image.getHeight() <= 2000) && (image.getWidth() <= 2000)) && ((image.getHeight() > 0) && (image.getWidth() > 0)) && !imagePath.toLowerCase().startsWith("file:"))
        {
            this.imagePath = imagePath;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the value of hitPoints
     * @param hitPoints the value to set, as a decimal between 0 & 1
     * @throws IllegalArgumentException
     */
    public void setHitPoints(double hitPoints)
    {
        if (hitPoints >=0 && hitPoints<=100)
        {
            this.hitPoints = hitPoints;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the value of stamina
     * @param stamina the value to set, as a decimal between 0 & 1
     * @throws IllegalArgumentException
     */
    public void setStamina(double stamina)
    {
        if (stamina >=0 && stamina<=100)
        {
            this.stamina = stamina;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the value of statusConditions
     * @param statusConditions the list of status conditions to set.
     */
    public void setStatusConditions(StatusCondition[] statusConditions)
    {
        this.statusConditions = Arrays.copyOf(statusConditions, 9);
        Arrays.fill(this.statusConditions, statusConditions.length, this.statusConditions.length, StatusCondition.NONE);
    }

    /**
     * Adds the specified status condition to the profile, if there is space.
     * @param condition The condition to add
     */
    public void addStatusCondition(StatusCondition condition)
    {
        for (int i = 0; i<statusConditions.length; i++)
        {
            if (statusConditions[i].equals(StatusCondition.NONE))
            {
                statusConditions[i] = condition;
                return;
            }
        }
    }

    /**
     * Removes the specified status condition, if it exists.
     * @param condition the condition to remove
     */
    public void removeStatusCondition(StatusCondition condition)
    {
        for (int i = 0; i<statusConditions.length; i++)
        {
            if (statusConditions[i].equals(condition))
            {
                statusConditions[i] = StatusCondition.NONE;
                if (i == statusConditions.length-1)
                {
                    statusConditions[i] = StatusCondition.NONE;
                }
                else if (!statusConditions[i+1].equals(StatusCondition.NONE))
                {
                    for (int j = i; j<statusConditions.length-1; j++)
                    {
                        statusConditions[j] = statusConditions[j+1];

                    }
                }

                break;
            }
        }
    }

    /**
     * Checks if the user has the specified status condition
     * @param condition the condition to check
     * @return whether the user has the condition or not
     */
    public boolean hasStatusCondition(StatusCondition condition)
    {
        for (int i = 0; i<statusConditions.length; i++)
        {
            if (statusConditions[i].equals(condition))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns this object as JSON
     * @return
     */
    public String getJson()
    {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    /**
     * Applies the state defined by the Json.
     * @param json the json to apply
     */
    protected void applyJson(String json)
    {
        try
        {
            Gson gson = new Gson();
            UserProfile profileFromJson = gson.fromJson(json, getClass());
            setName(profileFromJson.getName());
            setImage(profileFromJson.getImagePath());
            setHitPoints(profileFromJson.getHitPoints());
            setStamina(profileFromJson.getStamina());
            setStatusConditions(profileFromJson.getStatusConditions());
        }
        catch (JsonSyntaxException | JsonIOException e)
        {
            System.err.println("Malformed JSON for profile.");
            throw new IllegalArgumentException();
        }

    }

    @Override
    public boolean equals(Object userProfile)
    {
        if (userProfile instanceof UserProfile)
        {
            if (getJson().equals(((UserProfile) userProfile).getJson()))
            {
                return true;
            }
        }
        return false;
    }
}
