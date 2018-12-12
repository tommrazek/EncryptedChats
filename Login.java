import java.util.Scanner;
public class Login {

    public static boolean isloggedin(String username) {
            //Server response, true for valid login, false for existing username
            boolean server_response = true;
            if (username.length() != 0 && !username.contains(" ")){
                if(server_response){
                    System.out.println("Welcome!");
                    return true;
                }
                else{
                    System.out.println("This username already exists, please enter a unique username.");
                }
            }
            else{
                System.out.println("Please enter a valid username. Usernames cannot contain spaces");
            }         
            return false;
    }

    public static void main(String[] args) {
        isloggedin("Hello world");

    }
}