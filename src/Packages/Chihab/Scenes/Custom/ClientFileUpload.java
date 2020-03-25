package Packages.Chihab.Scenes.Custom;

import SharedResources.URLServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientFileUpload {
    private ClientFileUpload instance;

    private ClientFileUpload() {

    }

    void sendFile(File f) throws IOException {
        Socket clientSocket = new Socket(URLServer.ftpServerLink, 6969);
        FileInputStream fileStream = new FileInputStream(f.getAbsoluteFile());
        OutputStream output = clientSocket.getOutputStream();
        output.write(fileStream.readAllBytes());
    }

    public ClientFileUpload getInstance() {
        if (instance == null)
            return new ClientFileUpload();
        return instance;
    }
}
