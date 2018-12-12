import java.util.Scanner;
public class Interface {

	private static boolean isloggedin = false;

    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    		System.out.println("Type register/login");
    		while (true) {
    			if (!isloggedin) {
	    			String command = scanner.next();
	    			if (command.equals("login")) {	
	    				System.out.print("username: ");
		    			String username = scanner.next();
		    			login(username);
					} else if (command.equals("register")) {

					}
					else System.out.println("Type register/login");jk
				} else {
					//NetworkMessage message = new NetworkMessage();
					System.out.println("Enter command");
					System.out.println("1 - show all the connected users");
					System.out.println("2 - go to chat");
					String command = scanner.next();
						switch (command) {
							case "1" :
								System.out.print("All registered users:");
								//ConnectedUsersRequest cu = new ConnectedUsersRequest();

								//message.type();
								break;	
							case "2" : 
								System.out.print("User: ");
								String username = scanner.next();
								
								break;
							default :
								System.out.println("Please enter a valid command (1 or 2)");
								break;
				}
			}	
	}
}

    public static void login(String username) {
            //Server response, true for valid login, false for existing username
            if (username.length() != 0 && !username.contains(" ")){
            	boolean server_response = true;
            	//ConnectionRequest cr = new ConnectionRequest(username);
                if(server_response){
                    System.out.println("Welcome!");
                    isloggedin = true;
                }
                else{
                    System.out.println("This username already exists, please enter a unique username.");
                }
            }
            else{
                System.out.println("Please enter a valid username. Usernames cannot contain spaces");
            }         
    }
}