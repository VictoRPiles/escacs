const registerForm = document.getElementById("register-form") as HTMLFormElement;
if (registerForm) {
    registerForm.addEventListener("submit", (event: SubmitEvent) => {
        event.preventDefault();

        let action = "http://localhost:8080/api/v1/user/register";
        let parameters = formToParameters(new FormData(registerForm));

        post(action, parameters)
            .then(response => {
                let userJson = JSON.parse(JSON.stringify(response));
                let userId = userJson["id"];
                let userUsername = userJson["username"];
                let userEmail = userJson["email"];
                console.log("Registered -> " + new User(userId, userUsername, userEmail));

                showSuccessInRegisterForm();

                /* Quan s'inicia sessió correctament, espera 1 segon i canvia a la pàgina d'inici de sessió */
                setTimeout(() => {
                    window.location.replace("./login.html");
                }, 1000);
            })
            .catch(error => {
                showFailureInRegisterForm(error);
            });
    });
}

function showSuccessInRegisterForm() {
    const usernameField = document.getElementById("username");
    const emailField = document.getElementById("email");
    const passwordField = document.getElementById("password");
    const validationMessage = document.getElementById("form-validation-message");

    if (usernameField) {
        usernameField.classList.remove("is-invalid");
        usernameField.classList.add("is-valid");
    }
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

function showFailureInRegisterForm(error: Error) {
    const usernameField = document.getElementById("username");
    const emailField = document.getElementById("email");
    const passwordField = document.getElementById("password");
    const validationMessage = document.getElementById("form-validation-message");

    if (usernameField) {
        usernameField.classList.add("is-invalid");
    }
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
