const board = document.getElementById("board");
const moveList = document.getElementById("move-list-body");
const lightPlayerName = document.getElementById("light-player-name");
const darkPlayerName = document.getElementById("dark-player-name");

let rows = 8;
let columns = 8;
let squares = 0;
let moveCount = 0;

let isSquareSelected = false;
let selectedSquare;
let targetSquare;

jquery(document).ready(() => {
    fs.exists("games.json", function (exist) {
        let gameData;
        if (exist) {
            gameData = JSON.parse(fs.readFileSync("games.json"));
        }

        /* Se subscriu al flux de dades -> actualitza automàticament la llista */
        let moveStreamEndpoint = "http://localhost:8080/api/v1/move/stream/byGame?gameId=" + gameData["id"];
        let moveStream = new EventSource(moveStreamEndpoint);
        const moveMap = new Map();

        let previousMove = "";

        moveStream.onmessage = (event) => {
            gameData = JSON.parse(fs.readFileSync("games.json"));
            lightPlayerName.innerText = gameData["request"]["requestingUser"]["username"];
            darkPlayerName.innerText = gameData["request"]["requestedUser"]["username"];

            const move = JSON.parse(event.data);
            /* No acceptar repetits: JSON -> Map -> List */
            moveMap.set(move.id, move);
            const moveList = Array.from(moveMap, function (entry) {
                return entry[1];
            });

            let newMove = moveList[moveList.length - 1];

            /* Realment s'ha executat un moviment nou, no és un missatge periòdic del stream */
            if (previousMove.id !== newMove.id) {
                previousMove = newMove;

                updateBoard(newMove.value);
                updateMoveList(newMove.value);
                moveCount++;
            }
        };
    });
});

/* Crea les caselles */
for (let row = 0; row < rows; row++) {
    for (let column = 0; column < columns; column++) {
        if (squares < 10) {
            board.innerHTML += (row % 2 === column % 2)
                ? `<div id="square-0${squares}" class="square square-light"></div>`
                : `<div id="square-0${squares}" class="square square-dark"></div>`;
            squares++;
        }
        else {
            board.innerHTML += (row % 2 === column % 2)
                ? `<div id="square-${squares}" class="square square-light"></div>`
                : `<div id="square-${squares}" class="square square-dark"></div>`;
            squares++;
        }
    }
}

const items = document.querySelectorAll(".square");
let count = 0;

/* Estilitza les caselles */
for (let row = 1; row <= rows; row++) {
    for (let column = 1; column <= columns; column++) {
        let item = items[count];

        item.style["-ms-grid-column"] = column;
        item.style["-ms-grid-row"] = row;

        /* Torre obscura */
        if (count === 0 || count === 7) {
            item.classList.add("rook-dark");
        }
        /* Cavall obscur */
        else if (count === 1 || count === 6) {
            item.classList.add("knight-dark");
        }
        /* Alfil obscur */
        else if (count === 2 || count === 5) {
            item.classList.add("bishop-dark");
        }
        /* Reina obscura */
        else if (count === 3) {
            item.classList.add("queen-dark");
        }
        /* Rei obscur */
        else if (count === 4) {
            item.classList.add("king-dark");
        }
        /* Peó obscur */
        else if (count >= 8 && count <= 15) {
            item.classList.add("pawn-dark");
        }
        /* Torre clara */
        else if (count === 56 || count === 63) {
            item.classList.add("rook-light");
        }
        /* Cavall clar */
        else if (count === 57 || count === 62) {
            item.classList.add("knight-light");
        }
        /* Alfil clar */
        else if (count === 58 || count === 61) {
            item.classList.add("bishop-light");
        }
        /* Reina clara */
        else if (count === 59) {
            item.classList.add("queen-light");
        }
        /* Rei clar */
        else if (count === 60) {
            item.classList.add("king-light");
        }
        /* Peó clar */
        else if (count >= 48 && count <= 55) {
            item.classList.add("pawn-light");
        }
        count++;
    }
}

function highlightSquare(square) {
    if (square.classList.contains("square-light")) {
        square.classList.add("square-light-selected");
    }
    else if (square.classList.contains("square-dark")) {
        square.classList.add("square-dark-selected");
    }
}

function highlightSquareAttack(square) {
    if (square.classList.contains("square-light")) {
        square.classList.add("bg-danger");
    }
    else if (square.classList.contains("square-dark")) {
        square.classList.add("bg-danger");
    }
}

