const {app, dialog, BrowserWindow, electronIpcMain} = require("electron");
const path = require("path");

const createWindow = () => {
    const mainWindow = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: true,
            preload: path.join(__dirname, "preload.js")
        },
    });
    mainWindow.loadFile("public/index.html").then(() => {
        mainWindow.show();
    });

    /* Open the DevTools. */
    /* mainWindow.webContents.openDevTools() */

    return mainWindow;
};

app.whenReady().then(() => {
    createWindow();

    app.on("activate", () => {
        // On macOS, it's common to re-create a window in the app when the
        // dock icon is clicked and there are no other windows open.
        if (BrowserWindow.getAllWindows().length === 0) createWindow();
    });
});

app.on("window-all-closed", () => {
    if (process.platform !== "darwin") app.quit();
});