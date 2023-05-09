const fs = require("fs");

const loginForm = document.getElementById("login-form");
const emailField = document.getElementById("email");
const passwordField = document.getElementById("password");
const validationMessage = document.getElementById("form-validation-message");

loginForm.addEventListener("submit", async (e) => {
    /* Prevé el comportament predeterminat del navegador d'enviar el formulari perquè es puga gestionar de forma alternativa. */
    e.preventDefault();

    let form = e.currentTarget;

    let url = form.action;

    try {
        /* Agafa tots els camps del formulari i posa els valors dels camps a disposició a través d'una instància de FormData. */
        let formData = new FormData(form);

        let response = await postFormFieldsAsParameters({url, formData});

        fs.writeFile("login.json", JSON.stringify(response), function (err) {
            if (err) {

            }
        });
        /* Quan s'inicia sessió correctament, espera 1 segon i canvia a la pàgina de jugadors */
        setTimeout(() => {
            window.location.replace("./index.html");
        }, 1000);
    } catch (error) {
        /* Si es produeix un error, mostra'l a la consola (per depurar-lo). */
        console.error(error);
    }
});

/**
 * Funció auxiliar per fer una sol·licitud POST amb dades com a paràmetres utilitzant Fetch.
 */
async function postFormFieldsAsParameters({url, formData}) {
    let formDataObject = Object.fromEntries(formData.entries());

    let fetchOptions = {
        method: "POST"
    };

    let loginEndpoint = url + "?" + new URLSearchParams({
        email: formDataObject.email,
        password: formDataObject.password
    });

    let response = await fetch(loginEndpoint, fetchOptions);
    let responseBody = await response.json();

    /* Si la resposta no és correcta, llança un error (per depurar-lo). */
    if (!response.ok) {
        emailField.classList.add("is-invalid");
        passwordField.classList.add("is-invalid");

        validationMessage.classList.remove("d-none");
        validationMessage.classList.remove("bg-primary");
        validationMessage.classList.add("bg-danger");
        let error = responseBody;
        validationMessage.innerText = error.message;
        throw new Error(error);
    }

    emailField.classList.remove("is-invalid");
    emailField.classList.add("is-valid");
    passwordField.classList.remove("is-invalid");
    passwordField.classList.add("is-valid");

    validationMessage.classList.remove("d-none");
    validationMessage.classList.remove("bg-danger");
    validationMessage.classList.add("bg-primary");
    validationMessage.innerText = "Benvingut de nou!";
    /* Si la resposta és correcta, retorna el cos de la resposta. */
    return responseBody;
}