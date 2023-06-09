const gameListFilterInput = document.getElementById("game-search") as HTMLInputElement;
const gameListElement = document.getElementById("game-list-body") as HTMLElement;
const gameListReloadButton = document.getElementById("reload-game-list-btn") as HTMLButtonElement;

window.onload = () => {
    let username = sessionStorage.getItem("loggedUserUsername");
    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let gameStreamEndpoint = "http://localhost:8080/api/v1/gameRequest/stream/pending/to/user?username=" + username;
    let gameStream = new EventSource(gameStreamEndpoint);

    gameStream.onmessage = (event) => {
        /* Si el filtre existeix i no està buit */
        if (gameListFilterInput && gameListFilterInput.value !== "") {
            return;
        }
        gameListReloadButton.click();
    };
};

function sortByRequestedAt(gamesList: [any]) {
    return gamesList.sort(function (requestA, requestB) {
        if (requestA.requestedAt > requestB.requestedAt) {
            return -1;
        }
        if (requestA.requestedAt < requestB.requestedAt) {
            return 1;
        }
        return 0;
    });
}

gameListReloadButton.addEventListener("click", async () => {
    let username = sessionStorage.getItem("loggedUserUsername");
    if (!username) {
        return;
    }

    let parameters = new Map<string, string>([
        ["username", username]
    ]);
    get("http://localhost:8080/api/v1/gameRequest/pending/to", parameters)
        .then(response => {
            let gamesList = JSON.parse(JSON.stringify(response)) as [any];
            updateGamesTable(sortByRequestedAt(gamesList));
        });
});

function updateGamesTable(gamesList: any[]) {
    gameListElement.innerHTML = ``;
    gamesList.forEach((game) => {
        let requestingUser = game["requestingUser"];
        let requestUUID = game["id"];
        let requestedAt = game["requestedAt"];
        let requestingUserUsername = requestingUser["username"];
        let requestingUserScore = requestingUser["score"];

        gameListElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row">${gamesList.indexOf(game) + 1}</th>
                    <td><span>${requestingUserUsername}</span></td>
                    <td class="text-center"><span>${requestingUserScore}</span></td>
                    <td class="text-center"><span>${requestedAt}</span></td>
                    <td class="text-center">
                        <button id="accept-game-request-${requestUUID}" class="btn btn-sm btn-success" onclick="accept('${requestUUID}')"><i class="bi bi-check-lg"></i></button>
                        <button id="reject-game-request-${requestUUID}" class="btn btn-sm btn-danger" onclick="reject('${requestUUID}')"><i class="bi bi-x-lg"></i></button>
                    </td>
                </tr>`;
    });
}

async function accept(id: string) {
    let parameters = new Map<string, string>([
        ["uuid", id]
    ]);
    put("http://localhost:8080/api/v1/gameRequest/accept", parameters)
        .then(response => {
            let accepted = JSON.parse(JSON.stringify(response));
            console.log("Accepted request -> " + accepted.id);
            gameListReloadButton.click();
            createGame(id);
        })
        .catch(error => {
            reportError(error);
        });
}

function createGame(id: string) {
    let parameters = new Map<string, string>([
        ["gameRequestUUID", id]
    ]);
    post("http://localhost:8080/api/v1/game/create", parameters)
        .then(response => {
            let created = JSON.parse(JSON.stringify(response));
            console.log("Game created -> " + created.id);
            gameListReloadButton.click();

            sessionStorage.setItem("gameId", created.id);
            sessionStorage.setItem("requestingUserUsername", created.request.requestingUser.username);
            sessionStorage.setItem("requestedUserUsername", created.request.requestedUser.username);
            console.log("Joining game -> " + sessionStorage.getItem("gameId"));

            setTimeout(() => {
                window.location.replace("./index.html");
            }, 1000);
        })
        .catch(error => {
            reportError(error);
        });
}

async function reject(id: string) {
    let parameters = new Map<string, string>([
        ["uuid", id]
    ]);
    put("http://localhost:8080/api/v1/gameRequest/reject", parameters)
        .then(response => {
            let rejected = JSON.parse(JSON.stringify(response));
            console.log("Rejected request -> " + rejected.id);
            gameListReloadButton.click();
        })
        .catch(error => {
            reportError(error);
        });
}