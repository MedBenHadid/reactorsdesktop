

package Packages.Chihab.Controllers.Custom;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.regex.Pattern;


/**
 * Regex validation, that is applied on text input controls
 * such as {@link TextField} and {@link TextArea}.
 *
 * @version 1.0
 * @since 2018-08-06
 */
@DefaultProperty(value = "icon")
public class RegexValidator extends ValidatorBase {

    private String regexPattern;
    private Pattern regexPatternCompiled;

    public RegexValidator(String message, String regexPattern) {
        super(message);
        this.regexPattern = regexPattern;
        this.regexPatternCompiled = Pattern.compile(regexPattern);
    }


    public RegexValidator() {

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
        hasErrors.set(!regexPatternCompiled.matcher(textField.getText()).matches());
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    /*
     * GETTER AND SETTER
     */
    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
        this.regexPatternCompiled = Pattern.compile(regexPattern);
    }
}
