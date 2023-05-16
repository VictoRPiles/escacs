const playerListFilterInput = document.getElementById("players-search") as HTMLInputElement;
const playerListElement = document.getElementById("player-list-body") as HTMLElement;
const playerListReloadButton = document.getElementById("reload-player-list-btn") as HTMLButtonElement;

window.onload = () => {
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

        updatePlayerTable(playerList);
    };
};

playerListReloadButton.addEventListener("click", async () => {
    get("http://localhost:8080/api/v1/user/list", null)
        .then(response => {
            let playerList = JSON.parse(JSON.stringify(response)) as [string];
            updatePlayerTable(playerList);
        });
});

function updatePlayerTable(playerList: any[]) {
    playerListElement.innerHTML = ``;
    playerList.forEach((player) => {
        let username = player["username"];
        let score = player["score"];

        playerListElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row"><span>${playerList.indexOf(player) + 1}</span></th>
                    <td><span>${username}</span></td>
                    <td class="text-center"><span>${score}</span></td>
                    <td class="text-center">
                        <button id="send-game-request-${username}" class="btn btn-sm btn-primary" onclick="send('${username}')"><i class="bi bi-arrow-right"></i></button>
                    </td>
                </tr>`;
    });
}

function send(requestedUserUsername: string) {
    let requestingUserUsername = sessionStorage.getItem("loggedUserUsername");
    if (!requestingUserUsername) {
        return;
    }
    let parameters = new Map<string, string>([
        ["requestingUserUsername", requestingUserUsername],
        ["requestedUserUsername", requestedUserUsername]
    ]);
    post("http://localhost:8080/api/v1/gameRequest/send", parameters)
        .then(response => {
            let sent = JSON.parse(JSON.stringify(response));
            console.log("Game request sent -> " + sent.id);

            const sendButton = document.getElementById("send-game-request-" + requestedUserUsername) as HTMLButtonElement;
            sendButton.classList.remove("btn-green");
            sendButton.classList.add("btn-success");
            sendButton.innerHTML = `<i class="bi bi-check-lg"></i>`;

            let requestStreamEndpoint = "http://localhost:8080/api/v1/gameRequest/stream/to/user?username=" + requestedUserUsername;
            let requestStream = new EventSource(requestStreamEndpoint);

            requestStream.onmessage = (event) => {
                let requestList = JSON.parse(event.data) as [any];
                requestList.forEach(request => {
                    if (request.id === sent.id && request.accepted) {
                        let parameters = new Map<string, string>([
                            ["gameRequestUUID", sent.id]
                        ]);
                        get("http://localhost:8080/api/v1/game/listByGameRequest", parameters)
                            .then(response => {
                                let game = JSON.parse(JSON.stringify(response)) as any;
                                sessionStorage.setItem("gameId", game.id);
                                console.log("Joining game -> " + sessionStorage.getItem("gameId"));

                                sessionStorage.setItem("requestingUserUsername", game.request.requestingUser.username);
                                sessionStorage.setItem("requestedUserUsername", game.request.requestedUser.username);

                                let alert = document.getElementById("alert") as HTMLElement;
                                let alertMessage = document.getElementById("alert-message") as HTMLElement;
                                alert.classList.remove("d-none");
                                alert.classList.remove("alert-primary");
                                alert.classList.remove("alert-danger");
                                alert.classList.add("alert-success");
                                alertMessage.innerText = requestedUserUsername + " ha acceptat la sol·licitud de joc!";
                            })
                            .catch(error => {
                                reportError(error);
                            });
                    }
                });
            };
        })
        .catch(error => {
            const sendButton = document.getElementById("send-game-request-" + requestedUserUsername) as HTMLButtonElement;
            sendButton.classList.remove("btn-green");
            sendButton.classList.add("btn-danger");
            sendButton.innerHTML = `<i class="bi bi-x-lg"></i>`;
            reportError(error);
        });
}