const registerForm = document.getElementById("register-form");
const usernameField = document.getElementById("username");
const emailField = document.getElementById("email");
const passwordField = document.getElementById("password");
const validationMessage = document.getElementById("form-validation-message");

registerForm.addEventListener("submit", async (e) => {
    /* Prevé el comportament predeterminat del navegador d'enviar el formulari perquè es puga gestionar de forma alternativa. */
    e.preventDefault();

    let form = e.currentTarget;

    let url = form.action;

    try {
        /* Agafa tots els camps del formulari i posa els valors dels camps a disposició a través d'una instància de FormData. */
        let formData = new FormData(form);

        let responseData = await postFormFieldsAsJson({url, formData});
        /* Quan es registra correctament, espera 1 segon i canvia a la pàgina d'inici de sessió */
        setTimeout(() => {
            window.location.replace("./login.html");
        }, 1000);
    } catch (error) {
        /* Si es produeix un error, mostra'l a la consola (per depurar-lo). */
        console.error(error);
    }
});

/**
 * Funció auxiliar per fer una sol·licitud POST amb dades en format JSON utilitzant Fetch.
 */
async function postFormFieldsAsJson({url, formData}) {
    let formDataObject = Object.fromEntries(formData.entries());
    let formDataJsonString = JSON.stringify(formDataObject);

    let fetchOptions = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json"
        },
        body: formDataJsonString
    };

    let response = await fetch(url, fetchOptions);
    console.log("POST " + url + ": " + JSON.stringify(response));

    /* Si la resposta no és correcta, llança un error (per depurar-lo). */
    if (!response.ok) {
        let error = await response.json();

        if (error.message.includes("username")) {
            usernameField.classList.add("is-invalid");
        }
        if (error.message.includes("email")) {
            usernameField.classList.remove("is-invalid");
            usernameField.classList.add("is-valid");
            emailField.classList.add("is-invalid");
        }
        passwordField.classList.add("is-valid");

        validationMessage.classList.remove("d-none");
        validationMessage.classList.remove("bg-primary");
        validationMessage.classList.add("bg-danger");
        validationMessage.innerText = error.message;
        throw new Error(error);
    }

    usernameField.classList.add("is-valid");
    usernameField.classList.remove("is-invalid");
    emailField.classList.remove("is-invalid");
    emailField.classList.add("is-valid");
    passwordField.classList.add("is-valid");

    validationMessage.classList.remove("d-none");
    validationMessage.classList.remove("bg-danger");
    validationMessage.classList.add("bg-primary");
    validationMessage.innerText = "Usuari registrat!";
    /* Si la resposta és correcta, retorna el cos de la resposta. */
    return response.json();
}