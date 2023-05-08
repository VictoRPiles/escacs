const playerListElement = document.getElementById("player-list-body");
const reloadButton = document.getElementById("reload-player-list-btn");
const filterInput = document.getElementById("search");

window.onload = function () {
    /* Recarrega la llista en carregar la pàgina */
    reloadButton.click();

    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let playerStreamEndpoint = "http://localhost:8080/api/v1/user/stream/all";
    let playerStream = new EventSource(playerStreamEndpoint);
    const playerMap = new Map();

    playerStream.onmessage = (event) => {
        /* Si el filtre existeix i no està buit */
        if (filterInput && filterInput.value !== "") {
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
};

reloadButton.addEventListener("click", async () => {
    let playerList = await fetchPlayersList();
    updatePlayerTable(playerList);
});

function updatePlayerTable(playerList) {
    playerListElement.innerHTML = ``;
    playerList.forEach((player) => {
        let username = player["username"];

        playerListElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row">${playerList.indexOf(player) + 1}</th>
                    <td><span>${username}</span></td>
                    <td class="text-center">
                        <button id="send-game-request-${username}" class="btn btn-sm btn-green" onclick="send('${username}')"><i class="bi bi-arrow-right"></i></button>
                    </td>
                </tr>`;
    });
}

filterInput.addEventListener("input", filter);

function filter() {
    let input = document.getElementById("search");
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