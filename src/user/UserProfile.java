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
    protected double stamina;
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
     * @param name the value to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the value of image
     * @param image the value to set
     */
    public void setImage(Image image)
    {
        this.image = image;
    }

    /**
     * Sets the value of hitPoints
     * @param hitPoints the value to set
     * @return true/false depending on success
     */
    public boolean setHitPoints(double hitPoints)
    {
        if (hitPoints >=0 && hitPoints<=1)
        {
            this.hitPoints = hitPoints;
            return true;
        }
        return false;
    }

    /**
     * Sets the value of stamina
     * @param stamina the value to se
     * @return true/false depending on success
     */
    public boolean setStamina(double stamina)
    {
        if (stamina >=0 && stamina<=1)
        {
            this.stamina = stamina;
            return true;
        }
        return false;
    }

    /**
     * Sets the value of statusConditions
     * @param statusConditions the value to set
     */
    public void setStatusConditions(ArrayList<StatusCondition> statusConditions)
    {
        this.statusConditions = statusConditions;
    }
}
