const form = document.querySelector("form");

form.addEventListener("submit", async (e) => {
    /* Prevé el comportament predeterminat del navegador d'enviar el formulari perquè es puga gestionar de forma alternativa. */
    e.preventDefault();

    let form = e.currentTarget;

    let url = form.action;

    try {
        /* Agafa tots els camps del formulari i posa els valors dels camps a disposició a través d'una instància de FormData. */
        let formData = new FormData(form);

        let responseData = await postFormFieldsAsParameters({url, formData});
        console.log(responseData);
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

    let response = await fetch(url + "?" + new URLSearchParams({
        email: formDataObject.email,
        password: formDataObject.password
    }), fetchOptions);

    /* Si la resposta no és correcta, llança un error (per depurar-lo). */
    if (!response.ok) {
        let error = await response.text();
        throw new Error(error);
    }
    /* Si la resposta és correcta, retorna el cos de la resposta. */
    return response.json();
}