const requestListElement = document.getElementById("requests-list-body");
const requestListReloadButton = document.getElementById("reload-requests-list-btn");
const requestListFilterInput = document.getElementById("requests-search");

window.onload = function () {
    /* Recarrega la llista en carregar la pàgina */
    requestListReloadButton.click();

    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let requestStreamEndpoint = "http://localhost:8080/api/v1/gameRequest/stream/to/user?username=User";
    let requestStream = new EventSource(requestStreamEndpoint);
    const requestMap = new Map();

    requestStream.onmessage = (event) => {
        /* Si el filtre existeix i no està buit */
        if (requestListFilterInput && requestListFilterInput.value !== "") {
            return;
        }
        const request = JSON.parse(event.data);
        /* No acceptar repetits: JSON -> Map -> List */
        requestMap.set(request.id, request);
        const requestList = Array.from(requestMap, function (entry) {
            return entry[1];
        });
        console.log("GET " + requestStreamEndpoint + ": " + JSON.stringify(requestList));

        updateRequestTable(requestList);
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
                        <button id="accept-game-request-${requestingUserUsername}" class="btn btn-sm btn-success" onclick="send('${requestingUserUsername}')"><i class="bi bi-check-lg"></i></button>
                        <button id="reject-game-request-${requestingUserUsername}" class="btn btn-sm btn-danger" onclick="send('${requestingUserUsername}')"><i class="bi bi-x-lg"></i></button>
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
    let requestListEndpoint = "http://localhost:8080/api/v1/gameRequest/list/to?username=User";
    const response = await fetch(requestListEndpoint);
    let requestList = await response.json();
    console.log("GET " + requestListEndpoint + ": " + JSON.stringify(requestList));
    return requestList;
}