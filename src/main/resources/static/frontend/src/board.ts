const ROWS_NUMBER = 8;
const COLUMNS_NUMBER = 8;

let isSquareSelected = false;
let selectedSquare: HTMLElement;

const boardElement = document.getElementById("board");
if (boardElement) {
    createSquares();
    setPiecesToOpening();
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

function squareClicked(clickedSquare: HTMLElement) {
    let squaresElements = document.querySelectorAll(".square");

    squaresElements.forEach((squareElement) => {
        if (squareElement.classList.contains("square-light-selected")) {
            squareElement.classList.remove("square-light-selected");
        } else if (squareElement.classList.contains("square-dark-selected")) {
            squareElement.classList.remove("square-dark-selected");
        }
    });

    if (!isSquareSelected) {
        if (!pieceBySquare(clickedSquare)) {
            return;
        }

        isSquareSelected = true;
        selectedSquare = clickedSquare;
        highlightSquareAsValid(clickedSquare);
        highlightValidMoves(clickedSquare);
    } else {
        isSquareSelected = false;

        let moveInformation = toMoveInformation(clickedSquare);

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
            post("http://localhost:8080/api/v1/move/execute", parameters)
                .then(response => {
                    console.log(response);
                })
                .catch(error => {
                    reportError(error);
                });
            console.log("Executing move -> " + moveInformation);
        }
    }
}

function toMoveInformation(clickedSquare: HTMLElement) {
    let originSquareId = selectedSquare.id;
    let destinationSquareId = clickedSquare.id;

    let originSquareIndex = originSquareId.substring(originSquareId.length - 2);
    let destinationSquareIndex = destinationSquareId.substring(destinationSquareId.length - 2);

    let originSquareNotation = indexToNotation(parseInt(originSquareIndex));
    let destinationSquareNotation = indexToNotation(parseInt(destinationSquareIndex));

    let selectedSquarePiece = pieceBySquare(selectedSquare);

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

                if (pieceBySquare(destinationSquare as HTMLElement) != null) {
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
    let selectedSquarePiece = pieceBySquare(squareClicked);

    return selectedSquarePiece + squareNotation;
}

function boardToContext(): string {
    let context = "";
    let squaresElements = document.querySelectorAll(".square");
    squaresElements.forEach((squareElement) => {
        let squareItemId = squareElement.id;
        let squareItemIndex = squareItemId.substring(squareItemId.length - 2);
        let squareItemNotation = indexToNotation(parseInt(squareItemIndex));
        let pieceOnItem = pieceBySquare(squareElement as HTMLElement);
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

function pieceBySquare(square: HTMLElement) {
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
