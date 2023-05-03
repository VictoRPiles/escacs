const {app, dialog, BrowserWindow, electronIpcMain} = require("electron");
const path = require("path");

const createWindow = () => {
    let icon = path.join(__dirname, "public/images/icon-green.png");
    console.log(icon);
    const mainWindow = new BrowserWindow({
        width: 1200,
        height: 800,
        icon: icon,
        webPreferences: {
            preload: path.join(__dirname, "preload.js")
        },
    });
    mainWindow.loadFile("public/login.html").then(() => {
        mainWindow.show();
    });

    /* Open the DevTools. */
    mainWindow.webContents.openDevTools();

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