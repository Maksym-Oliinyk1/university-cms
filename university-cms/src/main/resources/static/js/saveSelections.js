document.addEventListener("DOMContentLoaded", function () {
    updateFieldsFromLocalStorage();

    document.getElementById("name").addEventListener("input", function () {
        saveLectureName();
    });

    document.getElementById("description").addEventListener("input", function () {
        saveLectureDescription();
    });

    document.getElementById("date").addEventListener("input", function () {
        saveLectureDate();
    });
});

function saveLectureName() {
    var lectureName = document.getElementById("lectureName").value;
    sessionStorage.setItem("lectureName", lectureName);
}

function saveLectureDescription() {
    var lectureDescription = document.getElementById("description").value;
    sessionStorage.setItem("lectureDescription", lectureDescription);
}

function saveLectureDate() {
    var lectureDate = document.getElementById("date").value;
    sessionStorage.setItem("lectureDate", lectureDate);
}

function updateFieldsFromLocalStorage() {
    var lectureName = sessionStorage.getItem("lectureName");
    if (lectureName) {
        document.getElementById("lectureName").value = lectureName;
    }

    var lectureDescription = sessionStorage.getItem("lectureDescription");
    if (lectureDescription) {
        document.getElementById("description").value = lectureDescription;
    }

    var lectureDate = sessionStorage.getItem("lectureDate");
    if (lectureDate) {
        document.getElementById("date").value = lectureDate;
    }
}
