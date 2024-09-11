package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.LectureService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@RestController
public class LectureRestController {
    private final LectureService lectureService;

    public LectureRestController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/getListLectures")
    @ResponseBody
    public ResponseEntity<List<Lecture>> getLectures(
            @RequestParam(defaultValue = "0") int pageNumber) {
        Page<Lecture> lecturesPage =
                lectureService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Lecture> lectures = lecturesPage.getContent();
        return ResponseEntity.ok(lectures);
    }
}
