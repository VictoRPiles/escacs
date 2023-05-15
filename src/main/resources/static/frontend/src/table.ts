function filter() {
    const input = document.getElementById("players-search") as HTMLInputElement;
    const filter = input.value.toUpperCase();
    const table = document.getElementById("player-list-table") as HTMLTableElement;
    const tr = table.getElementsByTagName("tr");

    for (let i = 0; i < tr.length; i++) {
        let td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            let filterValue = td.textContent || td.innerText;
            if (filterValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}