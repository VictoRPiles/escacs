import {app, BrowserWindow} from "electron"

let window: BrowserWindow

app.whenReady().then(createWindows)

function createWindows(): void {
    window = new BrowserWindow({
        width: 1920,
        height: 1080,
        webPreferences: {
            preload: __dirname + "/preload.js"
        },
        show: false
    })
    window.loadFile("./index.html")
    window.on("ready-to-show", () => window.show())
}