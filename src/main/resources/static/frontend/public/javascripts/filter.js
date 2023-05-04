const filterInput = document.getElementById("search");

filterInput.addEventListener("keyup", filter);

function filter() {
    let input = document.getElementById("search");
    let filter = input.value.toUpperCase();
    let table = document.getElementById("player-list-table");
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