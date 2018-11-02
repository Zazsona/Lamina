package core;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import user.StatusCondition;
import user.UserProfile;

import java.io.IOException;
import java.util.ArrayList;

public class HubController
{
    @FXML
    private VBox profileCollectionBox;
    @FXML
    private Slider userHPSlider;
    @FXML
    private Text userHPText;
    @FXML
    private Slider userStaminaSlider;
    @FXML
    private Text userStaminaText;
    @FXML
    private Text userText;
    @FXML
    private ImageView userImage;
    public void initialize()
    {
        userHPSlider.valueProperty().addListener((observable, oldValue, newValue) -> userHPText.setText(newValue.intValue()+"/100"));
        userHPSlider.setOnScroll((value) -> userHPSlider.adjustValue(userHPSlider.getValue()+(value.getDeltaY()/4)));

        userStaminaSlider.valueProperty().addListener((observable, oldValue, newValue) -> userStaminaText.setText(newValue.intValue()+"/100"));
        userStaminaSlider.setOnScroll((value) -> userStaminaSlider.adjustValue(userStaminaSlider.getValue()+(value.getDeltaY()/4)));

        userText.setOnMouseClicked((value) -> openUserInputDialogue(userText, "Please enter a username."));
        userImage.setOnMouseClicked((value) -> openUserInputDialogue(userImage, "Please enter an image URL."));

        Circle clip = new Circle(userImage.getFitWidth()/2, userImage.getFitHeight()/2, (userImage.getFitWidth()-20)/2); //Subtracting 1/10th here to ensure there is space for the dropshadow
        clip.setEffect(new DropShadow());
        userImage.setClip(clip);

        for (int i = 0; i<5; i++)
        {
            addProfile(new UserProfile("foobar", new Image("file:foobar.png"), Math.random(), Math.random(), new ArrayList<>())); //TODO: Remove. Testing purposes only
        }

    }
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
        statsHpBar.setProgress(profile.getHitPoints());
        ProgressBar statsStaminaBar = new ProgressBar();
        statsStaminaBar.setStyle("-fx-accent: #333CF3; -fx-control-inner-background: #121413; -fx-background: #242424;");
        statsStaminaBar.setMinWidth(statsHpBar.getMinWidth()*0.33);
        statsStaminaBar.setProgress(profile.getStamina());
        Pane statsDividerPane = new Pane();
        statsDividerPane.setMaxHeight(5);
        statsDividerPane.setMinHeight(5);
        HBox statsConditionBox = new HBox();
        statsConditionBox.setSpacing(10);
        for (int i = 0; i<9; i++)
        {
            Circle statsConditionImage = new Circle();
            statsConditionImage.setRadius(20);
            statsConditionBox.getChildren().add(statsConditionImage);
            statsConditionImage.setFill(new ImagePattern((!profile.getStatusConditions().isEmpty() && profile.getStatusConditions().size()-1 >= i) ? profile.getStatusConditions().get(i).getImage() : new Image("file:indent.png")));
            //TODO: Change from placeholder URL
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

    private void openUserInputDialogue(Node nodeToAlter, String prompt)
    {
        try
        {
            Stage inputStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InputDialogue.fxml"));
            Parent root = fxmlLoader.load();
            InputDialogueController idController = fxmlLoader.getController();
            idController.setNodeToAlter(nodeToAlter);
            idController.setPrompt(prompt);
            inputStage.setTitle("Lamina");
            inputStage.setScene(new Scene(root, 400, 200));
            inputStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
