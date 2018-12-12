import java.io.Serializable;

public class ConnectionResponse extends Data implements Serializable
{
    public String status;
    static final long serialVersionUID = 1L;

    public ConnectionResponse(String status)
    {
        this.status = status;
    }
}