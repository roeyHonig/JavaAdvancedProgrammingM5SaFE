import java.io.Serializable;

public class RequestWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestType;  // Can be "GET_MENU", "ORDER", etc.
    private Object payload;      // Can be an Order object or null

    public RequestWrapper(String requestType, Object payload) {
        this.requestType = requestType;
        this.payload = payload;
    }

    public String getRequestType() {
        return requestType;
    }

    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "RequestWrapper [requestType=" + requestType + ", payload=" + payload + "]";
    }
}