/* AddEventListener a les caselles */
for (let i = 0; i < items.length; i++) {
    let square = items[i];

    square.addEventListener("click", async () => {
        let context = "";

        for (let j = 0; j < items.length; j++) {
            let squareItem = items[j];

            if (squareItem.classList.contains("bg-danger")) {
                squareItem.classList.remove("bg-danger");
            }

            if (squareItem.classList.contains("square-light-selected")) {
                squareItem.classList.remove("square-light-selected");
            }
            else if (squareItem.classList.contains("square-dark-selected")) {
                squareItem.classList.remove("square-dark-selected");
            }

            let squareItemId = squareItem.id;
            let squareItemIndex = squareItemId.substring(squareItemId.length - 2);
            let squareItemNotation = indexToNotation(squareItemIndex);
            let pieceOnItem = pieceBySquare(squareItem);
            if (pieceOnItem !== null) {
                let squareInformation = pieceOnItem + squareItemNotation;
                context += squareInformation;
            }
        }

        if (!isSquareSelected) {
            selectedSquare = square;

            let selectedSquareId = selectedSquare.id;
            let selectedSquarePiece = pieceBySquare(selectedSquare);
            /* Si hi ha peça a la casella */
            if (selectedSquarePiece !== null) {
                highlightSquare(square);
                isSquareSelected = true;

                let selectedSquareIndex = selectedSquareId.substring(selectedSquareId.length - 2);
                let selectedSquareNotation = indexToNotation(selectedSquareIndex);

                let pieceInfo = selectedSquarePiece + selectedSquareNotation;

                let fetchOptions = {
                    method: "POST"
                };

                let endpoint = "http://localhost:8080/api/v1/move/listValid" + "?" + new URLSearchParams({
                    piece: pieceInfo,
                    context: context
                });

                let response = await fetch(endpoint, fetchOptions);
                let responseBody = await response.json();

                responseBody.forEach((move) => {
                    let validSquareIndex = notationToIndex(move.substring(move.length - 2));
                    let validSquare = items[validSquareIndex];

                    if (pieceBySquare(validSquare) != null) {
                        highlightSquareAttack(validSquare);
                    }
                    else highlightSquare(validSquare);
                });
            }
        }
        else {
            targetSquare = square;

            let selectedSquareId = selectedSquare.id;
            let targetSquareId = targetSquare.id;
            let selectedSquarePiece = pieceBySquare(selectedSquare);
            /* No hi ha peça a la casella */
            if (selectedSquarePiece === null) {
                return;
            }

            let selectedSquareIndex = selectedSquareId.substring(selectedSquareId.length - 2);
            let targetSquareIndex = targetSquareId.substring(targetSquareId.length - 2);

            let selectedSquareNotation = indexToNotation(selectedSquareIndex);
            let targetSquareNotation = indexToNotation(targetSquareIndex);

            let move = selectedSquarePiece + selectedSquareNotation + targetSquareNotation;

            const userData = JSON.parse(fs.readFileSync("login.json"));

            fs.exists("games.json", async function (exist) {
                let gameData;
                if (exist) {
                    gameData = JSON.parse(fs.readFileSync("games.json"));
                }

                let fetchOptions = {
                    method: "POST"
                };

                let endpoint = "http://localhost:8080/api/v1/move/execute" + "?" + new URLSearchParams({
                    move: move,
                    context: context,
                    gameId: gameData["id"],
                    username: userData["username"]
                });

                let response = await fetch(endpoint, fetchOptions);
                let responseBody = await response.json();

                isSquareSelected = false;
            });
        }
    });

}

function updateBoard(move) {
    let squareClass, chessPiece, pieceColor;
    if (move.startsWith("b")) {
        chessPiece = "bishop";
    }
    else if (move.startsWith("k")) {
        chessPiece = "king";
    }
    else if (move.startsWith("n")) {
        chessPiece = "knight";
    }
    else if (move.startsWith("p")) {
        chessPiece = "pawn";
    }
    else if (move.startsWith("q")) {
        chessPiece = "queen";
    }
    else if (move.startsWith("r")) {
        chessPiece = "rook";
    }

    if (move.charAt(1) === "l") {
        pieceColor = "light";
    }
    else if (move.charAt(1) === "d") {
        pieceColor = "dark";
    }

    let from = move.substring(2, 4).replace("x", "");
    let to = move.substring(4).replace("x", "");
    squareClass = chessPiece + "-" + pieceColor;
    fromSquareIndex = notationToIndex(from);
    toSquareIndex = notationToIndex(to);

    items[fromSquareIndex].classList.remove(squareClass);
    removeSquarePiece(items[toSquareIndex]);
    items[toSquareIndex].classList.add(squareClass);
}

