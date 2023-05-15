const ROWS_NUMBER = 8;
const COLUMNS_NUMBER = 8;

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
            let squareElement;
            if (squareCount < 10) {
                boardElement.innerHTML += (row % 2 === column % 2)
                    ? `<div id="square-0${squareCount}" class="square square-light"></div>`
                    : `<div id="square-0${squareCount}" class="square square-dark"></div>`;

                squareElement = document.getElementById("square-0" + squareCount);
                squareCount++;
            } else {
                boardElement.innerHTML += (row % 2 === column % 2)
                    ? `<div id="square-${squareCount}" class="square square-light"></div>`
                    : `<div id="square-${squareCount}" class="square square-dark"></div>`;

                squareElement = document.getElementById("square-" + squareCount);
                squareCount++;
            }
        }
    }
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
