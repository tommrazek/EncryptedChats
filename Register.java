import java.util.Scanner;
public class Register {

    public static void main(String[] args) {
        while(true){
        	System.out.println("Please enter a username:");
        	Scanner scanner = new Scanner(System.in);
        	String username_new = scanner.nextLine();
        	if (username_new.length() != 0 && !username_new.contains(" ")){
        		System.out.println("Welcome!");
                break;
        	}
     /*     else if (username exists){
                System.out.println("This username already exists. Please pick a unique username");
          } */ 
        	else{
        		System.out.println("Please enter a valid username. Usernames cannot contain spaces");
        	}
        }
    }
}