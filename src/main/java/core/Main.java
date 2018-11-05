package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.CurrentUserProfile;
import user.UserProfile;

import java.io.File;
import java.io.IOException;

public class Main extends Application
{
    private static CurrentUserProfile currentUserProfile;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try
        {
            currentUserProfile = new CurrentUserProfile();
            currentUserProfile.restore();
        }
        catch (IOException e)
        {
            System.err.println("The user profile has become corrupted or is missing. Resetting stored data.");
            currentUserProfile.deleteFile();
            currentUserProfile.save();
            //If the delete fails, there's nothing we can do. The OS is preventing us from accessing data. As such, we'll boot without reading from stored data.
        }
        finally
        {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/hub.fxml"));
            primaryStage.setTitle("Lamina");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.setOnCloseRequest((event) ->
                                           {
                                               try
                                               {
                                                   currentUserProfile.save();
                                               }
                                               catch (IOException e) //TODO: Refine
                                               {
                                                   e.printStackTrace();
                                               }

                                           });
            primaryStage.show();
        }
    }

    public static CurrentUserProfile getCurrentUserProfile()
    {
        return currentUserProfile;
    }


}
