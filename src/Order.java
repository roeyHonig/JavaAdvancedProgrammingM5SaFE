import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Integer, Integer> menuItems; // Menu item ID and quantity
    private String name;
    private String phoneNumber;
    private String address;

    public Order(Map<Integer, Integer> menuItems, String name, String phoneNumber, String address) {
        this.menuItems = menuItems;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Map<Integer, Integer> getMenuItems() {
        return menuItems;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
    // Helper method to print the order in a human-readable format
    public String toOrderString(ArrayList<MenuItem> menu) {
        StringBuilder sb = new StringBuilder();

        // Order summary: Name, phone, address
        sb.append("Order by: ").append(name).append("\n");
        sb.append("Phone: ").append(phoneNumber).append("\n");
        sb.append("Address: ").append(address).append("\n");

        // Items ordered
        double totalCost = 0;
        sb.append("----- Ordered Items -----\n");

        // Iterate through the order items (menuItemId -> quantity)
        for (Map.Entry<Integer, Integer> entry : menuItems.entrySet()) {
            int menuItemId = entry.getKey(); // Get the menu item ID
            int quantity = entry.getValue(); // Get the quantity ordered

            // Find the matching MenuItem by ID
            MenuItem item = null;
            for (MenuItem menuItem : menu) {
                if (menuItem.getId() == menuItemId) {
                    item = menuItem;
                    break;  // Assuming each ID is unique, break when found
                }
            }

            // If we find the menu item, print it out
            if (item != null) {
                double cost = item.getPrice() * quantity;
                sb.append(item.getDescription())
                        .append(" (")
                        .append(item.getType())
                        .append(") - X ")
                        .append(quantity)
                        .append(" | Cost: ")
                        .append(cost)
                        .append("\n");

                totalCost += cost;  // Add the cost to the total
            }
        }

        sb.append("--------------------------\n");
        sb.append("Total: ").append(totalCost).append("\n");

        return sb.toString();
    }
}
