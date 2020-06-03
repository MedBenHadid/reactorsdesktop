package SharedResources.Utils.BinaryValidator;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class PhoneNumberValidator extends ValidatorBase {
    public PhoneNumberValidator(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        String text = textField.getText();
        if (text.length() != 8) {
            hasErrors.set(true);
            message.set("Taille doit étre 8");
        } else if (!text.matches("^[\\d]{8}$")) {
            message.set("Numérique seulement");
            hasErrors.set(true);
        } else
            hasErrors.set(false);
    }
}
