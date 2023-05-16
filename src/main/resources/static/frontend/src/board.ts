const CHECK = "CHECK";
const CHECK_MATE = "CHECK_MATE";

const ROWS_NUMBER = 8;
const COLUMNS_NUMBER = 8;

let isSquareSelected = false;
let selectedSquare: HTMLElement;
let moveCount = 0;

function listenGameMoves() {
    let gameId = sessionStorage.getItem("gameId");

    /* Se subscriu al flux de dades -> actualitza automàticament la llista */
    let moveStreamEndpoint = "http://localhost:8080/api/v1/move/stream/byGame?gameId=" + gameId;
    let moveStream = new EventSource(moveStreamEndpoint);
    const moveMap = new Map();
    let previousMove: any;

    moveStream.onmessage = (event) => {
        if (!previousMove) {
            const lightPlayerName = document.getElementById("light-player-name") as HTMLElement;
            const darkPlayerName = document.getElementById("dark-player-name") as HTMLElement;

            let requestingUserUsername = sessionStorage.getItem("requestingUserUsername") as string;
            let requestedUserUsername = sessionStorage.getItem("requestedUserUsername") as string;

            lightPlayerName.innerText = requestingUserUsername;
            darkPlayerName.innerText = requestedUserUsername;
        }

        const move = JSON.parse(event.data);
        /* No acceptar repetits: JSON -> Map -> List */
        moveMap.set(move.id, move);
        const moveList = Array.from(moveMap, function (entry) {
            return entry[1];
        });

        let newMove = moveList[moveList.length - 1];

        /* Realment s'ha executat un moviment nou, no és un missatge periòdic del stream */
        if (!previousMove || previousMove.id !== newMove.id) {
            previousMove = newMove;

            let movingPlayer = newMove["user"]["username"];
            console.log("Move by: " + movingPlayer + " -> " + newMove["value"]);
            console.log("Status -> " + newMove.status);

            if (newMove.status === CHECK || newMove.status === CHECK_MATE) {
                markCheck(newMove.value);

                if (newMove.status === CHECK_MATE) {
                    let alert = document.getElementById("alert") as HTMLElement;
                    let alertMessage = document.getElementById("alert-message") as HTMLElement;
                    alert.classList.remove("d-none");
                    alert.classList.remove("alert-success");
                    let loggedUser = sessionStorage.getItem("loggedUserUsername");
                    if (movingPlayer === loggedUser) {
                        alert.classList.add("alert-primary");
                    } else {
                        alert.classList.add("alert-danger");
                    }
                    alertMessage.innerText = movingPlayer + " ha fet escac i mat!";
                }
            }

            updateBoard(newMove.value);
            updateMoveList(newMove.value);
            moveCount++;
        }
    };
}

const moveList = document.getElementById("move-list-body") as HTMLElement;

function updateMoveList(move: any) {
    let from = move.substring(2, 4);
    let to = move.substring(4);
    let moveInfo = (from + " - " + to).toUpperCase();
    if (moveCount % 2 === 0) {
        moveList.innerHTML += `
            <tr class="align-middle" id="moves-${moveCount}-${moveCount + 1}">
                <th class="text-center" scope="row">${(moveCount / 2) + 1}</th>
                <td id="move-${moveCount}"><i id="move-${moveCount}-icon" class="fas me-2"></i>${moveInfo}</td>
                <td id="move-${moveCount + 1}"></td>
            </tr>
        `;
        const firstColumnMoveIcon = document.getElementById("move-" + moveCount + "-icon") as HTMLElement;
        firstColumnMoveIcon.classList.add(classByMoveValue(move));
    } else {
        const secondColumnMove = document.getElementById("move-" + moveCount) as HTMLElement;
        secondColumnMove.innerHTML = `<i id="move-${moveCount}-icon" class="fas me-2"></i>${moveInfo}`;
        const secondColumnMoveIcon = document.getElementById("move-" + moveCount + "-icon") as HTMLElement;
        secondColumnMoveIcon.classList.add(classByMoveValue(move));
    }
}

