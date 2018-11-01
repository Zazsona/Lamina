package core;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CoreUtils
{
    /**
     * Applies the specified image to the ImageView, within a circle.
     * @param imageView the ImageView to set
     * @param image the image to apply
     */
    public static void centreImage(ImageView imageView, Image image)
    {
        /*
            What occurs below is we check to find the smallest dimension, and set that as the maximum pixel size in a crop, then proceed to centre the image
        */
        Rectangle2D crop;
        if (image.getHeight() > image.getWidth())
        {
            crop = new Rectangle2D(0, image.getHeight()/2-(image.getWidth()/2), image.getWidth(), image.getWidth());
        }
        else if (image.getWidth() > image.getHeight())
        {
            crop = new Rectangle2D((image.getWidth()/2)-(image.getHeight()/2), 0, image.getHeight(), image.getHeight());
        }
        else
        {
            crop = new Rectangle2D(0, 0, image.getHeight(), image.getHeight());
        }
        imageView.setViewport(crop);
    }
}
