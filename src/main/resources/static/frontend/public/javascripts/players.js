const fs = require("fs");
const jquery = require("jquery");

const playerListElement = document.getElementById("player-list-body");
const playerListReloadButton = document.getElementById("reload-player-list-btn");
const playerListFilterInput = document.getElementById("players-search");

const userData = JSON.parse(fs.readFileSync("login.json"));

jquery(document).ready(() => {
    /* Recarrega la llista en carregar la pàgina */
    playerListReloadButton.click();

    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let playerStreamEndpoint = "http://localhost:8080/api/v1/user/stream/all";
    let playerStream = new EventSource(playerStreamEndpoint);
    const playerMap = new Map();

    playerStream.onmessage = (event) => {
        /* Si el filtre existeix i no està buit */
        if (playerListFilterInput && playerListFilterInput.value !== "") {
            return;
        }
        const player = JSON.parse(event.data);
        /* No acceptar repetits: JSON -> Map -> List */
        playerMap.set(player.id, player);
        const playerList = Array.from(playerMap, function (entry) {
            return entry[1];
        });
        console.log("GET " + playerStreamEndpoint + ": " + JSON.stringify(playerList));

        updatePlayerTable(playerList);
    };
});

playerListReloadButton.addEventListener("click", async () => {
    let playerList = await fetchPlayersList();
    updatePlayerTable(playerList);
});

function updatePlayerTable(playerList) {
    playerListElement.innerHTML = ``;
    playerList.forEach((player) => {
        let username = player["username"];
        let score = player["score"];

        playerListElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row">${playerList.indexOf(player) + 1}</th>
                    <td><span>${username}</span></td>
                    <td class="text-center"><span>${score}</span></td>
                    <td class="text-center">
                        <button id="send-game-request-${username}" class="btn btn-sm btn-green" onclick="send('${username}')"><i class="bi bi-arrow-right"></i></button>
                    </td>
                </tr>`;
    });
}

function filter() {
    let input = document.getElementById("players-search");
    let filter = input.value.toUpperCase();
    let table = document.getElementById("player-list-table");
    let tr = table.getElementsByTagName("tr");

    for (let i = 0; i < tr.length; i++) {
        let td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            let filterValue = td.textContent || td.innerText;
            if (filterValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            }
            else {
                tr[i].style.display = "none";
            }
        }
    }
}

async function fetchPlayersList() {
    let playerListEndpoint = "http://localhost:8080/api/v1/user/list";
    const response = await fetch(playerListEndpoint);
    let playerList = await response.json();
    console.log("GET " + playerListEndpoint + ": " + JSON.stringify(playerList));
    return playerList;
}

async function send(requestedUserUsername) {
    let url = "http://localhost:8080/api/v1/gameRequest/send";

    let fetchOptions = {
        method: "POST"
    };

    let endpoint = url + "?" + new URLSearchParams({
        requestingUserUsername: userData["username"],
        requestedUserUsername: requestedUserUsername
    });

    let response = await fetch(endpoint, fetchOptions);
    let responseBody = await response.json();
    let requestUUID = responseBody["id"];
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
        sendButton.innerHTML = `<i class="bi bi-x-lg"></i>`;
    }

    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let requestStreamEndpoint = "http://localhost:8080/api/v1/gameRequest/stream/to/user?username=" + requestedUserUsername;
    let requestStream = new EventSource(requestStreamEndpoint);

    requestStream.onmessage = async (event) => {
        let requestList = JSON.parse(event.data);
        console.log("GET " + requestStreamEndpoint + ": " + JSON.stringify(requestList));

        for (const request of requestList) {
            if (request["id"] === requestUUID) {
                console.log(request);
            }

            let requestAccepted = request["accepted"];
            if (requestAccepted) {
                console.log("Request " + requestUUID + " accepted");
                let fetchOptions = {
                    method: "GET"
                };

                let endpoint = "http://localhost:8080/api/v1/game/listByGameRequest" + "?" + new URLSearchParams({
                    gameRequestUUID: requestUUID,
                });

                let response = await fetch(endpoint, fetchOptions);
                let responseBody = await response.json();
                console.log("GET " + endpoint + ": " + JSON.stringify(responseBody));

                fs.writeFile("games.json", JSON.stringify(responseBody), function (err) {
                    if (err) {
                        console.log(err);
                    }
                });

                setTimeout(() => {
                    window.location.replace("./index.html");
                }, 1000);
            }
        }
    };
}