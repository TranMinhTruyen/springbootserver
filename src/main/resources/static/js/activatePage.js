function callApi() {
    $.ajax({
        type: "POST",
        url: "/api/account/activateAccount",
        data: { key: "filter", email: "en-US" },
    }).then(function (response) {
        console.log("Success")
    }, function (reason) {
        console.log("Error")
    })
}