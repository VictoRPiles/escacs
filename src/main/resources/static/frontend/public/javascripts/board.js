const board = document.getElementById("board");
const moveList = document.getElementById("move-list-body");
const lightPlayerName = document.getElementById("light-player-name");
const darkPlayerName = document.getElementById("dark-player-name");

let rows = 8;
let columns = 8;
let squares = 0;
let moveCount = 0;

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
            console.log("GET " + moveStreamEndpoint + ": " + JSON.stringify(moveList));

            let newMove = moveList[moveList.length - 1];

            /* Realment s'ha executat un moviment nou, no és un missatge periòdic del stream */
            if (previousMove.id !== newMove.id) {
                console.log("New move: " + JSON.stringify(newMove.value));
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
        board.innerHTML += (row % 2 === column % 2)
            ? `<div id="square-${squares}" class="square square-light"></div>`
            : `<div id="square-${squares}" class="square square-dark"></div>`;
        squares++;
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
    console.log("From square " + fromSquareIndex + " to " + toSquareIndex + " with class " + squareClass);

    items[fromSquareIndex].classList.remove(squareClass);
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

function notationToIndex(chessNotation) {
    const column = chessNotation.charCodeAt(0) - "a".charCodeAt(0);
    const rank = parseInt(chessNotation.charAt(1)) - 1;
    return (7 - rank) * 8 + column;
}
