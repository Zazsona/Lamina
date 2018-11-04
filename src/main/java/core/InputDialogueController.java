package core;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class InputDialogueController
{
    private String prompt = "Please input your value.";
    private Node node;

    @FXML
    private TextField inputTextField;
    @FXML
    private Text promptText;
    @FXML
    private Button enterButton;

    public void initialize()
    {
        promptText.setText(prompt);
        enterButton.setOnAction((value) -> updateUserProfile());
    }
    public String getInput()
    {
        return inputTextField.getText();
    }

    public void setPrompt(String prompt)
    {
        this.prompt = prompt;
        promptText.setText(prompt);
    }

    public void setNodeToAlter(Node node)
    {
        this.node = node;
    }

    private void updateUserProfile()
    {
        try
        {
            if (node instanceof Text)
            {
                Main.getCurrentUserProfile().setName(((Text) node).getText());
                ((Text) node).setText(getInput());
            }
            else if (node instanceof ImageView)
            {
                Image image = new Image(getInput());
                Main.getCurrentUserProfile().setImage(((ImageView) node).getImage());
                ((ImageView) node).setImage(image);
                CoreUtils.centreImage(((ImageView) node), image);
            }
            enterButton.getScene().getWindow().hide();
        }
        catch (IllegalArgumentException e)
        {
            if (node instanceof Text)
            {
                promptText.setText(promptText.getText()+"\n\nThat input is too long. Please try again.");
            }
            else if (node instanceof ImageView)
            {
                promptText.setText(promptText.getText()+"\n\nThat image is too large. Please try again.");
            }
        }
        catch (NullPointerException e)
        {
            System.err.println("No node has been set.");
            e.printStackTrace();
        }

    }
}
