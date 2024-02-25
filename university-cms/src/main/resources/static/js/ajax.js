function loadContent(element) {
    event.preventDefault();

    let content = element.closest('nav').getAttribute('data-content');
    let page = element.getAttribute('data-page');

    let capitalizedContent = content.charAt(0).toUpperCase() + content.slice(1);

    $.ajax({
        url: `/getList${capitalizedContent}?pageNumber=${page}`,
        type: 'GET',
        success: function (data) {
            $(`#${content}`).empty();

            $(`#${content}`).append($('<option>', {
                value: "",
                text: `Select ${capitalizedContent}`,
                disabled: true,
                selected: false
            }));

            $.each(data, function (i, item) {
                if (content === 'teachers') {
                    $(`#${content}`).append($('<option>', {
                        value: item.id,
                        text: `${item.firstName} ${item.lastName}`
                    }));
                } else if (item.name && item.id) {
                    $(`#${content}`).append($('<option>', {
                        value: item.id,
                        text: `${item.name}`
                    }));
                }
            });
        },
        error: function (error) {
            console.log('Error:', error);
        }
    });
}