function updateBoard(move: string) {
    let squareClass, chessPiece, pieceColor;
    if (move.startsWith("b")) {
        chessPiece = "bishop";
    } else if (move.startsWith("k")) {
        chessPiece = "king";
    } else if (move.startsWith("n")) {
        chessPiece = "knight";
    } else if (move.startsWith("p")) {
        chessPiece = "pawn";
    } else if (move.startsWith("q")) {
        chessPiece = "queen";
    } else if (move.startsWith("r")) {
        chessPiece = "rook";
    }

    if (move.charAt(1) === "l") {
        pieceColor = "light";
    } else if (move.charAt(1) === "d") {
        pieceColor = "dark";
    }

    let from = move.substring(2, 4).replace("x", "");
    let to = move.substring(4).replace("x", "");
    squareClass = chessPiece + "-" + pieceColor;
    let fromSquareIndex = notationToIndex(from);
    let toSquareIndex = notationToIndex(to);

    let squaresElements = document.querySelectorAll(".square");
    squaresElements[fromSquareIndex].classList.remove(squareClass);
    removePiece(squaresElements[toSquareIndex]);
    squaresElements[toSquareIndex].classList.add(squareClass);
}

function removePiece(square: Element) {
    if (square.classList.contains("rook-light")) {
        square.classList.remove("rook-light");
    } else if (square.classList.contains("rook-dark")) {
        square.classList.remove("rook-dark");
    } else if (square.classList.contains("knight-light")) {
        square.classList.remove("knight-light");
    } else if (square.classList.contains("knight-dark")) {
        square.classList.remove("knight-dark");
    } else if (square.classList.contains("bishop-light")) {
        square.classList.remove("bishop-light");
    } else if (square.classList.contains("bishop-dark")) {
        square.classList.remove("bishop-dark");
    } else if (square.classList.contains("queen-light")) {
        square.classList.remove("queen-light");
    } else if (square.classList.contains("queen-dark")) {
        square.classList.remove("queen-dark");
    } else if (square.classList.contains("king-light")) {
        square.classList.remove("king-light");
    } else if (square.classList.contains("king-dark")) {
        square.classList.remove("king-dark");
    } else if (square.classList.contains("pawn-light")) {
        square.classList.remove("pawn-light");
    } else if (square.classList.contains("pawn-dark")) {
        square.classList.remove("pawn-dark");
    }
}

function classByMoveValue(move: any): string {
    let chessPiece;
    if (move.startsWith("b")) {
        chessPiece = "fa-chess-bishop";
    } else if (move.startsWith("k")) {
        chessPiece = "fa-chess-king";
    } else if (move.startsWith("n")) {
        chessPiece = "fa-chess-knight";
    } else if (move.startsWith("p")) {
        chessPiece = "fa-chess-pawn";
    } else if (move.startsWith("q")) {
        chessPiece = "fa-chess-queen";
    } else if (move.startsWith("r")) {
        chessPiece = "fa-chess-rook";
    } else chessPiece = "";

    return chessPiece;
}

const boardElement = document.getElementById("board");
if (boardElement) {
    createSquares();
    setPiecesToOpening();
    listenGameMoves();
}

function createSquares() {
    if (!boardElement) {
        return;
    }

    let squareCount = 0;
    for (let row = 0; row < ROWS_NUMBER; row++) {
        for (let column = 0; column < COLUMNS_NUMBER; column++) {
            if (squareCount < 10) {
                boardElement.innerHTML += (row % 2 === column % 2)
                    ? `<div id="square-0${squareCount}" class="square square-light"></div>`
                    : `<div id="square-0${squareCount}" class="square square-dark"></div>`;
            } else {
                boardElement.innerHTML += (row % 2 === column % 2)
                    ? `<div id="square-${squareCount}" class="square square-light"></div>`
                    : `<div id="square-${squareCount}" class="square square-dark"></div>`;
            }
            squareCount++;
        }
    }

    let squaresElements = document.querySelectorAll(".square");
    squaresElements.forEach((squareElement) => {
        squareElement.addEventListener("click", () => {
            if (squareElement) {
                squareClicked(squareElement as HTMLElement);
            }
        });
    });
}

function isUserOwner(pieceColor: string) {
    /* Només el jugador propietari de la peça pot veure els moviments vàlids d'aquesta */
    let requestingUserUsername = sessionStorage.getItem("requestingUserUsername");
    let loggedUsername = sessionStorage.getItem("loggedUserUsername");
    let isLightPlayer = requestingUserUsername === loggedUsername;
    if (isLightPlayer && pieceColor !== "light") {
        return false;
    }
    if (!isLightPlayer && pieceColor !== "dark") {
        return false;
    }
    return true;
}

