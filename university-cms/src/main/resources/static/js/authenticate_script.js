document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault();

    var formData = new FormData(this);

    fetch("/auth/authenticate", {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            var token = data.token;
            localStorage.setItem("token", token);

            var url = "/general/student/showStudent?token=" + token;
            window.location.href = url;
        })
        .catch(error => console.error("Error:", error));
});