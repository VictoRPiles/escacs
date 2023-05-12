const fs = require("fs");

const requestListElement = document.getElementById("requests-list-body");
const requestListReloadButton = document.getElementById("reload-requests-list-btn");
const requestListFilterInput = document.getElementById("requests-search");

const userData = JSON.parse(fs.readFileSync("login.json"));

let requestList;
window.onload = function () {
    /* Recarrega la llista en carregar la pàgina */
    requestListReloadButton.click();

    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let requestStreamEndpoint = "http://localhost:8080/api/v1/gameRequest/stream/pending/to/user?username=" + userData["username"];
    let requestStream = new EventSource(requestStreamEndpoint);

    requestStream.onmessage = (event) => {
        /* Si el filtre existeix i no està buit */
        if (requestListFilterInput && requestListFilterInput.value !== "") {
            return;
        }
        const request = JSON.parse(event.data);
        requestList = request;

        updateRequestTable(request);
    };
};

requestListReloadButton.addEventListener("click", async () => {
    let requestList = await fetchRequestsList();
    updateRequestTable(requestList);
});

function updateRequestTable(requestList) {
    requestListElement.innerHTML = ``;
    requestList.forEach((request) => {
        let requestingUser = request["requestingUser"];
        let requestUUID = request["id"];
        let requestedAt = request["requestedAt"];
        let requestingUserUsername = requestingUser["username"];
        let requestingUserScore = requestingUser["score"];

        requestListElement.innerHTML += `
                <tr class="align-middle">
                    <th class="text-center" scope="row">${requestList.indexOf(request) + 1}</th>
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

requestListFilterInput.addEventListener("input", filter);

function filter() {
    let input = document.getElementById("requests-search");
    let filter = input.value.toUpperCase();
    let table = document.getElementById("request-list-table");
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

async function fetchRequestsList() {
    let requestListEndpoint = "http://localhost:8080/api/v1/gameRequest/pending/to?username=" + userData["username"];
    const response = await fetch(requestListEndpoint);
    let requestList = await response.json();

    return requestList;
}

async function accept(id) {
    let url = "http://localhost:8080/api/v1/gameRequest/accept";

    let request = requestList.find(request => {
        return request.id === id;
    });

    let fetchOptions = {
        method: "PUT"
    };

    let endpointWithParameters = url + "?" + new URLSearchParams({
        uuid: request.id
    });

    let response = await fetch(endpointWithParameters, fetchOptions);
    let responseJSON = await response.json();

    if (!response.ok) {
        throw new Error(responseJSON.message);
    }

    requestListReloadButton.click();

    await createGame(id);
}

async function createGame(id) {
    let url = "http://localhost:8080/api/v1/game/create";

    let request = requestList.find(request => {
        return request.id === id;
    });

    let fetchOptions = {
        method: "POST"
    };

    let endpointWithParameters = url + "?" + new URLSearchParams({
        gameRequestUUID: request.id
    });

    let response = await fetch(endpointWithParameters, fetchOptions);
    let responseJSON = await response.json();

    if (!response.ok) {
        throw new Error(responseJSON.message);
    }

    requestListReloadButton.click();

    fs.writeFile("games.json", JSON.stringify(responseJSON), function (err) {
        if (err) {
            console.log(err);
        }
    });

    setTimeout(() => {
        window.location.replace("./index.html");
    }, 1000);
}

async function reject(id) {
    let url = "http://localhost:8080/api/v1/gameRequest/reject";

    let request = requestList.find(request => {
        return request.id === id;
    });

    let fetchOptions = {
        method: "PUT"
    };

    let endpointWithParameters = url + "?" + new URLSearchParams({
        uuid: request.id
    });

    let response = await fetch(endpointWithParameters, fetchOptions);
    let responseJSON = await response.json();

    if (!response.ok) {
        throw new Error(responseJSON.message);
    }

    requestListReloadButton.click();
}