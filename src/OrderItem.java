public class OrderItem extends MenuItem {
    private int amount;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public OrderItem(int id, String type, String description, int price, int amount) {
        super(id, type, description, price);
        this.amount = amount;
    }

    public OrderItem(MenuItem menuItem, int amount) {
        super(menuItem.getId(), menuItem.getType(), menuItem.getDescription(), menuItem.getPrice());
        this.amount = amount;
    }
}