function updateMoveList(move) {
    let from = move.substring(2, 4).replace("x", "");
    let to = move.substring(4).replace("x", "");
    let moveInfo = (from + " - " + to).toUpperCase();

    if (moveCount % 2 === 0) {
        moveList.innerHTML += `
            <tr class="align-middle" id="moves-${moveCount}-${moveCount + 1}">
                <th class="text-center" scope="row">${(moveCount / 2) + 1}</th>
                <td id="move-${moveCount}"><i id="move-${moveCount}-icon" class="fas me-2"></i>${moveInfo}</td>
                <td id="move-${moveCount + 1}"></td>
            </tr>
        `;
        const firstColumnMoveIcon = document.getElementById("move-" + moveCount + "-icon");
        firstColumnMoveIcon.classList.add(...classByMoveValue(move));
    }
    else {
        const secondColumnMove = document.getElementById("move-" + moveCount);
        secondColumnMove.innerHTML = `<i id="move-${moveCount}-icon" class="fas me-2"></i>${moveInfo}`;
        const secondColumnMoveIcon = document.getElementById("move-" + moveCount + "-icon");
        secondColumnMoveIcon.classList.add(...classByMoveValue(move));
    }
}

function classByMoveValue(move) {
    let chessPiece, textColor = "text-dark";
    if (move.startsWith("b")) {
        chessPiece = "fa-chess-bishop";
    }
    else if (move.startsWith("k")) {
        chessPiece = "fa-chess-king";
    }
    else if (move.startsWith("n")) {
        chessPiece = "fa-chess-knight";
    }
    else if (move.startsWith("p")) {
        chessPiece = "fa-chess-pawn";
    }
    else if (move.startsWith("q")) {
        chessPiece = "fa-chess-queen";
    }
    else if (move.startsWith("r")) {
        chessPiece = "fa-chess-rook";
    }

    if (move.includes("x")) {
        textColor = "text-danger";
    }

    return [chessPiece, textColor];
}

function pieceBySquare(square) {
    let pieceInSquare;

    if (square.classList.contains("rook-light")) {
        pieceInSquare = "rl";
    }
    else if (square.classList.contains("rook-dark")) {
        pieceInSquare = "rd";
    }
    else if (square.classList.contains("knight-light")) {
        pieceInSquare = "nl";
    }
    else if (square.classList.contains("knight-dark")) {
        pieceInSquare = "nd";
    }
    else if (square.classList.contains("bishop-light")) {
        pieceInSquare = "bl";
    }
    else if (square.classList.contains("bishop-dark")) {
        pieceInSquare = "bd";
    }
    else if (square.classList.contains("queen-light")) {
        pieceInSquare = "ql";
    }
    else if (square.classList.contains("queen-dark")) {
        pieceInSquare = "qd";
    }
    else if (square.classList.contains("king-light")) {
        pieceInSquare = "kl";
    }
    else if (square.classList.contains("king-dark")) {
        pieceInSquare = "kd";
    }
    else if (square.classList.contains("pawn-light")) {
        pieceInSquare = "pl";
    }
    else if (square.classList.contains("pawn-dark")) {
        pieceInSquare = "pd";
    }
    else {
        pieceInSquare = null;
    }

    return pieceInSquare;
}

function removeSquarePiece(square) {
    if (square.classList.contains("rook-light")) {
        square.classList.remove("rook-light");
    }
    else if (square.classList.contains("rook-dark")) {
        square.classList.remove("rook-dark");
    }
    else if (square.classList.contains("knight-light")) {
        square.classList.remove("knight-light");
    }
    else if (square.classList.contains("knight-dark")) {
        square.classList.remove("knight-dark");
    }
    else if (square.classList.contains("bishop-light")) {
        square.classList.remove("bishop-light");
    }
    else if (square.classList.contains("bishop-dark")) {
        square.classList.remove("bishop-dark");
    }
    else if (square.classList.contains("queen-light")) {
        square.classList.remove("queen-light");
    }
    else if (square.classList.contains("queen-dark")) {
        square.classList.remove("queen-dark");
    }
    else if (square.classList.contains("king-light")) {
        square.classList.remove("king-light");
    }
    else if (square.classList.contains("king-dark")) {
        square.classList.remove("king-dark");
    }
    else if (square.classList.contains("pawn-light")) {
        square.classList.remove("pawn-light");
    }
    else if (square.classList.contains("pawn-dark")) {
        square.classList.remove("pawn-dark");
    }
}

function notationToIndex(chessNotation) {
    const column = chessNotation.charCodeAt(0) - "a".charCodeAt(0);
    const rank = parseInt(chessNotation.charAt(1)) - 1;
    return (7 - rank) * 8 + column;
}

function indexToNotation(squareIndex) {
    squareIndex = parseInt(squareIndex);

    const file = String.fromCharCode("a".charCodeAt(0) + (squareIndex % 8));
    const rank = 8 - Math.floor(squareIndex / 8);
    return file + rank;
}
