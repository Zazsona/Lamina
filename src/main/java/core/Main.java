package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.NetworkManager;
import user.UserProfileManager;

public class Main extends Application
{
    public static void main(String[] args)
    {
        Thread networkThread = new Thread(() -> NetworkManager.initialize());
        networkThread.setName("Network Thread");
        networkThread.setDaemon(true);
        networkThread.start();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/hub.fxml"));
        primaryStage.setTitle("Lamina");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setOnCloseRequest((event) -> UserProfileManager.getCurrentUserProfile().save());
        primaryStage.show();
    }


}
