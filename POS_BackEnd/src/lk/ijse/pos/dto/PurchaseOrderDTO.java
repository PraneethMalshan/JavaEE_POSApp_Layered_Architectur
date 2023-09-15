package lk.ijse.pos.dto;

public class PurchaseOrderDTO {
    private String orderID;
    private String itemCode;
    private String orderQty;
    private double price;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PurchaseOrderDTO(String orderID, String itemCode, String orderQty, double price) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.orderQty = orderQty;
        this.price = price;
    }

    public PurchaseOrderDTO() {
    }
}
