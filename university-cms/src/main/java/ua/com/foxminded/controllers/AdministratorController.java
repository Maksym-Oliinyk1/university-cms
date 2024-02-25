package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.*;
import ua.com.foxminded.service.*;

@Controller
public class AdministratorController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    @RequestMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("administrator", new Administrator());
        return "admin";
    }

    private final AdministratorService administratorService;

    @RequestMapping("/admin/manage-faculty")
    public String showFacultyPage(Model model) {
        model.addAttribute("faculty", new Faculty());
        model.addAttribute("courses", courseService.findAll());
        return "manage-faculty";
    }

    @RequestMapping("/admin/manage-group")
    public String showGroupPage(Model model) {
        model.addAttribute("group", new Group());
        return "manage-group";
    }

    @RequestMapping("/admin/manage-student")
    public String showStudentPage(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.findAll());
        return "manage-student";
    }

    @RequestMapping("/admin/manage-teacher")
    public String showTeacherPage(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "manage-teacher";
    }

    @RequestMapping("/admin/manage-lecture")
    public String showLecturePage(Model model) {
        model.addAttribute("lecture", new Lecture());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "manage-lecture";
    }

    private final CourseService courseService;
    private final FacultyService facultyService;
    private final GroupService groupService;
    private final LectureService lectureService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AdministratorController(AdministratorService administratorService,
                                   CourseService courseService,
                                   FacultyService facultyService,
                                   GroupService groupService,
                                   LectureService lectureService,
                                   StudentService studentService,
                                   TeacherService teacherService) {
        this.administratorService = administratorService;
        this.courseService = courseService;
        this.facultyService = facultyService;
        this.groupService = groupService;
        this.lectureService = lectureService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @RequestMapping("/admin/manage-course")
    public String showCoursePage(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("faculties", facultyService.findAll());
        return "manage-course";
    }

    @GetMapping("/admin/listAdmin")
    public String listAdmin(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Administrator> adminPage = administratorService.findAll(PageRequest.of(page, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("administrator", new Administrator());
        model.addAttribute("administrators", adminPage.getContent());
        model.addAttribute("page", adminPage);
        return "admin";
    }

    @GetMapping("/admin/showAdmin/{id}")
    public String showAdministrator(@PathVariable Long id, Model model) {
        Administrator administrator = administratorService.findById(id);
        model.addAttribute("administrator", administrator);
        return "admin";
    }

    @PostMapping("/admin/addAdmin")
    public String addAdministrator(@ModelAttribute Administrator administrator, Model model) {
        administratorService.save(administrator);
        model.addAttribute("administrator", administrator);
        return "redirect:/admin";
    }

    @PostMapping("/admin/deleteAdmin/{id}")
    public String deleteAdministrator(@PathVariable Long id) {
        administratorService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/manage-course/listCourse")
    public String listCourses(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Course> coursePage = courseService.findAll(PageRequest.of(page, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("course", new Course());
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("page", coursePage);
        return "manage-course";
    }

    @GetMapping("/admin/manage-course/showCourse/{id}")
    public String showCourse(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "manage-course";
    }

    @PostMapping("/admin/manage-course/addCourse")
    public String addCourse(@ModelAttribute Course course, Model model) {
        courseService.save(course);
        model.addAttribute("course", course);
        return "redirect:/admin/manage-course";
    }

    @PostMapping("/admin/manage-course/deleteCourse/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "redirect:/admin/manage-course";
    }

    @GetMapping("/admin/manage-faculty/listFaculty")
    public String listFaculties(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Faculty> pageFaculty = facultyService.findAll(PageRequest.of(page, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("faculty", new Faculty());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("faculties", pageFaculty.getContent());
        model.addAttribute("page", pageFaculty);
        return "manage-faculty";
    }

    @GetMapping("/admin/manage-faculty/showFaculty/{id}")
    public String showFaculty(@PathVariable Long id, Model model) {
        Faculty faculty = facultyService.findById(id);
        model.addAttribute("faculty", faculty);
        return "manage-faculty";
    }

    @PostMapping("/admin/manage-faculty/addFaculty")
    public String addFaculty(@ModelAttribute Faculty faculty, Model model) {
        facultyService.save(faculty);
        model.addAttribute("faculty", new Faculty());
        return "redirect:/admin/manage-faculty";
    }

    @PostMapping("/admin/manage-faculty/deleteFaculty/{id}")
    public String deleteFaculty(@RequestParam Long id) {
        facultyService.delete(id);
        return "redirect:/admin/manage-faculty";
    }

    @PostMapping("/admin/manage-faculty/attachCourseToFaculty")
    public String attachCourseToFaculty(
            @RequestParam Long courseId,
            @RequestParam Long facultyId) {
        facultyService.attachCourseToFaculty(courseId, facultyId);
        return "redirect:/admin/manage-faculty";
    }

    @PostMapping("/admin/manage-faculty/detachCourseFromFaculty")
    public String detachCourseFromFaculty(
            @RequestParam Long courseId,
            @RequestParam Long facultyId) {
        facultyService.detachCourseFromFaculty(courseId, facultyId);
        return "redirect:/admin/manage-faculty";
    }

    @GetMapping("/admin/manage-group/listGroup")
    public String listGroups(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Group> pageGroup = groupService.findAll(PageRequest.of(page, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("group", new Group());
        model.addAttribute("groups", pageGroup.getContent());
        model.addAttribute("page", pageGroup);
        return "manage-group";
    }

    @GetMapping("admin/manage-group/showGroup/{id}")
    public String showGroup(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        return "manage-group";
    }

    @PostMapping("/admin/manage-group/addGroup")
    public String addGroup(@ModelAttribute Group group, Model model) {
        groupService.save(group);
        model.addAttribute("group", group);
        return "redirect:/admin/manage-group";
    }

    @PostMapping("admin/manage-group/deleteGroup/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return "redirect:/admin/manage-group";
    }

    @PostMapping("admin/manage-group/detachStudentFromGroup")
    public String detachStudentFromGroup(
            @RequestParam Long studentId,
            @RequestParam Long groupId) {
        groupService.detachStudentFromGroup(studentId, groupId);
        return "redirect:/admin/manage-group";
    }

    @GetMapping("/admin/manage-lecture/listLecture")
    public String listLectures(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Lecture> pageLecture = lectureService.findAll(PageRequest.of(page, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("lecture", new Lecture());
        model.addAttribute("lectures", pageLecture.getContent());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("page", pageLecture);
        return "manage-lecture";
    }

    @GetMapping("/admin/manage-lecture/showLecture/{id}")
    public String showLecture(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id);
        model.addAttribute("lecture", lecture);
        return "manage-lecture";
    }

    @PostMapping("/admin/manage-lecture/addLecture")
    public String addLecture(@ModelAttribute Lecture lecture, Model model) {
        lectureService.save(lecture);
        model.addAttribute("lecture", lecture);
        return "redirect:/admin/manage-lecture";
    }

    @PostMapping("/admin/manage-lecture/deleteLecture/{id}")
    public String deleteLecture(@PathVariable Long id) {
        lectureService.delete(id);
        return "redirect:/admin/manage-lecture";
    }

    @PostMapping("/admin/manage-lecture/attachGroupToLecture")
    public String attachGroupToLecture(
            @RequestParam Long groupId,
            @RequestParam Long lectureId) {
        lectureService.attachGroupToLecture(groupId, lectureId);
        return "redirect:/admin/manage-lecture";
    }

    @PostMapping("/admin/manage-lecture/detachGroupFromLecture")
    public String detachGroupFromLecture(
            @RequestParam Long groupId,
            @RequestParam Long lectureId) {
        lectureService.detachGroupFromLecture(groupId, lectureId);
        return "redirect:/admin/manage-lecture";
    }

    @GetMapping("/admin/manage-student/listStudent")
    public String listStudent(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Student> pageStudent = studentService.findAll(PageRequest.of(page, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("students", pageStudent.getContent());
        model.addAttribute("page", pageStudent);
        return "manage-student";
    }

    @GetMapping("/admin/manage-student/showStudent/{id}")
    public String showStudent(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "manage-student";
    }

    @PostMapping("/admin/manage-student/addStudent")
    public String addStudent(@ModelAttribute Student student, Model model) {
        studentService.save(student);
        model.addAttribute("student", student);
        model.addAttribute("groups", groupService.findAll());
        return "redirect:/admin/manage-student";
    }

    @PostMapping("/admin/manage-student/deleteStudent/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/admin/manage-student";
    }

    @PostMapping("admin/manage-student/attachStudentToGroup")
    public String attachStudentToGroup(
            @RequestParam Long studentId,
            @RequestParam Long groupId) {
        groupService.attachStudentToGroup(studentId, groupId);
        return "redirect:/admin/manage-student";
    }

    @GetMapping("/admin/manage-teacher/listTeacher")
    public String listTeacher(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Teacher> pageTeacher = teacherService.findAll(PageRequest.of(page, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("teachers", pageTeacher.getContent());
        model.addAttribute("page", pageTeacher);
        return "manage-teacher";
    }

    @GetMapping("/admin/manage-teacher/showTeacher/{id}")
    public String showTeacher(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "manage-teacher";
    }

    @PostMapping("/admin/manage-teacher/addTeacher")
    public String addTeacher(@ModelAttribute Teacher teacher, Model model) {
        teacherService.save(teacher);
        model.addAttribute("teacher", teacher);
        return "redirect:/admin/manage-teacher";
    }

    @PostMapping("/admin/manage-teacher/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);
        return "redirect:/admin/manage-teacher";
    }

    @PostMapping("/admin/manage-lecture/attachLectureToTeacher")
    public String attachLectureToTeacher(
            @RequestParam Long lectureId,
            @RequestParam Long teacherId) {
        teacherService.attachLectureToTeacher(lectureId, teacherId);
        return "redirect:/admin/manage-lecture";
    }

    @PostMapping("admin/manage-lecture/detachLectureFromTeacher")
    public String detachLectureFromTeacher(
            @RequestParam Long lectureId,
            @RequestParam Long teacherId) {
        teacherService.detachLectureFromTeacher(lectureId, teacherId);
        return "redirect:/admin/manage-lecture";
    }
}
