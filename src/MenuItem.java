import java.io.Serializable;

public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String type;
    private String description;
    private int price;

    public MenuItem(int id, String type, String description, int price) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.price = price;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Type: " + type +
                " | Description: " + description +
                " | Price: " + price;
    }
}

