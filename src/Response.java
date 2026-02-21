import java.io.*;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderSummary;
    private String timestamp;

    public Response(String orderSummary) {
        this.orderSummary = orderSummary;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String toString(boolean includeTimestamp) {
        if (includeTimestamp) {
            return orderSummary + "\nDate: " + timestamp;
        } else {
            return orderSummary;
        }
    }
}
