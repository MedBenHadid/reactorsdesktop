package SharedResources.Utils.FTPInterface;

import SharedResources.URLServer;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FTPInterface extends FTPClient{
    private static FTPInterface instance;
    private FTPInterface (){
        super();
    }
    private final Logger logger = Logger.getLogger(FTPInterface.class.getName());

    public static FTPInterface getInstance(){
        return Objects.requireNonNullElseGet(instance, () -> instance = new FTPInterface());
    }

    public File downloadFile(String filePath, int fileType) throws IOException {
        connect();
        this.setFileType(fileType);
        int i = filePath.lastIndexOf('.');
        if (i > Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'))) {
            InputStream inputStream = this.retrieveFileStream(filePath);
            String extension = filePath.substring(i + 1);
            File temp = File.createTempFile(UUID.randomUUID().toString(), "." + extension);
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            temp.deleteOnExit();
            closeConnection();
            return temp;
        }
        closeConnection();
        return null;
    }

    public boolean send(File file, String filePath)
    {
        if(!isConnected())
        {
            try {
                connect();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Couldnt connect to FTP Server");
                return false;
            }
        }
        try {
            byte[] data = Files.readAllBytes(file.toPath());
            setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean success =  storeFile(filePath +file.getName(), new ByteArrayInputStream(data));
            closeConnection();
            if(success) {
                logger.log(Level.INFO, "Successfully transmitted file: " + filePath + file.getName());
                return true;
            } else {
                logger.log(Level.WARNING, "Could not transmit file: " + filePath + file.getName() + " REASON :"+ getReplyString());
                return false;
            }
        }
        catch (IOException ex)
        {
            logger.log(Level.SEVERE, "Could not transmit file: " + filePath + file.getName(), ex);
            return false;
        }
    }

    public synchronized void fileUpload(File f, String destination) throws IOException {
        connect();
        try (InputStream input = new FileInputStream(f)) {
            this.storeFile(destination + f.getName(), input);
        }
        closeConnection();
    }
    public void closeConnection() throws IOException {
        if (this.isConnected()) {
            this.logout();
            this.disconnect();
        }
    }
    private void connect() throws IOException {
        this.connect(URLServer.ftpServerLink, URLServer.ftpSocketPort);
        this.login(URLServer.ftpUser, URLServer.ftpPassword);
        this.enterLocalActiveMode();
    }
}
