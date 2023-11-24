package ua.com.foxminded.utill;

public class NameValidator {
    private static final String FILTER_ON_THE_MINIMUM_NUMBER_OF_LETTERS = ".*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*";
    private static final String FILTER_ON_ACCEPTABLE_NAME_FORM = "^[A-Z]{2}-\\d{2}$";
    private static final String FILTER_ON_ACCEPTABLE_AMOUNT_OF_DESCRIPTION_SYMBOLS = "^.{100,2000}$";


    public static boolean isValidName(String name) {
        return name.matches(FILTER_ON_THE_MINIMUM_NUMBER_OF_LETTERS);
    }

    public static boolean isValidNameForUser(String name) {
        return !name.isEmpty() && name.matches("[a-zA-Z]+");
    }

    public static boolean isValidNameForGroup(String name) {
        return name.matches(FILTER_ON_ACCEPTABLE_NAME_FORM);
    }

    public static boolean isValidDescriptionForLecture(String description) {
        return description.matches(FILTER_ON_ACCEPTABLE_AMOUNT_OF_DESCRIPTION_SYMBOLS);
    }
}
