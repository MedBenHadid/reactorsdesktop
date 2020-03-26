package SharedResources.Utils.FTPInterface;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FTPInterface {
    private static FTPInterface instance;
    private FTPClient ftpClient;

    private FTPInterface(String serverUrl, int serverPort, String user, String password) throws IOException {
        this.ftpClient = new FTPClient();
        ftpClient.connect(serverUrl, serverPort);
        ftpClient.login(user, password);
        ftpClient.enterLocalPassiveMode();
    }

    public static synchronized FTPInterface getInstance(String serverUrl, int serverPort, String user, String password) throws IOException {
        if (instance == null)
            return new FTPInterface(serverUrl, serverPort, user, password);
        else
            return instance;
    }

    // FileType is generally org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE
    public File downloadFile(String filePath, int fileType) throws IOException {
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
        return temp;
        // file.closewithend walla dra chnya please

    }

    public void fileUpload(File f, String destination) throws IOException {
        FileInputStream inputStream = new FileInputStream(destination + f.getName());
        OutputStream outputStream = ftpClient.storeFileStream(String.valueOf(f));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = inputStream.read(bytesIn)) != -1) {
            outputStream.write(bytesIn, 0, read);
        }
        inputStream.close();
        outputStream.close();
        ftpClient.completePendingCommand();

    }

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
}
