import java.util.ArrayList;
import java.io.Serializable;

public class ConnectedUsersResponse extends Data implements Serializable {
	static final long serialVersionUID = 1L;
	
	private ArrayList<String> users;
 	public ConnectedUsersResponse(ArrayList<String> users) {
		setUsers(users);
	}
 	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}
 	public ArrayList<String> getUsers() {
		return users;
	}
} 