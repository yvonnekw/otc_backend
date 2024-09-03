package com.otc.backend.dto;

public class PaymentDto {

    private Long paymentId;
    private String amount;
    private String paymentDate;
    private String fullNameOnPaymentCard;
    private String cardNumber;
    private String expiringDate;
    private String issueNumber;
    private String securityNumber;
    private String status;
    private Long invoiceId;
    private String username;

    public PaymentDto() {
    }

    public PaymentDto(String paymentDate, String fullNameOnPaymentCard, String cardNumber, String expiringDate, String issueNumber, String securityNumber, String status, Long invoiceId) {
        this.paymentDate = paymentDate;
        this.fullNameOnPaymentCard = fullNameOnPaymentCard;
        this.cardNumber = cardNumber;
        this.expiringDate = expiringDate;
        this.issueNumber = issueNumber;
        this.securityNumber = securityNumber;
        this.status = status;
        this.invoiceId = invoiceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getFullNameOnPaymentCard() {
        return fullNameOnPaymentCard;
    }

    public void setFullNameOnPaymentCard(String fullNameOnPaymentCard) {
        this.fullNameOnPaymentCard = fullNameOnPaymentCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(String expiringDate) {
        this.expiringDate = expiringDate;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentDto{" +
                "paymentId=" + paymentId +
                ", amount='" + amount + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", fullNameOnPaymentCard='" + fullNameOnPaymentCard + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expiringDate='" + expiringDate + '\'' +
                ", issueNumber='" + issueNumber + '\'' +
                ", securityNumber='" + securityNumber + '\'' +
                ", status='" + status + '\'' +
                ", invoiceId=" + invoiceId +
                ", username='" + username + '\'' +
                '}';
    }
}
