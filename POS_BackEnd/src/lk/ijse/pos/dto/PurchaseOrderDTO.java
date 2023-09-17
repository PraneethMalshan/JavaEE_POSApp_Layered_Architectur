package lk.ijse.pos.dto;

public class PurchaseOrderDTO {
    private String orderID;
    private String itemCode;
    private int orderQty;
    private double price;
    private double buyQty;
    public double getBuyQty() {
        return buyQty;
    }

    public void setBuyQty(double buyQty) {
        this.buyQty = buyQty;
    }



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

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PurchaseOrderDTO(String orderID, String itemCode, int orderQty, double price, double buyQty) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.orderQty = orderQty;
        this.price = price;
        this.buyQty = buyQty;
    }

    public PurchaseOrderDTO() {
    }
}
