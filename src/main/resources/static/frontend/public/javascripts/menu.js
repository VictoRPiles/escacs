const fs = require("fs");
const menu = document.getElementById("menu");
const userData = JSON.parse(fs.readFileSync("login.json"));
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
                <a class="nav-link active" href="./index.html">Inici</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="./players.html">Llistat de jugadors</a>
            </li>
             <li class="nav-item">
                <a class="nav-link" href="./requests.html">Sol·licituds de joc</a>
            </li>
        </ul>
        <!-- Nom del usuari -->
        <div class="d-flex btn btn-light mr-auto mt-2 mt-lg-0 mx-5 text-center">
            <span id="username" class="fw-bold">
                <i class="bi bi-person-fill mt-auto mb-auto"></i>
                ${userData["username"]}
            </span>
        </div>
    </div>
</nav>
`;