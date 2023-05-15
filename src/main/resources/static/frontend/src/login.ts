let loggedUser: User;
const loginForm = document.getElementById("login-form") as HTMLFormElement;
if (loginForm) {
    loginForm.addEventListener("submit", (event: SubmitEvent) => {
        event.preventDefault();

        let action = "http://localhost:8080/api/v1/user/login";
        let parameters = formToParameters(new FormData(loginForm));

        post(action, parameters)
            .then(response => {
                let userJson = JSON.parse(JSON.stringify(response));
                let userId = userJson["id"];
                let userUsername = userJson["username"];
                let userEmail = userJson["email"];
                loggedUser = new User(userId, userUsername, userEmail);
                console.log("Login -> " + loggedUser);

                showSuccessInLoginForm();

                /* Quan s'inicia sessió correctament, espera 1 segon i canvia a la pàgina d'inici */
                setTimeout(() => {
                    window.location.replace("./index.html");
                }, 1000);
            })
            .catch(error => {
                showFailureInLoginForm(error);
            });
    });
}

function showSuccessInLoginForm() {
    const emailField = document.getElementById("email");
    const passwordField = document.getElementById("password");
    const validationMessage = document.getElementById("form-validation-message");

    if (emailField) {
        emailField.classList.remove("is-invalid");
        emailField.classList.add("is-valid");
    }
    if (passwordField) {
        passwordField.classList.remove("is-invalid");
        passwordField.classList.add("is-valid");
    }
    if (validationMessage) {
        validationMessage.classList.remove("d-none");
        validationMessage.classList.remove("bg-danger");
        validationMessage.classList.add("bg-success");
        validationMessage.innerText = "Benvingut de nou!";
    }
}

function showFailureInLoginForm(error: Error) {
    const emailField = document.getElementById("email");
    const passwordField = document.getElementById("password");
    const validationMessage = document.getElementById("form-validation-message");

    if (emailField) {
        emailField.classList.add("is-invalid");
    }
    if (passwordField) {
        passwordField.classList.add("is-invalid");
    }

    if (validationMessage) {
        validationMessage.classList.remove("d-none");
        validationMessage.classList.remove("bg-success");
        validationMessage.classList.add("bg-danger");
        validationMessage.innerText = error.message;
    }
}
