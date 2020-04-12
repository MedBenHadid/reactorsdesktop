package Packages.Chihab.Controllers.Custom;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

/**
 * @author Victor Espino
 * @version 1.0
 * @since 2019-08-10
 */
public class StringLengthValidator extends ValidatorBase {

    int StringLength;
    int comp;

    /**
     * Basic constructor with Default message this way:
     * "Max length is " + StringLength +" character(s) "
     *
     * @param StringLengh Length of the string in the input field to validate.
     */
    public StringLengthValidator(int StringLengh) {
        super("Max length is " + StringLengh + " character(s) ");
        this.StringLength = StringLengh + 1;
    }


    /**
     * The displayed message shown will be concatenated by the message with StringLength
     * this way "message" + StringLength.
     *
     * @param StringLength Length of the string in the input field to validate.
     * @param message      Message to show.
     * @param comp         comparator : 0 = , 1 > , -1 <
     */
    public StringLengthValidator(int StringLength, int comp, String message) {
        this.StringLength = StringLength + 1;
        this.comp = comp;
        setMessage(message + StringLength);
    }

    /**
     * The displayed message will be personalized,
     * but still need to indicate the StringLength to validate.
     *
     * @param StringLength Length of the string in the input field to validate.
     * @param message      Message to show.
     */
    public StringLengthValidator(String message, int StringLength) {
        super(message);
        this.StringLength = StringLength + 1;
    }

    public void changeStringLength(int newLength) {
        this.StringLength = newLength + 1;
    }

    public int getStringLength() {
        return StringLength - 1;
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
        hasErrors.set(false);
        if (comp == -1)
            if (!text.isEmpty()) {
                if (text.length() < StringLength - 1) {
                    hasErrors.set(true);
                }
            }
        if (comp == 0)
            if (!text.isEmpty()) {
                if (text.length() != StringLength - 1) {
                    hasErrors.set(true);
                }
            }
        if (comp == 1)
            if (!text.isEmpty()) {
                if (text.length() > StringLength - 1) {
                    hasErrors.set(true);
                }
            }
    }
}
