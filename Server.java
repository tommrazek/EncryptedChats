import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class Server {
        
    ConcurrentHashMap<String, ObjectOutputStream> connections = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> currentusers = new ConcurrentHashMap<String, Integer>();
	public static void main(String[] args)
    {
        Server server = new Server();
        System.out.print("\033[H\033[2J");
        System.out.println("\u001B[34m\033[1mWelcome to my Java Messaging Server:\033[0m\u001B[0m");
        try {
        	int port = 25000;
        	InetAddress address = InetAddress.getByName("localhost");
        	
            ServerSocket serverSocket = new ServerSocket(port, 0, address);
            
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("\u001B[36m\033[1mEstablished Connection to a User\033[0m\u001B[0m");
                Thread thread = new Thread(server.new MessageThread(socket));
                thread.start();
            }
        } catch (Exception e) {
        	System.out.println(e);
        }
        finally
        {
            try
            {
                //socket.close();
            }
            catch(Exception e){}
        }
    }

    protected class MessageThread implements Runnable 
	{
        Socket socket;
        ObjectOutputStream out;
        public MessageThread(Socket socket)
        {
            try {
                this.socket = socket;
                this.out = new ObjectOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		@Override
		public void run() 
		{
			boolean run = true; //Runs infinitely until interrupted
			while (run == true)
			{
				try 
				{
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                    Object message;
                    while ((message = input.readObject()) != null) {
						handleMessage((NetworkMessage) message);
                    }
				} catch (Exception e) 
				{
                    run = false;
				}
			}
		}

		public void handleMessage(NetworkMessage message) 
		{
            switch(message.getType()){
                case "ConnectionRequest":
                    handleConnectionRequest((ConnectionRequest) message.getData());
                    break;
                case "ConnectedUsersRequest":
                    handleConnectedUsersRequest((ConnectedUsersRequest) message.getData());
                    break;
                case "OpenConversation":
                    handleOpenConversation((OpenConversation) message.getData());
                    break;
                case "Message":
                    handleMessages((Message) message.getData());
                    break;
                default :
                    System.out.println(message.getType() + ": If you're seeing this you messed up");
            }

            //Check message type
                //if sign on then add to hashMap
                //if sign off then remove from hashMap

            // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // out.println("Here's your message back!" + message);
            // out.flush();
        }
        
        public void handleConnectionRequest(ConnectionRequest message)
        {
            NetworkMessage newMessage;
            try {
                if(!connections.containsKey(message.username)) {
                    connections.put(message.username, this.out);
                    int enc_key = ThreadLocalRandom.current().nextInt(10000, 100000);
                    currentusers.put(message.username, enc_key);
                    System.out.println(currentusers);
                    newMessage = new NetworkMessage("ConnectionResponse", new ConnectionResponse(Integer.toString(enc_key)));
                    out.writeObject(newMessage);
                    out.flush();
                }
                else {
                    newMessage = new NetworkMessage("ConnectionResponse", new ConnectionResponse(""));
                    out.writeObject(newMessage);
                    out.flush();
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void handleConnectedUsersRequest(ConnectedUsersRequest message) 
        {
            try {
                ArrayList<String> users = new ArrayList<String>(connections.keySet());
                NetworkMessage newMessage = new NetworkMessage("ConnectedUsersResponse", new ConnectedUsersResponse(users));
                out.writeObject(newMessage);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void handleOpenConversation(OpenConversation message) 
        {

        }

        public void handleMessages(Message message)
        {
            System.out.println("Handling Message");
            try {
                if(connections.containsKey(message.getReceiver()))
                {
                    ObjectOutputStream oos = connections.get(message.getReceiver());
                    oos.writeObject(new NetworkMessage("Message", message));
                    oos.flush();
                    System.out.println(message.toString());
                } else 
                {
                    System.out.println("User doesn't exist");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}
