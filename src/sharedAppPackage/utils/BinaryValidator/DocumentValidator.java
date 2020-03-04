package sharedAppPackage.utils.BinaryValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author Chihab
 * Purpose Verifies submited file is a document
 */


public class DocumentValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String DOCUMENT_PATTERN = "([^\\s]+(\\.(?i)(pdf|docx))$)";

    public DocumentValidator() {
        pattern = Pattern.compile(DOCUMENT_PATTERN);
    }

    public boolean validate(final String image) {

        matcher = pattern.matcher(image);
        return matcher.matches();

    }
}