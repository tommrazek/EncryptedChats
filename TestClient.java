import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.ObjectInputStream;

public class TestClient {

  private static Socket socket;
  static ObjectOutputStream out;

  public static void main(String[] args)
  {
    try {
      String host = "localhost";
      int port = 25000;
      InetAddress address = InetAddress.getByName(host);
      socket = new Socket(address, port);

      out = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

      listenerThread(input);

      NetworkMessage message = new NetworkMessage("ConnectionRequest", new ConnectionRequest("Ryan" + Math.random()));
      out.writeObject(message);

      NetworkMessage message2 = new NetworkMessage("ConnectedUsersRequest", new ConnectedUsersRequest());
      out.writeObject(message2);
      

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
                    System.out.println(cR.status);
                    break;
                  case "ConnectedUsersResponse":
                    Data data2 = nM.getData();
                    ConnectedUsersResponse cUR = (ConnectedUsersResponse) data2;
                    System.out.println((cUR.getUsers()));
                    System.out.println(cUR.getUsers().get(cUR.getUsers().size()-1));
                    NetworkMessage nM2 = new NetworkMessage("Message", new Message(cUR.getUsers().get(cUR.getUsers().size()-1), "Bob", "Hello World"));
                    out.writeObject(nM2);
                    out.flush();
                    break;
                  case "Message":
                    System.out.println("Handling Message");
                    Data data3 = nM.getData();
                    Message m = (Message) data3;
                    System.out.println(m.getMessage());
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
}