function squareClicked(clickedSquare: HTMLElement) {
    let squaresElements = document.querySelectorAll(".square");

    squaresElements.forEach((squareElement) => {
        if (squareElement.classList.contains("square-light-selected")) {
            squareElement.classList.remove("square-light-selected");
        } else if (squareElement.classList.contains("square-dark-selected")) {
            squareElement.classList.remove("square-dark-selected");
        } else if (squareElement.classList.contains("bg-danger")) {
            squareElement.classList.remove("bg-danger");
        }
    });

    if (!isSquareSelected) {
        let piece = pieceInSquare(clickedSquare);
        if (!piece) {
            return;
        }

        let pieceColor = "";
        if (piece.charAt(1) === "l") {
            pieceColor = "light";
        } else if (piece.charAt(1) === "d") {
            pieceColor = "dark";
        }

        if (!isUserOwner(pieceColor)) {
            return;
        }

        isSquareSelected = true;
        selectedSquare = clickedSquare;
        highlightSquareAsValid(clickedSquare);
        highlightValidMoves(clickedSquare);
    } else {
        isSquareSelected = false;
        executeMove(toMoveInformation(clickedSquare));
    }
}

function markCheck(moveInformation: string) {
    let kingClass = "";
    if (moveInformation.charAt(1) === "l") {
        kingClass = "king-dark";
    } else if (moveInformation.charAt(1) === "d") {
        kingClass = "king-light";
    }

    let squaresElements = document.querySelectorAll(".square");
    squaresElements.forEach(squareElement => {
        if (squareElement.classList.contains(kingClass)) {
            highlightSquareAsAttack(squareElement as HTMLElement);
        }
    });
}

function executeMove(moveInformation: string) {
    let context = boardToContext();
    let gameId = sessionStorage.getItem("gameId");
    let username = sessionStorage.getItem("loggedUserUsername");
    if (gameId && username) {
        let parameters = new Map<string, string>([
            ["move", moveInformation],
            ["context", context],
            ["gameId", gameId],
            ["username", username]
        ]);
        console.log("Executing move -> " + moveInformation);
        post("http://localhost:8080/api/v1/move/execute", parameters);
    }
}

function toMoveInformation(clickedSquare: HTMLElement) {
    let originSquareId = selectedSquare.id;
    let destinationSquareId = clickedSquare.id;

    let originSquareIndex = originSquareId.substring(originSquareId.length - 2);
    let destinationSquareIndex = destinationSquareId.substring(destinationSquareId.length - 2);

    let originSquareNotation = indexToNotation(parseInt(originSquareIndex));
    let destinationSquareNotation = indexToNotation(parseInt(destinationSquareIndex));

    let selectedSquarePiece = pieceInSquare(selectedSquare);

    return selectedSquarePiece + originSquareNotation + destinationSquareNotation;
}

function highlightValidMoves(squareClicked: HTMLElement) {
    let squaresElements = document.querySelectorAll(".square");
    let pieceInformation = squareToPieceInformation(squareClicked);
    let context = boardToContext();
    let parameters = new Map<string, string>([
        ["piece", pieceInformation],
        ["context", context]
    ]);
    post("http://localhost:8080/api/v1/move/listValid", parameters)
        .then(response => {
            let responseBody = JSON.parse(JSON.stringify(response)) as [string];
            responseBody.forEach((move) => {
                let destinationSquareIndex = notationToIndex(move.substring(move.length - 2));
                let destinationSquare = squaresElements[destinationSquareIndex];

                if (pieceInSquare(destinationSquare as HTMLElement) != null) {
                    highlightSquareAsAttack(destinationSquare as HTMLElement);
                } else {
                    highlightSquareAsValid(destinationSquare as HTMLElement);
                }
            });
        })
        .catch(error => {
            reportError(error);
        });
}

function squareToPieceInformation(squareClicked: HTMLElement) {
    let squareId = squareClicked.id;
    let squareIndex = squareId.substring(squareId.length - 2);
    let squareNotation = indexToNotation(parseInt(squareIndex));
    let selectedSquarePiece = pieceInSquare(squareClicked);

    return selectedSquarePiece + squareNotation;
}

