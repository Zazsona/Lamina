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
        enterButton.setOnAction((value) ->
                                {
                                    alterNode();
                                    enterButton.getScene().getWindow().hide();
                                });
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

    private void alterNode()
    {
        if (node instanceof Text)
        {
            ((Text) node).setText(getInput());
        }
        else if (node instanceof ImageView)
        {
            Image image = new Image(getInput());
            ((ImageView) node).setImage(image);
            CoreUtils.centreImage(((ImageView) node), image);
        }
    }
}
