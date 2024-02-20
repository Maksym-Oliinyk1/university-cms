package ua.com.foxminded.service.impl;

/*
@Service
TODO Outdated. Must be updated after writing all templates.
public class DataGeneratorServiceImpl implements DataGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(DataGeneratorServiceImpl.class);
    private static final int AMOUNT_OF_ADMINISTRATORS = 21;
    private static final int AMOUNT_OF_TEACHERS = 30;
    private static final int AMOUNT_OF_STUDENTS = 200;
    private final AdministratorRepository administratorRepository;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;
    private final GroupRepository groupRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final Random random = new Random();
    private final List<String> firstNames = readFilePerOneLine("/populate/first_names");
    private final List<String> lastNames = readFilePerOneLine("/populate/last_names");
    @Autowired
    public DataGeneratorServiceImpl(AdministratorRepository administratorRepository, CourseRepository courseRepository, FacultyRepository facultyRepository, GroupRepository groupRepository, LectureRepository lectureRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.administratorRepository = administratorRepository;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
        this.groupRepository = groupRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    @PostConstruct
    public void generateDataIfEmpty() {
        if (administratorRepository.count() == 0) {
            logger.info("Generating administrators...");
            generateAdministrators();
        }

        if (teacherRepository.count() == 0) {
            logger.info("Generating teachers...");
            generateTeachers();
        }

        if (facultyRepository.count() == 0) {
            logger.info("Generating faculties...");
            generateFaculties();
        }

        if (groupRepository.count() == 0) {
            logger.info("Generating groups...");
            generateGroups();
        }

        if (courseRepository.count() == 0) {
            logger.info("Generate courses...");
            generateCourses();
        }

        if (studentRepository.count() == 0) {
            logger.info("Generate students...");
            generateStudents();
        }

        if (lectureRepository.count() == 0) {
            logger.info("Generate lectures...");
            generateLectures();
        }
    }

    private void generateAdministrators() {
        for (int i = 0; i < AMOUNT_OF_ADMINISTRATORS; i++) {
            int randomFirstNameIndex = random.nextInt(firstNames.size());
            int randomLastNameIndex = random.nextInt(lastNames.size());
            String randomFirstName = firstNames.get(randomFirstNameIndex);
            String randomLastName = lastNames.get(randomLastNameIndex);
            administratorRepository.save(new Administrator(0L, randomFirstName, randomLastName, 32, "john.dao@gmail.com", "static/images/avatar.png"));
            logger.info("Created administrator: {} {}.", randomFirstName, randomLastName);
        }
    }

    private void generateTeachers() {
        List<String> degrees = readFilePerOneLine("/populate/education_degrees");
        for (int i = 0; i < AMOUNT_OF_TEACHERS; i++) {
            int randomFirstNameIndex = random.nextInt(firstNames.size());
            int randomLastNameIndex = random.nextInt(lastNames.size());
            int randomDegreeIndex = random.nextInt(degrees.size());
            String randomFirstName = firstNames.get(randomFirstNameIndex);
            String randomLastName = lastNames.get(randomLastNameIndex);
            String randomDegree = degrees.get(randomDegreeIndex);
            teacherRepository.save(new Teacher(0L, randomFirstName, randomLastName, randomDegree, 32, "john.dao@gmail.com", "static/images/avatar.png"));
            logger.info("Created teacher: {} {}.", randomFirstName, randomLastName);
        }
    }

    private void generateFaculties() {
        List<String> facultiesNames = readFilePerOneLine("/populate/faculties_names");
        for (String facultyName : facultiesNames) {
            facultyRepository.save(new Faculty(0L, facultyName));
            logger.info("Created faculty: {}.", facultyName);
        }
    }

    private void generateGroups() {
        List<String> groupNames = readFilePerOneLine("/populate/group_names");
        for (String groupName : groupNames) {
            groupRepository.save(new Group(0L, groupName));
            logger.info("Created group: {}.", groupName);
        }
    }

    private void generateCourses() {
        List<String> courseNames = readFilePerOneLine("/populate/course_names");
        List<Faculty> faculties = (List<Faculty>) facultyRepository.findAll();
        for (String courseName : courseNames) {
            Faculty randomFaculty = faculties.get(random.nextInt(faculties.size()));
            courseRepository.save(new Course(0L, courseName, randomFaculty));
            logger.info("Created course: {}.", courseName);
        }
    }

    private void generateStudents() {
        List<Group> groups = (List<Group>) groupRepository.findAll();
        for (int i = 0; i < AMOUNT_OF_STUDENTS; i++) {
            int randomFirstNameIndex = random.nextInt(firstNames.size());
            int randomLastNameIndex = random.nextInt(lastNames.size());
            int randomGroupIndex = random.nextInt(groups.size());
            String randomFirstName = firstNames.get(randomFirstNameIndex);
            String randomLastName = lastNames.get(randomLastNameIndex);
            Group randomGroup = groups.get(randomGroupIndex);
            studentRepository.save(new Student(0L, randomFirstName, randomLastName, randomGroup, 12, "john.dao@gmail.com", "static/images/avatar.png"));
            logger.info("Created Student: {} {}", randomFirstName, randomLastName);

        }
    }

    private void generateLectures() {
        List<Teacher> teachers = (List<Teacher>) teacherRepository.findAll();
        List<Course> courses = (List<Course>) courseRepository.findAll();
        List<String> lectureNames = readFilePerOneLine("/populate/lectures_names");
        List<String> lectureDescriptions = readFilePerOneLine("/populate/lectures_description");
        for (String lectureName : lectureNames) {
            int teacherRandomIndex = random.nextInt(teachers.size());
            int courseRandomIndex = random.nextInt(courses.size());
            int lectureDescriptionRandomIndex = random.nextInt(lectureDescriptions.size());
            Teacher teacher = teachers.get(teacherRandomIndex);
            Course course = courses.get(courseRandomIndex);
            String description = lectureDescriptions.get(lectureDescriptionRandomIndex);
            LocalDateTime randomDate = generateRandomDate();
            lectureRepository.save(new Lecture(0L, course, teacher, lectureName, description, randomDate));
            logger.info("Created lecture: {}", lectureName);
        }
    }

    private LocalDateTime generateRandomDate() {
        LocalDateTime startDate = LocalDateTime.now();

        LocalDateTime randomDate = startDate.plus(random.nextInt(365), ChronoUnit.DAYS);

        int[] fixedHours = {8, 10, 12, 14, 16, 18};
        randomDate = randomDate.withHour(fixedHours[random.nextInt(fixedHours.length)]);

        return randomDate;
    }

    private List<String> readFilePerOneLine(String filePath) {
        List<String> stringList = new ArrayList<>();
        InputStream inputStream = ReaderInputStream.class.getResourceAsStream(filePath);

        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringList.add(line);
                }
            } catch (IOException e) {
                logger.error("Error reading file: " + filePath, e);
                throw new RuntimeException(e);
            }
        } else {
            logger.error("File not found: {}.", filePath);
        }
        return stringList;
    }
}
*/

