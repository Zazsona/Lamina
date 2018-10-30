package core;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import user.StatusCondition;
import user.UserProfile;

public class Controller
{
    @FXML
    private VBox profileCollectionBox;

    public void initialize()
    {
    }
    public void addProfile(UserProfile profile)
    {
        HBox profileBox = new HBox();
        profileBox.setStyle("-fx-background-color: #7c0000;");
        profileBox.setEffect(new DropShadow());
        profileBox.setPadding(new Insets(10, 10, 10, 10));

        VBox identityBox = new VBox();
        identityBox.setPrefHeight(250);
        identityBox.setAlignment(Pos.CENTER);
        identityBox.setSpacing(10.0);
        Circle identityImage = new Circle();
        identityImage.setRadius(60);
        identityImage.setFill(new ImagePattern(profile.getImage()));
        identityImage.setEffect(new DropShadow());
        Text identityName = new Text(profile.getName());
        identityName.setFill(Paint.valueOf("#FFFFFF"));
        identityName.setFont(Font.font("System", FontWeight.BOLD, 14));
        identityBox.getChildren().add(identityImage);
        identityBox.getChildren().add(identityName);

        VBox statsBox = new VBox();
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(0, 10, 0, 10));
        statsBox.setSpacing(3);
        ProgressBar statsHpBar = new ProgressBar();
        statsHpBar.setStyle("-fx-accent: #16b120; -fx-control-inner-background: #5b0000;");
        statsHpBar.setMinHeight(30);
        statsHpBar.setMinWidth(400);
        statsHpBar.setProgress(profile.getHitPoints());
        ProgressBar statsStaminaBar = new ProgressBar();
        statsStaminaBar.setStyle("-fx-accent: #333CF3; -fx-control-inner-background: #5b0000;");
        statsStaminaBar.setMinWidth(statsHpBar.getMinWidth()*0.33);
        statsStaminaBar.setProgress(profile.getStamina());
        Pane statsDividerPane = new Pane();
        statsDividerPane.setMaxHeight(20);
        statsDividerPane.setMinHeight(20);
        HBox statsConditionBox = new HBox();
        statsConditionBox.setSpacing(10);
        for (StatusCondition statusCondition : profile.getStatusConditions())
        {
            Circle statsConditionImage = new Circle();
            statsConditionImage.setRadius(30);
            statsConditionImage.setFill(new ImagePattern(statusCondition.getImage()));
            statsConditionBox.getChildren().add(statsConditionImage);
        }
        statsBox.getChildren().add(statsHpBar);
        statsBox.getChildren().add(statsStaminaBar);
        statsBox.getChildren().add(statsDividerPane);
        statsBox.getChildren().add(statsConditionBox);

        identityBox.setEffect(null);
        statsBox.setEffect(null);
        profileBox.getChildren().add(identityBox);
        profileBox.getChildren().add(statsBox);

        profileCollectionBox.getChildren().add(profileBox);
    }
}
