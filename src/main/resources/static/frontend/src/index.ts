import {app, BrowserWindow} from "electron";

let window: BrowserWindow;

app.whenReady().then(createWindows);

function createWindows(): void {
    window = new BrowserWindow({
        width: 1920,
        height: 1080,
        webPreferences: {
            preload: __dirname + "/preload.js"
        },
        icon: (__dirname + "/images/knight-green.png").replace("/dist", ""),
        show: false
    });
    window.loadFile("./login.html");
    window.on("ready-to-show", () => window.show());
}