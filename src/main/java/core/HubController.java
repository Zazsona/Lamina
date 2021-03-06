package core;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.NetworkManager;
import user.StatusCondition;
import user.UserProfile;
import user.UserProfileManager;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class HubController
{
    /**
     * The VBox used to store other user's profiles
     */
    @FXML
    private VBox profileCollectionBox;
    /**
     * This user's HP control
     */
    @FXML
    private Slider userHPSlider;
    /**
     * Text representing this user's current HP out of 100
     */
    @FXML
    private Text userHPText;
    /**
     * This user's stamina control
     */
    @FXML
    private Slider userStaminaSlider;
    /**
     * Text representing this user's current stamina out of 100
     */
    @FXML
    private Text userStaminaText;
    /**
     * This user's name
     */
    @FXML
    private Text userText;
    /**
     * This user's profile image
     */
    @FXML
    private ImageView userImage;
    /**
     * The overlay effect for hovering over the userImage
     */
    @FXML
    private ImageView userImageHover;
    /**
     * The instructions on how to set a userImage
     */
    @FXML
    private Text userImageInstructions;
    /**
     * The grid that stores the status buttons
     */
    @FXML
    private GridPane statusGrid;
    /**
     * The scroll pane for profiles
     */
    @FXML
    private ScrollPane profileScrollPane;

    /**
     * Enum to represent the various input types that can be requested.
     */
    private enum InputRequest
    {
        USERNAME,
        USERIMAGE
    }

    /**
     * Initializer
     */
    public void initialize()
    {
        userText.setText(UserProfileManager.getCurrentUserProfile().getName());
        userImage.setImage(UserProfileManager.getCurrentUserProfile().getImage());
        userHPSlider.setValue(UserProfileManager.getCurrentUserProfile().getHitPoints());
        userStaminaSlider.setValue(UserProfileManager.getCurrentUserProfile().getStamina());
        userHPText.setText(Math.round(userHPSlider.getValue())+"/100");
        userStaminaText.setText(Math.round(userStaminaSlider.getValue())+"/100");

        if (UserProfileManager.getCurrentUserProfile().getImagePath().equals(""))
        {
            userImageInstructions.setVisible(true); //Show the instructions if the user has the default profile image
        }

        userHPSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                                                 {
                                                     userHPText.setText(newValue.intValue()+"/100");
                                                     UserProfileManager.getCurrentUserProfile().setHitPoints(newValue.intValue());
                                                 });
        userHPSlider.setOnScroll((value) -> userHPSlider.adjustValue(userHPSlider.getValue()+(value.getDeltaY()/4)));

        userStaminaSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                                                      {
                                                          userStaminaText.setText(newValue.intValue()+"/100");
                                                          UserProfileManager.getCurrentUserProfile().setStamina(newValue.intValue());
                                                      });
        userStaminaSlider.setOnScroll((value) -> userStaminaSlider.adjustValue(userStaminaSlider.getValue()+(value.getDeltaY()/4)));

        userText.setOnMouseClicked((value) -> getInput(InputRequest.USERNAME));
        userText.setOnMouseEntered((value) ->
                                   {
                                       userText.setScaleX(1.05);
                                       userText.setScaleY(1.05);
                                   });
        userText.setOnMouseExited((value) ->
                                  {
                                      userText.setScaleY(1);
                                      userText.setScaleX(1);
                                  });
        userImage.setOnMouseClicked((value) -> getInput(InputRequest.USERIMAGE));
        userImage.setOnMouseEntered((value) ->
                                    {
                                        userImage.setScaleX(1.05);
                                        userImage.setScaleY(1.05);
                                        userImageHover.setVisible(true);
                                        userImageInstructions.setVisible(false);
                                    });
        userImage.setOnMouseExited((value) ->
                                   {
                                       userImage.setScaleY(1);
                                       userImage.setScaleX(1);
                                       userImageHover.setVisible(false);
                                   });

        Circle clip = new Circle(userImage.getFitWidth()/2, userImage.getFitHeight()/2, (userImage.getFitWidth()-20)/2); //Subtracting 1/10th here to ensure there is space for the dropshadow
        clip.setEffect(new DropShadow());
        userImage.setClip(clip);

        Timer profileListUpdater = new Timer(true);
        profileListUpdater.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (UserProfileManager.hasChangeOccurred()) //Avoids the expensive task of reconstructing the profile list arbitrarily.
                {
                    Platform.runLater(() ->
                                      {
                                          profileCollectionBox.getChildren().removeAll(profileCollectionBox.getChildren());
                                          Iterator profileListIterator = UserProfileManager.getUserProfiles().entrySet().iterator();
                                          while (profileListIterator.hasNext())
                                          {
                                              HashMap.Entry<String, UserProfile> entry = (HashMap.Entry<String, UserProfile>) profileListIterator.next();
                                              addProfile(entry.getValue());
                                          }
                                      }
                    );
                }
                NetworkManager.sendProfile();
            }
        }, 0, 1000*3);

        DropShadow statusDropShadow = new DropShadow();
        statusDropShadow.setColor(Color.WHITE);
        for (Node node : statusGrid.getChildren())
        {
            StatusCondition statusCondition;
            switch (node.getId().replace("StatusView", "")) //TODO: Surely there's a better way than a switch?
            {
                case "gaming":
                    statusCondition = StatusCondition.GAMING;
                    break;
                case "watching":
                    statusCondition = StatusCondition.WATCHING;
                    break;
                case "eating":
                    statusCondition = StatusCondition.EATING;
                    break;
                case "hobbywork":
                    statusCondition = StatusCondition.HOBBYWORK;
                    break;
                case "sleeping":
                    statusCondition = StatusCondition.SLEEPING;
                    break;
                case "drinking":
                    statusCondition = StatusCondition.DRINKING;
                    break;
                case "venturing":
                    statusCondition = StatusCondition.VENTURING;
                    break;
                case "shopping":
                    statusCondition = StatusCondition.SHOPPING;
                    break;
                case "working":
                    statusCondition = StatusCondition.WORKING;
                    break;
                    default:
                        statusCondition = StatusCondition.NONE;
                        break;

            }
            if (UserProfileManager.getCurrentUserProfile().hasStatusCondition(statusCondition))
            {
                node.setEffect(statusDropShadow);
            }
            node.setOnMouseClicked((value) ->
                                   {
                                       if (node.getEffect() == null)
                                       {
                                           node.setEffect(statusDropShadow);
                                           UserProfileManager.getCurrentUserProfile().addStatusCondition(statusCondition);
                                       }
                                       else
                                       {
                                           node.setEffect(null);
                                           UserProfileManager.getCurrentUserProfile().removeStatusCondition(statusCondition);
                                       }

                                   });
            node.setOnMouseEntered((value) ->
                                   {
                                       node.setScaleX(1.05);
                                       node.setScaleY(1.05);
                                   });
            node.setOnMouseExited((value) ->
                                  {
                                      node.setScaleX(1);
                                      node.setScaleY(1);
                                  });
        }
        profileCollectionBox.setOnScroll(event -> //Set scrolling to be "set" - That is, no matter how many elements are in the list, the moved pixels is the same.
                                         {
                                             double scrollAmount = (event.getDeltaY()/profileScrollPane.getContent().getBoundsInLocal().getHeight()/2);
                                             profileScrollPane.setVvalue(profileScrollPane.getVvalue() + -scrollAmount);
                                         });

    }

    /**
     * Adds the specified profile onto the GUI.
     * @param profile the profile to add
     */
    public void addProfile(UserProfile profile)
    {
        HBox profileBox = new HBox();
        profileBox.setStyle("-fx-background-color: #242424;");
        profileBox.setEffect(new DropShadow());
        profileBox.setPadding(new Insets(10, 10, 10, 10));

        ImageView identityImage = new ImageView(profile.getImage());
        identityImage.setFitHeight(125);
        identityImage.setFitWidth(125);
        Circle identityImageClip = new Circle(identityImage.getFitWidth()/2, identityImage.getFitHeight()/2, (identityImage.getFitWidth()-12)/2);
        identityImageClip.setFill(new ImagePattern(profile.getImage()));
        identityImageClip.setEffect(new DropShadow());
        identityImage.setClip(identityImageClip);
        CoreUtils.centreImage(identityImage, profile.getImage());
        profileBox.getChildren().add(identityImage);

        VBox statsBox = new VBox();
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(0, 10, 0, 10));
        statsBox.setSpacing(3);
        Text statsName = new Text(profile.getName());
        statsName.setFill(Paint.valueOf("#FFFFFF"));
        statsName.setFont(Font.font("System", FontWeight.BOLD, 18));
        ProgressBar statsHpBar = new ProgressBar();
        statsHpBar.setStyle("-fx-accent: #16b120; -fx-control-inner-background: #121413; -fx-background: #242424;");
        statsHpBar.setMinHeight(20);
        statsHpBar.setMinWidth(400);
        statsHpBar.setProgress(profile.getHitPoints()/100);
        ProgressBar statsStaminaBar = new ProgressBar();
        statsStaminaBar.setStyle("-fx-accent: #333CF3; -fx-control-inner-background: #121413; -fx-background: #242424;");
        statsStaminaBar.setMinWidth(statsHpBar.getMinWidth()*0.33);
        statsStaminaBar.setProgress(profile.getStamina()/100);
        Pane statsDividerPane = new Pane();
        statsDividerPane.setMaxHeight(5);
        statsDividerPane.setMinHeight(5);
        HBox statsConditionBox = new HBox();
        statsConditionBox.setSpacing(10);
        for (int i = 0; i<profile.getStatusConditions().length; i++)
        {
            Circle statsConditionImage = new Circle();
            statsConditionImage.setSmooth(true);
            statsConditionImage.setRadius(20);
            statsConditionBox.getChildren().add(statsConditionImage);
            statsConditionImage.setFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("hubGraphics/status/"+profile.getStatusConditions()[i].name().toLowerCase()+".png"))));
        }
        statsBox.getChildren().add(statsName);
        statsBox.getChildren().add(statsHpBar);
        statsBox.getChildren().add(statsStaminaBar);
        statsBox.getChildren().add(statsDividerPane);
        statsBox.getChildren().add(statsConditionBox);
        statsBox.setEffect(null);

        profileBox.getChildren().add(statsBox);
        profileCollectionBox.getChildren().add(profileBox);
    }

    /**
     * Takes an input from the user defined by the inputrequest
     * @param inputRequest the input type to get
     */
    private void getInput(InputRequest inputRequest)
    {
        try
        {
            Stage inputStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(CoreUtils.class.getClassLoader().getResource("fxml/inputDialogue.fxml"));
            Parent root = fxmlLoader.load();
            InputDialogueController idController = fxmlLoader.getController();
            inputStage.setTitle("Lamina");
            inputStage.setScene(new Scene(root, 400, 200));

            switch (inputRequest)
            {
                case USERNAME:
                    idController.setPrompt("Please enter a username.");
                    break;
                case USERIMAGE:
                    idController.setPrompt("Please enter an image URL.");
                    break;
            }
            Task<String> waitOnInput = new Task<String>()
            {
                @Override
                protected String call() throws Exception
                {
                    while (idController.getInput().equals("") && inputStage.getScene().getWindow().isShowing())
                    {
                        Thread.sleep(100);
                    }
                    if (inputStage.getScene().getWindow().isShowing())
                    {
                        String input = idController.getInput();
                        try
                        {
                            if (inputRequest.equals(InputRequest.USERNAME))
                            {
                                UserProfileManager.getCurrentUserProfile().setName(input);
                                Platform.runLater(() -> userText.setText(input));
                            }
                            else if (inputRequest.equals(InputRequest.USERIMAGE))
                            {
                                UserProfileManager.getCurrentUserProfile().setImage(input);
                                Platform.runLater(() ->
                                                  {
                                                      userImage.setImage(UserProfileManager.getCurrentUserProfile().getImage());
                                                      CoreUtils.centreImage(userImage, UserProfileManager.getCurrentUserProfile().getImage());
                                                  });
                            }
                            UserProfileManager.getCurrentUserProfile().save();
                            Platform.runLater(() -> root.getScene().getWindow().hide());
                        }
                        catch (IllegalArgumentException e)
                        {
                            if (inputRequest.equals(InputRequest.USERNAME))
                            {
                                idController.setPrompt("Please enter a username."+"\n\nThat input is invalid. Please try again.");
                            }
                            else if (inputRequest.equals(InputRequest.USERIMAGE))
                            {
                                idController.setPrompt("Please enter an image URL."+"\n\nThat image is invalid. Please try again.");
                            }
                        }
                        catch (NullPointerException e)
                        {
                            System.err.println("No node has been set.");
                            e.printStackTrace();
                        }
                        return input;
                    }
                    return null; //Input was closed
                }
            };
            new Thread(waitOnInput).start();
            inputStage.show();
        }
        catch (IOException e)
        {
            System.err.println("Unable to open input dialogue.");
            e.printStackTrace();
        }
    }
}
