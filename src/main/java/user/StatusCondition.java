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
    private String imagePath; //This is used instead of Image as Gson can't save self-referential objects.

    /**
     * Constructor
     * @param title the condition title
     * @param imagePath the condition image path
     */
    public StatusCondition(String title, String imagePath)
    {
        this.title = title;
        this.imagePath = imagePath;
    }

    /**
     * Gets title
     * @return the status' title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Gets image
     * @return the status' image
     */
    public Image getImage()
    {
        return new Image(imagePath);
    }

    /**
     * Gets image path
     * @return the path of the status' image
     */
    public String getImagePath()
    {
        return imagePath;
    }

}
