package Packages.Chihab.Scenes.Custom;

import SharedResources.URLServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerFileUpload {
    private ServerFileUpload instance;

    private ServerFileUpload() {

    }

    void sendFile(File f) throws IOException {
        Socket sr = new Socket(URLServer.url, 6969);
        InputStream ios = sr.getInputStream();
        File fp = new File(String.valueOf(ios.read()));
        FileInputStream fr = new FileInputStream(URLServer.userImageDir + fp.getName());
        fr.readAllBytes();
    }

    public ServerFileUpload getInstance() {
        if (instance == null)
            return new ServerFileUpload();
        return instance;
    }
}
