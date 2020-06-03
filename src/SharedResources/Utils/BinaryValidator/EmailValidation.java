package SharedResources.Utils.BinaryValidator;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

public class EmailValidation extends ValidatorBase {
    public EmailValidation(String message) {
        super(message);
    }


    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        String text = textField.getText();
        hasErrors.set(!EmailValidator.isEmail(text));
    }

    @Override
    public Node getStyleableNode() {
        return null;
    }
}