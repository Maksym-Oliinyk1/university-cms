function loadFaculties(link, containerId) {
    var pageNumber = link.getAttribute('data-page');

    $.get('/getListFaculties', {pageNumber: pageNumber}, function (faculties) {
        console.log(faculties);
        var selectContainer = $('#' + containerId);
        selectContainer.empty();
        $.each(faculties, function (index, faculty) {
            selectContainer.append('<option value="' + faculty.id + '">' + faculty.name + '</option>');
        });
    });

    return false;
}