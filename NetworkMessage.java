import java.io.Serializable;

public class NetworkMessage extends Data implements Serializable
{
    private String type;
    private Data data;
    static final long serialVersionUID = 1L;

    public NetworkMessage(String type, Data data)
    {
        this.type = type;
        this.data = data;
    }

    public String getType()
    {
        return type;
    }

    public Data getData()
    {
        return data;
    }

    public void setType(String newType)
    {
        type = newType;
    }

    public void setData(Data newData)
    {
        data = newData;
    }
}