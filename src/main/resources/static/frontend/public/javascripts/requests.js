async function send(requestedUserUsername) {
    let url = "http://localhost:8080/api/v1/gameRequest/send";

    let fetchOptions = {
        method: "POST"
    };

    let endpoint = url + "?" + new URLSearchParams({
        requestingUserUsername: "Administrator",
        requestedUserUsername: requestedUserUsername
    });

    let response = await fetch(endpoint, fetchOptions);
    let responseBody = await response.json();
    console.log("POST " + endpoint + ": " + JSON.stringify(responseBody));

    const sendButton = document.getElementById("send-game-request-" + requestedUserUsername);
    if (response.ok) {
        sendButton.classList.remove("btn-green");
        sendButton.classList.add("btn-success");
        sendButton.innerHTML = `<i class="bi bi-check-lg"></i>`;
    }
    else {
        sendButton.classList.remove("btn-green");
        sendButton.classList.add("btn-danger");
        sendButton.innerHTML = `<i class="bi bi-exclamation-octagon-fill"></i>`;
    }
}