package api.dto;

public class CheckoutRequest {
    private String paymentMethod; // VD: "CASH", "WALLET", "BANK"

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
