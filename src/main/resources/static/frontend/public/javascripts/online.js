const playerListElement = document.getElementById("player-list-body");
const reloadButton = document.getElementById("reload-player-list-btn");

reloadButton.addEventListener("click", async () => {
    let onlinePlayersList = await fetchOnlinePlayersList();

    playerListElement.innerHTML = ``;
    onlinePlayersList.forEach((player) => {
        let username = player["username"];

        let maxUsernameLength = 25;
        if (username.length > maxUsernameLength) {
            username = username.slice(0, maxUsernameLength) + "...";
        }


        playerListElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row">${onlinePlayersList.indexOf(player) + 1}</th>
                    <td>@${username}</td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-primary px-2 py-0"><i class="bi bi-arrow-right"></i></button>
                    </td>
                </tr>`;
    });
});

async function fetchOnlinePlayersList() {
    let playerListEndpoint = "http://localhost:8080/api/v1/user/list";
    const response = await fetch(playerListEndpoint);
    let onlinePlayersList = await response.json();
    console.log("GET " + playerListEndpoint + ": " + JSON.stringify(onlinePlayersList));
    return onlinePlayersList;
}