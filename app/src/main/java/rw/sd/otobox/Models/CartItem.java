package rw.sd.otobox.Models;

/**
 * Created by miller on 9/11/17.
 */

public class CartItem {

    private Product product;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

}
