package by.bsuir.FtpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.net.ftp.FTPClient;

// https://en.wikipedia.org/wiki/List_of_FTP_server_return_codes

public class FtpClient {
  
  private static void showServerReply(FTPClient ftpClient) {

    String[] replies = ftpClient.getReplyStrings();
    if (replies != null && replies.length > 0) {
      for (String aReply : replies) {
        System.out.println("SERVER: " + aReply);
      }
    }
  }

  public static void main(String[] args) {

    FTPClient ftp = new FTPClient();
//    String username = "Valentine";
//    String password = "40468";
    String username;
    String password;
    String server  = "0.0.0.0";
    String rootDir = "/Users/valentinaprotasena/";
    int port = 21;

    try {

        ftp.connect(server, port);
        showServerReply(ftp);    // 220 Service ready for new user.

        // Authentication
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

        System.out.print("Username: ");
        username = reader.readLine();
        System.out.print("Password: ");
        password = reader.readLine();

        // Login
        boolean success = ftp.login(username, password);
        showServerReply(ftp);   // 230 User logged in, proceed. Logged out if appropriate.

        if (!success) {
          System.out.println("Could not login to the server.");
          return;
        }

        System.out.print("New directory name: ");
        String newDirName = reader.readLine();

        // Changes working director
        success = ftp.changeWorkingDirectory(rootDir + newDirName);
        showServerReply(ftp);

        if (success) {
          System.out.println("Successfully changed working directory.");
        } else {
          System.out.println("Failed to change working directory. See server's reply.");
        }

        // Print working directory
        String path = ftp.printWorkingDirectory();
        showServerReply(ftp);   // 257 "PATHNAME" created.

        //  Renaming directory
        System.out.print("Directory to rename: ");
        String oldDir = rootDir + newDirName+ reader.readLine();
        System.out.print("New name: ");
        String newDir = rootDir + newDirName + reader.readLine();

        success = ftp.rename(oldDir, newDir);
        if (success) {
          System.out.println(oldDir + " was successfully renamed to: "
              + newDir);
        } else {
          System.out.println("Failed to rename: " + oldDir);
        }

        // Renaming file name
  //      String oldFile = newDir+"/exmaple_file";
  //      String newFile = newDir+"/new_file.rtf";
  //
  //      success = ftp.rename(oldFile, newFile);
  //      if (success) {
  //        System.out.println(oldFile + " was successfully renamed to: "
  //            + newFile);
  //      } else {
  //        System.out.println("Failed to rename: " + oldFile);
  //      }

        // Logs out
        ftp.logout();
        ftp.disconnect();
        showServerReply(ftp);   // 221 Service closing control connection.


    } catch (final IOException e){

      System.out.println("Oops! Something wrong happened");
      e.printStackTrace();
      System.exit(1);
    }

  }
}
