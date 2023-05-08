const menu = document.getElementById("menu");
menu.innerHTML += `
<nav id="navbar" class="navbar navbar-expand-sm navbar-dark sticky-top bg-dark">
    <!-- Logotype -->
    <a class="navbar-brand" href="./index.html">
        <img class="ms-3 ms-lg-5" src="./images/knight-green.svg" width="50" alt="">
    </a>
    <!-- Button toggler -->
    <button class="me-3 navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-collapse"
            aria-controls="navbar-collapse"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <!-- Links -->
    <div id="navbar-collapse" class="collapse navbar-collapse">
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
    </div>
</nav>
`;