import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextField;

public class RestaurantAppController {

    // Boolean property to control button visibility
    private BooleanProperty canPlaceOrder = new SimpleBooleanProperty(false);
    // StringProperties for two-way binding
    private StringProperty name = new SimpleStringProperty("John Doe");
    private StringProperty phoneNumber = new SimpleStringProperty("123-456-7890");
    private StringProperty address = new SimpleStringProperty("123 Main St");
    private ArrayList<MenuItem> menu;
    private ArrayList<OrderItem> menuViewModel = new ArrayList<>();

    @FXML
    private Button placeOrderButton;
    @FXML
    private VBox orderDialog;  // The dialog box that contains the menu
    @FXML
    private VBox menuList;  // VBox to display dynamic menu items
    // FXML injected TextFields
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField addressField;
    @FXML
    private Button cancelButton;  // Cancel button in the dialog
    @FXML
    private Button orderButton;   // Order button in the dialog

    // This method is used to bind the button visibility to canPlaceOrder
    public void initialize() {
        // Binding
        placeOrderButton.visibleProperty().bind(canPlaceOrder);
        nameField.textProperty().bindBidirectional(name);
        phoneNumberField.textProperty().bindBidirectional(phoneNumber);
        addressField.textProperty().bindBidirectional(address);

        menu = getMenu();
        if (menu != null) {
            canPlaceOrder.setValue(true);
            printMenu();
        }
    }

    // Creates a view for each OrderItem (a Horizontal Box)
    private HBox createMenuItemView(OrderItem orderItem) {
        HBox itemView = new HBox(10);

        // Item Name
        Label nameLabel = new Label(orderItem.getDescription());

        // Item Price
        Label priceLabel = new Label("$" + orderItem.getPrice());

        // Buttons and Amount Control
        Button minusButton = new Button("-");
        Button plusButton = new Button("+");
        Label amountLabel = new Label(String.valueOf(orderItem.getAmount()));

        // Event Handlers for + and -
        minusButton.setOnAction(event -> updateAmount(orderItem, amountLabel, -1));
        plusButton.setOnAction(event -> updateAmount(orderItem, amountLabel, 1));

        // Add components to the HBox
        itemView.getChildren().addAll(nameLabel, priceLabel, minusButton, amountLabel, plusButton);
        return itemView;
    }

    // Updates the amount of an OrderItem and the UI
    private void updateAmount(OrderItem orderItem, Label amountLabel, int delta) {
        int newAmount = orderItem.getAmount() + delta;
        if (newAmount >= 0) {  // Prevent negative amounts
            orderItem.setAmount(newAmount);
            amountLabel.setText(String.valueOf(newAmount));
        }
    }

    private void resetMenuViewModel() {
        menuViewModel = new ArrayList<>();;
        for (MenuItem item: menu
             ) {
            menuViewModel.add(new OrderItem(item, 0));
        }
    }

    // Handle the "Place an Order" button click event
    @FXML
    private void handlePlaceOrder() {
        System.out.println("Placing order...");
        resetMenuViewModel();
        menuList.getChildren().clear();
        // Add dynamic UI for each menu item
        for (OrderItem item : menuViewModel) {
            menuList.getChildren().add(createMenuItemView(item));
        }
        orderDialog.setVisible(true);
    }

    // Method to close the dialog when the Cancel button is clicked
    @FXML
    private void handleCancel() {
        System.out.println("Cancelling order...");
        // Hide the menu dialog box
        orderDialog.setVisible(false);
    }

    // Method to handle placing the order (for now it just prints a message)
    @FXML
    private void handleOrder() {
        System.out.println("Placing order...");
        // Hide the menu dialog box after order is placed
        orderDialog.setVisible(false);
        // Create an Order object with the menuItems and customer info
        Map<Integer, Integer> orderItems = convertMenuViewModelToMap();
        Order order = new Order(orderItems, name.get(), phoneNumber.get(), address.get());
        placeOrder(order);
    }

    private static void placeOrder(Order order) {
        try (
                Socket socket = new Socket("localhost", 3333);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {


            // Send the Order object directly to the server
            RequestWrapper requestWrapper = new RequestWrapper("ORDER", order);
            out.writeObject(requestWrapper);
            out.flush();

            // Receive confirmation from the server (Response object)
            Response response = (Response) in.readObject();
            System.out.println("Order Confirmation:");
            System.out.println(response.toString(true)); // Include date & time in the confirmation

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<MenuItem> getMenu() {
        ArrayList<MenuItem> myMenu = null;
        try (
                Socket socket = new Socket("localhost", 3333);

                ObjectOutputStream out =
                        new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream in =
                        new ObjectInputStream(socket.getInputStream());
        ) {
            RequestWrapper requestWrapper = new RequestWrapper("GET_MENU", null);
            out.writeObject(requestWrapper);
            out.flush();

            myMenu = (ArrayList<MenuItem>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myMenu;
    }

    private void printMenu() {
        System.out.println("----- MENU -----");

        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    private Map<Integer, Integer> convertMenuViewModelToMap() {
        Map<Integer, Integer> itemMap = new HashMap<>();

        for (OrderItem orderItem : menuViewModel) {
            itemMap.put(orderItem.getId(), orderItem.getAmount());  // Use OrderItem's id as the key and amount as the value
        }

        return itemMap;
    }
}
