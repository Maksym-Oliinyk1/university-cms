package ua.com.foxminded.utill;

public class NameValidator {
    private static final String FILTER_ON_ACCEPTABLE_NAME_FORM_FOR_USER = "[a-zA-Z]+";

    private static final String FILTER_ON_THE_MINIMUM_NUMBER_OF_LETTERS = ".*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*";
    private static final String FILTER_ON_ACCEPTABLE_NAME_FORM = "^[A-Z]{2}-\\d{2}$";
    private static final String FILTER_ON_ACCEPTABLE_AMOUNT_OF_DESCRIPTION_SYMBOLS = "^.{50,2000}$";
    private NameValidator() {
        throw new RuntimeException("Utils class");
    }

    public static boolean isValidNameForUniversityEntity(String name) {
        return name.matches(FILTER_ON_THE_MINIMUM_NUMBER_OF_LETTERS);
    }

    public static boolean isValidNameForUser(String name) {
        return !name.isEmpty() && name.matches(FILTER_ON_ACCEPTABLE_NAME_FORM_FOR_USER);
    }

    public static boolean isValidNameForGroup(String name) {
        return name.matches(FILTER_ON_ACCEPTABLE_NAME_FORM);
    }

    public static boolean isValidDescriptionForLecture(String description) {
        return description.matches(FILTER_ON_ACCEPTABLE_AMOUNT_OF_DESCRIPTION_SYMBOLS);
    }
}
