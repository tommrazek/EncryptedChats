import java.io.Serializable;

public class ConnectionRequest extends Data implements Serializable
{
    public String username;
    static final long serialVersionUID = 1L;

    public ConnectionRequest(String username)
    {
        this.username = username;
    }
}