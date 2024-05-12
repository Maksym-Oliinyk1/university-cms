function loadGroups(link, containerId) {
    var pageNumber = link.getAttribute('data-page');

    $.get('/getList/groups', {pageNumber: pageNumber}, function (groups) {
        console.log(groups);

        var selectContainer = $('#' + containerId);

        var newOptions = groups.map(function (group) {
            return '<option value="' + group.id + '">' + group.name + '</option>';
        });

        selectContainer.empty().append(newOptions);
    });

    return false;
}