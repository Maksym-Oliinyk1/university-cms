package ua.com.foxminded.service.impl.generators;

import org.hibernate.engine.jdbc.ReaderInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.UserDTO;
import ua.com.foxminded.enums.Gender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class DataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private static final String FIRST_NAMES_FOR_USERS_DIRECTORY = "/populate/first_names";
    private static final String LAST_NAMES_FOR_USER_DIRECTORY = "/populate/last_names";
    private static final List<String> FIRST_NAMES = readFilePerOneLine(FIRST_NAMES_FOR_USERS_DIRECTORY);
    private static final List<String> LAST_NAMES = readFilePerOneLine(LAST_NAMES_FOR_USER_DIRECTORY);
    protected final Random random = new Random();

    private static String generateRandomEmail(String firstName, String lastName) {
        String domain = "@example.com";
        String randomString = String.valueOf(System.currentTimeMillis());
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "." + randomString + domain;
    }

    protected static List<String> readFilePerOneLine(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                (ReaderInputStream.class.getResourceAsStream(filePath))))) {

            return br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            logger.error("Error reading file: " + filePath, e);
            throw new RuntimeException(e);
        }
    }

    protected void fillUserFields(UserDTO userDTO) {
        String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        Gender gender = Gender.values()[random.nextInt(Gender.values().length)];
        String email = generateRandomEmail(firstName, lastName);

        userDTO.setId(null);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setGender(gender);
        userDTO.setAge(generateRandomAge());
        userDTO.setEmail(email);
        userDTO.setImage(null);
    }

    private LocalDate generateRandomAge() {
        LocalDate currentDate = LocalDate.now();

        int randomYearsAgo = random.nextInt(53) + 18;
        int randomMountsAgo = random.nextInt(11) + 1;
        int randomDaysAgo = random.nextInt(27) + 1;

        return currentDate
                .minus(Period.ofYears(randomYearsAgo))
                .minus(Period.ofMonths(randomMountsAgo))
                .minus(Period.ofDays(randomDaysAgo));
    }
}
