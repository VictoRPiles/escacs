const board = document.getElementById("board");

let rows = 8;
let columns = 8;
let squares = 0;

/* Crea les caselles */
for (let row = 0; row < rows; row++) {
    for (let column = 0; column < columns; column++) {
        if (row % 2 === column % 2) {
            board.innerHTML += `<div id="square-${squares}" class="square square-light"></div>`;
        }
        else {
            board.innerHTML += `<div id="square-${squares}" class="square square-dark"></div>`;
        }
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