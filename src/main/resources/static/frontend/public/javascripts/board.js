const board = document.getElementById("board");

let rows = 8;
let columns = 8;

/* Crea les caselles */
for (let row = 0; row < rows; row++) {
    for (let column = 0; column < columns; column++) {
        if (row % 2 === column % 2) {
            board.innerHTML += `<div class="square square-light"></div>`;
        } else {
            board.innerHTML += `<div class="square square-dark"></div>`;
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
        count++;
    }
}