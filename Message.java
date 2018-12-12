import java.io.Serializable;

public class Message extends Data implements Serializable
{
    private final String message;
    private final String from;
    private final String to;
    static final long serialVersionUID = 1L;

    public Message(String to, String from, String message)
    {
        this.message = message;
        this.from = from;
        this.to = to;
    }

    public String getSender()
    {
        return from;
    }

    public String getReceiver()
    {
        return to;
    }

    public String getMessage()
    {
        return message;
    }

    public String toString()
    {
        return "from: " + from + "; to " + to + "; message: " + message;
    }
}