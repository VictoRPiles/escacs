const playerListOnBoardElement = document.getElementById("player-list-body") as HTMLElement;
const playerListOnBoardReloadButton = document.getElementById("reload-player-list-btn") as HTMLButtonElement;

window.onload = () => {
    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let playerStreamEndpoint = "http://localhost:8080/api/v1/user/stream/all";
    let playerStream = new EventSource(playerStreamEndpoint);
    const playerMap = new Map();

    playerStream.onmessage = (event) => {
        const player = JSON.parse(event.data);
        /* No acceptar repetits: JSON -> Map -> List */
        playerMap.set(player.id, player);
        const playerList = Array.from(playerMap, function (entry) {
            return entry[1];
        });

        updatePlayerOnBoardTable(playerList);
    };
};

playerListOnBoardReloadButton.addEventListener("click", async () => {
    get("http://localhost:8080/api/v1/user/list", null)
        .then(response => {
            let playerList = JSON.parse(JSON.stringify(response)) as [string];
            updatePlayerOnBoardTable(playerList);
        });
});

function updatePlayerOnBoardTable(playerList: any[]) {
    playerListOnBoardElement.innerHTML = ``;
    playerList.forEach((player) => {
        let username = player["username"];
        let score = player["score"];

        playerListOnBoardElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row"><span>${playerList.indexOf(player) + 1}</span></th>
                    <td class="text-start"><span>${username}</span></td>
                    <td class="text-center"><span>${score}</span></td>
                </tr>`;
    });
}