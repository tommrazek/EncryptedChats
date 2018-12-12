import java.io.Serializable;

//This is a class which can be used for requesting a conversation
//for group chats
public class OpenConversation extends Data implements Serializable
{
    String user;

	public OpenConversation(String user)
	{
		this.user = user;
	}
}