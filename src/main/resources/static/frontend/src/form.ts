function formToParameters(formData: FormData) {
    let formEntries = formData.entries();
    let parameters = new Map<string, string>;
    for (let formEntry of formEntries) {
        parameters.set(formEntry[0], formEntry[1].toString());
    }
    return parameters;
}