const menu = document.getElementById("menu");
if (menu) {
    let username = sessionStorage.getItem("loggedUserUsername");
    menu.innerHTML += `
<nav id="navbar" class="navbar navbar-expand-sm navbar-dark sticky-top bg-dark">
    <!-- Logotype -->
    <a class="navbar-brand" href="./index.html">
        <img class="ms-3 ms-lg-5" src="./images/knight-green.svg" width="50" alt="">
    </a>
    <!-- Links -->
    <div id="navbar-collapse" class="collapse container-fluid">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0 mx-5">
            <li class="nav-item">
                <a class="nav-link fw-bold active fs-4" href="./index.html">Inici</a>
            </li>
            <li class="nav-item">
                <a class="nav-link fw-bold fs-4" href="./players.html">Llistat de jugadors</a>
            </li>
             <li class="nav-item">
                <a class="nav-link fw-bold fs-4" href="./requests.html">Sol·licituds de joc</a>
            </li>
        </ul>
        <!-- Nom del usuari -->
        <button type="button" class="btn btn-primary position-relative mr-auto mt-2 mt-lg-0 mx-5 text-center fw-bold">
            <i class="bi bi-person-fill mt-auto mb-auto me-1"></i>${username}
            <span class="position-absolute top-0 start-100 translate-middle p-2 bg-success border border-light rounded-circle">
            </span>
        </button>
    </div>
</nav>
`;
}