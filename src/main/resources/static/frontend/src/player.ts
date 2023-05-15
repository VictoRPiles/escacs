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
                    <th class="text-center" scope="row">${playerList.indexOf(player) + 1}</th>
                    <td><span>${username}</span></td>
                    <td class="text-center"><span>${score}</span></td>
                    <td class="text-center">
                        <button id="send-game-request-${username}" class="btn btn-sm btn-primary" onclick="send('${username}')"><i class="bi bi-arrow-right"></i></button>
                    </td>
                </tr>`;
    });
}