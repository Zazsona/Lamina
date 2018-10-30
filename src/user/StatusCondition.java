package user;

import javafx.scene.image.Image;

public class StatusCondition
{
    /**
     * The condition's title
     */
    private String title;
    /**
     * The condition's image
     */
    private Image image;

    /**
     * Constructor
     * @param title the condition title
     * @param image the condition image
     */
    public StatusCondition(String title, Image image)
    {
        this.title = title;
        this.image = image;
    }

    /**
     * Gets title
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Gets image
     * @return image
     */
    public Image getImage()
    {
        return image;
    }


}
