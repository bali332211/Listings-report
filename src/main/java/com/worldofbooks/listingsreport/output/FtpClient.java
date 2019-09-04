package com.worldofbooks.listingsreport.output;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class FtpClient implements AutoCloseable {

    private String server;
    private int port;
    private String user;
    private String password;
    private FTPClient ftp;

    public FtpClient(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void sendToFtp(Path pathToSend, String ftpPath) throws IOException {
        if (ftp == null) {
            open();
        }

        try {
            ftp.sendNoOp();
        } catch (IOException e) {
            open();
        }

        File fileToUpload = new File(pathToSend.toUri());
        uploadToFtp(fileToUpload, ftpPath);
    }

    public void open() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(server, port);

        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new IOException("Can't connect to FTP Server");
        }
        ftp.login(user, password);
    }

    @Override
    public void close() throws IOException {
        if (ftp != null) {
            ftp.disconnect();
        }
    }

    private void uploadToFtp(File fileToUpload, String ftpPath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(fileToUpload)) {
            ftp.storeFile(ftpPath, fileInputStream);
        }
    }
}
