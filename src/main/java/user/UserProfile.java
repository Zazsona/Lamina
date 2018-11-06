package user;

import javafx.scene.image.Image;

import java.util.ArrayList;

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
    private ArrayList<StatusCondition> statusConditions;


    /**
     * Constructor
     * @param name the user's name
     * @param imagePath the user's profile image path
     * @param hitPoints the user's HP
     * @param stamina the user's stamina
     * @param statusConditions the user's conditions
     */
    public UserProfile(String name, String imagePath, double hitPoints, double stamina, ArrayList<StatusCondition> statusConditions)
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
        this.statusConditions = new ArrayList<>();
        for (int i = 0; i<9; i++)
        {
            statusConditions.add(new StatusCondition("None", getClass().getClassLoader().getResource("hubGraphics/emptyStatus.png").getPath()));
        }
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
     * @return hitPoints
     */
    public double getHitPoints()
    {
        return hitPoints;
    }

    /**
     * Gets stamina
     * @return stamina
     */
    public double getStamina()
    {
        return stamina;
    }

    /**
     * Gets statusConditions
     * @return arraylist holding all status conditions
     */
    public ArrayList<StatusCondition> getStatusConditions()
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
     * @param statusConditions the value to set
     */
    public void setStatusConditions(ArrayList<StatusCondition> statusConditions)
    {
        for (int i = 0; i<9; i++)
        {
            if (statusConditions.size() <= i)
            {
                statusConditions.add(new StatusCondition("None", getClass().getClassLoader().getResource("hubGraphics/emptyStatus.png").getPath()));
            }
        }
        this.statusConditions = statusConditions;
    }
}
