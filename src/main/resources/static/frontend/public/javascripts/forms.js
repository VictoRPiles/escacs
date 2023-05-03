const forms = document.querySelector("form");

forms.addEventListener("submit", async (e) => {
    /* Prevé el comportament predeterminat del navegador d'enviar el formulari perquè es puga gestionar de forma alternativa. */
    e.preventDefault();

    let form = e.currentTarget;

    let url = form.action;

    try {
        /* Agafa tots els camps del formulari i posa els valors dels camps a disposició a través d'una instància de FormData. */
        let formData = new FormData(form);

        let responseData = await postFormFieldsAsJson({url, formData});
        console.log(responseData);
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
            Accept: "application/json",
        },
        body: formDataJsonString,
    };

    let response = await fetch(url, fetchOptions);

    /* Si la resposta no és correcta, llança un error (per depurar-lo). */
    if (!response.ok) {
        let error = await response.text();
        throw new Error(error);
    }
    /* Si la resposta és correcta, retorna el cos de la resposta. */
    return response.json();
}