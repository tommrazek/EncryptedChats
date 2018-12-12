import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.util.*;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;



public class Client {

  private static Socket socket;
  private static ObjectOutputStream out;
  private static boolean isloggedin = false;
  private static String username;
  private static String status;
  private static String host = "localhost";
  public static void main(String[] args)
  {
    try {
      int port = 25000;
      InetAddress address = InetAddress.getByName(host);
      socket = new Socket(address, port);

     out = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

      listenerThread(input);
      user_interface();
      

      

      // String userInput = "";
      // while ((userInput = stdIn.readLine()) != null) {
      //   NetworkMessage message = new NetworkMessage("connect", new ConnectionRequest("Ryan"));
      //   out.writeObject(message);
      //   out.flush();
      // }

      // String userInput = "";
      // while ((userInput = in.readLine()) != null) {
      //     //out.println(userInput);
      //     System.out.println("echo: " + userInput);
      // }

    } catch (Exception exception) {exception.printStackTrace();}
    finally {
      try {
        //socket.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void listenerThread(ObjectInputStream input) 
  {
      Thread thread = new Thread(){

        @Override
        public void run() {
            try {
              Object responseMessage;
              while ((responseMessage = input.readObject()) != null) {
                  NetworkMessage nM = (NetworkMessage) responseMessage;
                  switch(nM.getType())
                  {
                    case "ConnectionResponse":
                      Data data = nM.getData();
                      ConnectionResponse cR = (ConnectionResponse) data;
                      String server_response = cR.status;
                      if(server_response.length() >= 0){
                          System.out.println("You are logged in, welcome!");
                          status = "Connected to Server, your key is: " + server_response;
                          System.out.println("[Status]: " + status);
                          isloggedin = true;
                      }
                      else{
                          System.out.println("This username already exists, please enter a unique username.");
                      }
                      break;
                    case "ConnectedUsersResponse":
                      Data data2 = nM.getData();
                      ConnectedUsersResponse cUR = (ConnectedUsersResponse) data2;
                      System.out.println((cUR.getUsers()));
                      //System.out.println(cUR.getUsers().get(cUR.getUsers().size()-1));
                      break;
                    case "Message":
                      Data data3 = nM.getData();
                      Message m = (Message) data3;
                      System.out.print("\033[H\033[2J");

                      String key = "Bar12345Bar12345";
                      Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

                      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

                      byte[] encrypted = Base64.getDecoder().decode(m.getMessage());
                      cipher.init(Cipher.DECRYPT_MODE, aesKey);
                      String decrypted = new String(cipher.doFinal(encrypted));

                      System.out.println("[Status]: Message received from user \u001B[32m*" + m.getSender() + "*\u001B[0m [ " + decrypted + " ]");
                      System.out.println("\u001B[34m\033[1mPick an option:\033[0m\u001B[0m");
                      System.out.println("[1] - Show all users connected to server");
                      System.out.println("[2] - Chat with a user");
                      break;
                  }
                }
            } catch (Exception e) {
              e.printStackTrace();
            }
        }
      };
      thread.start();
  }

  public static void user_interface() {
      Scanner scanner = new Scanner(System.in);
        System.out.println("Type register/login");
        while (true) {
          if (!isloggedin) {
            String command = scanner.next();
            if (command.equals("login")) {  
              System.out.print("username: ");
              username = scanner.next();
              login(username);
          } 

          else System.out.println("Type register/login");
        } 

          //NetworkMessage message = new NetworkMessage();
          System.out.print("\033[H\033[2J");
          System.out.println("[Status]: " + status);
          System.out.println("\u001B[34m\033[1mPick an option:\033[0m\u001B[0m");
          System.out.println("[1] - Show all users connected to server");
          System.out.println("[2] - Chat with a user");
          String command = scanner.next();
            switch (command) {
              case "1" :
                System.out.println("All connected users:");
                NetworkMessage message = new NetworkMessage("ConnectedUsersRequest", new ConnectedUsersRequest());
                try{
                  out.writeObject(message);
                }
                catch(Exception e){
                  e.printStackTrace();
                }
                  //ConnectedUsersRequest cu = new ConnectedUsersRequest();

                //message.type();
                break;  
              case "2" : 
                System.out.print("\033[H\033[2J");
                System.out.println("[Status]: " + status);
                System.out.println("\u001B[34m\033[1mSend a message:\033[0m\u001B[0m");
                System.out.print("[To]: ");
                String username2 = scanner.next();
                //NetworkMessage message2 = new NetworkMessage("OpenConversation", new OpenConversation(username));
                // try{
                //   out.writeObject(message2);
                // }
                // catch(Exception e){
                //   e.printStackTrace();
                // }
                System.out.print("[Message Text]: ");
                scanner.nextLine();
                String input = scanner.nextLine();
                if (!input.equals("\\quit")){
                  try {
                    //Here is where encryption kicks in
                    String key = "Bar12345Bar12345";
                    Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

                    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

                    cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                    byte[] encrypted = cipher.doFinal(input.getBytes());
                    String encrypted_string = new String(Base64.getEncoder().encodeToString(encrypted));

                    NetworkMessage nM2 = new NetworkMessage("Message", new Message(username2, username, encrypted_string));
                    out.writeObject(nM2);
                    out.flush();
                    status = "Message sent!";
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
                System.out.print("\033[H\033[2J");
                break;
              default :
                System.out.println("Please enter a valid command (1 or 2)");

                break;
        }    
  }
}

    public static void login(String username) {
            //Server response, true for valid login, false for existing username
            if (username.length() != 0 && !username.contains(" ")){
              NetworkMessage message = new NetworkMessage("ConnectionRequest", new ConnectionRequest(username));
              try{
                out.writeObject(message);
              }
              catch(Exception e){
                e.printStackTrace();
              }
              
              
              
            }
            else{
                System.out.println("Please enter a valid username. Usernames cannot contain spaces");
            }         
    }
}