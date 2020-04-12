package SharedResources.Utils.FTPInterface;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FTPInterface {
    // FileType is generally org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE

    private static FTPInterface instance;
    private FTPClient ftpClient;

    public void closeConnection() {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private int serverPort;
    private String serverUrl, user, password;

    private FTPInterface(String serverUrl, int serverPort, String user, String password) throws IOException {
        this.ftpClient = new FTPClient();
        this.serverUrl = serverUrl;
        this.serverPort = serverPort;
        this.user = user;
        this.password = password;
    }

    public static synchronized FTPInterface getInstance(String serverUrl, int serverPort, String user, String password) throws IOException {
        if (instance == null)
            return new FTPInterface(serverUrl, serverPort, user, password);
        else
            return instance;
    }

    public File downloadFile(String filePath, int fileType) throws IOException {
        connect();
        ftpClient.setFileType(fileType);
        InputStream inputStream = ftpClient.retrieveFileStream(filePath);
        String extension = "";
        int i = filePath.lastIndexOf('.');
        int p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (i > p) {
            extension = filePath.substring(i + 1);
        }
        File temp = File.createTempFile(UUID.randomUUID().toString(), "." + extension);
        Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        temp.deleteOnExit();
        closeConnection();
        return temp;
    }

    public void fileUpload(File f, String destination) throws IOException {
        connect();
        try (InputStream input = new FileInputStream(f)) {
            ftpClient.storeFile(destination + f.getName(), input);
        }
        closeConnection();
    }

    private void connect() throws IOException {
        ftpClient.connect(this.serverUrl, this.serverPort);
        ftpClient.login(this.user, this.password);
        ftpClient.enterLocalPassiveMode();
    }
}
