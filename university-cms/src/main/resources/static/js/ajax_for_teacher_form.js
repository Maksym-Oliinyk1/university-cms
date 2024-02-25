function loadLectures(link, containerId) {
    var pageNumber = link.getAttribute('data-page');

    $.get('/getListLectures', {pageNumber: pageNumber}, function (lectures) {
        console.log(lectures);
        var selectContainer = $('#' + containerId);
        selectContainer.empty();
        $.each(lectures, function (index, lecture) {
            selectContainer.append($('<option>', {
                value: lecture.id,
                text: `${lecture.name}`
            }));
        });
    });

    return false;
}