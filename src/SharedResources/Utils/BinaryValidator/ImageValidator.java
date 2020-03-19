package utils.Utils.BinaryValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    private static final String DOCUMENT_PATTERN = "([^\\s]+(\\.(?i)(pdf|docx))$)";

    public ImageValidator() {
        pattern = Pattern.compile(IMAGE_PATTERN);
    }

    public boolean validate(final String image) {

        matcher = pattern.matcher(image);
        return matcher.matches();

    }
}