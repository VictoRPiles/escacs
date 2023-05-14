const HTTP_GET = "GET";
const HTTP_POST = "POST";
const HTTP_PUT = "PUT";

async function fetchWithMethod<T>(httpMethod: string, url: string, parameters: Map<string, string> | null) {
    if (parameters !== null) {
        url = url.concat(toUrlParameters(parameters));
        console.log(httpMethod + " " + url);
    }
    let response = await fetch(url, {method: httpMethod});
    let responseBody = await response.json();
    if (!response.ok) {
        throw new Error(responseBody.message);
    }
    return responseBody as Promise<T>;
}

async function get<T>(url: string, parameters: Map<string, string> | null): Promise<T> {
    return await fetchWithMethod(HTTP_GET, url, parameters);
}

async function post<T>(url: string, parameters: Map<string, string> | null): Promise<T> {
    return await fetchWithMethod(HTTP_POST, url, parameters);
}

async function put<T>(url: string, parameters: Map<string, string> | null): Promise<T> {
    return await fetchWithMethod(HTTP_PUT, url, parameters);
}

function toUrlParameters(parameters: Map<string, string>) {
    let urlParameters: string = "";
    urlParameters = urlParameters.concat("?");
    for (let entry of parameters.entries()) {
        let key = entry[0];
        let value = entry[1];
        urlParameters = urlParameters.concat(key).concat("=").concat(value).concat("&");
    }
    return urlParameters;
}
