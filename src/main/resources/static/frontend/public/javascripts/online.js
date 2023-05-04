const playerListElement = document.getElementById("player-list-body");
const reloadButton = document.getElementById("reload-player-list-btn");

window.onload = function () {
    /* Recarrega la llista en carregar la pàgina */
    reloadButton.click();
};

reloadButton.addEventListener("click", async () => {
    let playerList = await fetchPlayersList();

    playerListElement.innerHTML = ``;
    playerList.forEach((player) => {
        let username = player["username"];

        playerListElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row">${playerList.indexOf(player) + 1}</th>
                    <td><span>${username}</span></td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-green"><i class="bi bi-arrow-right"></i></button>
                    </td>
                </tr>`;
    });
});

async function fetchPlayersList() {
    let playerListEndpoint = "http://localhost:8080/api/v1/user/list";
    const response = await fetch(playerListEndpoint);
    let onlinePlayersList = await response.json();
    console.log("GET " + playerListEndpoint + ": " + JSON.stringify(onlinePlayersList));
    return onlinePlayersList;
}