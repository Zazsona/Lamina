package user;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class UserProfile
{
    /**
     * The user's name
     */
    protected String name;
    /**
     * The user's profile picture
     */
    protected Image image;
    /**
     * The user's HP
     */
    protected double hitPoints;
    /**
     * The user's stamina
     */
    protected double stamina = 100;
    /**
     * The user's status conditions
     */
    protected ArrayList<StatusCondition> statusConditions;


    /**
     * Constructor
     * @param name the user's name
     * @param image the user's profile image
     * @param hitPoints the user's HP
     * @param stamina the user's stamina
     * @param statusConditions the user's conditions
     */
    public UserProfile(String name, Image image, double hitPoints, double stamina, ArrayList<StatusCondition> statusConditions)
    {
        setName(name);
        setImage(image);
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
        this.image = new Image(getClass().getClassLoader().getResourceAsStream("hubGraphics/placeholderProfileImage.png"));
        this.hitPoints = 100;
        this.stamina = 100;
        this.statusConditions = new ArrayList<>();
        for (int i = 0; i<9; i++)
        {
            statusConditions.add(new StatusCondition("None", new Image(getClass().getClassLoader().getResourceAsStream("hubGraphics/emptyStatus.png"))));
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
     * @return image
     */
    public Image getImage()
    {
        return image;
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
        if (name.length() <= 20)
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
     * @param image the image to set, of a maximum size 2000x2000
     * @throws IllegalArgumentException
     */
    public void setImage(Image image)
    {
        System.out.println(image.getHeight());
        if ((image.getHeight() <= 2000) && (image.getWidth() <= 2000))
        {
            this.image = image;
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
        if (hitPoints >=0 && hitPoints<=1)
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
        if (stamina >=0 && stamina<=1)
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
                statusConditions.add(new StatusCondition("None", new Image(getClass().getClassLoader().getResourceAsStream("hubGraphics/emptyStatus.png"))));
            }
        }
        this.statusConditions = statusConditions;
    }
}