function boardToContext(): string {
    let context = "";
    let squaresElements = document.querySelectorAll(".square");
    squaresElements.forEach((squareElement) => {
        let squareItemId = squareElement.id;
        let squareItemIndex = squareItemId.substring(squareItemId.length - 2);
        let squareItemNotation = indexToNotation(parseInt(squareItemIndex));
        let pieceOnItem = pieceInSquare(squareElement as HTMLElement);
        if (pieceOnItem !== null) {
            let squareInformation = pieceOnItem + squareItemNotation;
            context += squareInformation;
        }
    });

    return context;
}

function highlightSquareAsValid(square: HTMLElement) {
    if (square.classList.contains("square-light")) {
        square.classList.add("square-light-selected");
    } else if (square.classList.contains("square-dark")) {
        square.classList.add("square-dark-selected");
    }
}

function highlightSquareAsAttack(square: HTMLElement) {
    if (square.classList.contains("square-light")) {
        square.classList.add("bg-danger");
    } else if (square.classList.contains("square-dark")) {
        square.classList.add("bg-danger");
    }
}

function pieceInSquare(square: HTMLElement) {
    let pieceInSquare;

    if (square.classList.contains("rook-light")) {
        pieceInSquare = "rl";
    } else if (square.classList.contains("rook-dark")) {
        pieceInSquare = "rd";
    } else if (square.classList.contains("knight-light")) {
        pieceInSquare = "nl";
    } else if (square.classList.contains("knight-dark")) {
        pieceInSquare = "nd";
    } else if (square.classList.contains("bishop-light")) {
        pieceInSquare = "bl";
    } else if (square.classList.contains("bishop-dark")) {
        pieceInSquare = "bd";
    } else if (square.classList.contains("queen-light")) {
        pieceInSquare = "ql";
    } else if (square.classList.contains("queen-dark")) {
        pieceInSquare = "qd";
    } else if (square.classList.contains("king-light")) {
        pieceInSquare = "kl";
    } else if (square.classList.contains("king-dark")) {
        pieceInSquare = "kd";
    } else if (square.classList.contains("pawn-light")) {
        pieceInSquare = "pl";
    } else if (square.classList.contains("pawn-dark")) {
        pieceInSquare = "pd";
    } else {
        pieceInSquare = null;
    }

    return pieceInSquare;
}

function indexToNotation(squareIndex: number) {
    const file = String.fromCharCode("a".charCodeAt(0) + (squareIndex % 8));
    const rank = 8 - Math.floor(squareIndex / 8);
    return file + rank;
}

function notationToIndex(notation: string) {
    const column = notation.charCodeAt(0) - "a".charCodeAt(0);
    const rank = parseInt(notation.charAt(1)) - 1;
    return (7 - rank) * 8 + column;
}

function setPiecesToOpening() {
    let squareCount = 0;
    let squaresElements = document.querySelectorAll(".square");

    for (let row = 1; row <= ROWS_NUMBER; row++) {
        for (let column = 1; column <= COLUMNS_NUMBER; column++) {
            let squareElement = squaresElements[squareCount];

            if (squareCount === 0 || squareCount === 7) {
                squareElement.classList.add("rook-dark");
            } else if (squareCount === 1 || squareCount === 6) {
                squareElement.classList.add("knight-dark");
            } else if (squareCount === 2 || squareCount === 5) {
                squareElement.classList.add("bishop-dark");
            } else if (squareCount === 3) {
                squareElement.classList.add("queen-dark");
            } else if (squareCount === 4) {
                squareElement.classList.add("king-dark");
            } else if (squareCount >= 8 && squareCount <= 15) {
                squareElement.classList.add("pawn-dark");
            } else if (squareCount === 56 || squareCount === 63) {
                squareElement.classList.add("rook-light");
            } else if (squareCount === 57 || squareCount === 62) {
                squareElement.classList.add("knight-light");
            } else if (squareCount === 58 || squareCount === 61) {
                squareElement.classList.add("bishop-light");
            } else if (squareCount === 59) {
                squareElement.classList.add("queen-light");
            } else if (squareCount === 60) {
                squareElement.classList.add("king-light");
            } else if (squareCount >= 48 && squareCount <= 55) {
                squareElement.classList.add("pawn-light");
            }

            squareCount++;
        }
    }
}
