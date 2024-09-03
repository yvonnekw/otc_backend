package com.otc.backend.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(name = "payment_id")
    private Long paymentId;
   // @Column(name = "amount")
    private String amount;
   // @Column(name = "payment_date")
    private String paymentDate;
    private String fullNameOnPaymentCard;
    private String cardNumber;
    private String expiringDate;
    private String issueNumber;
    private String securityNumber;
    private String status;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private Users user;

    public Payment() {
    }

    public Payment(Long paymentId, String amount, String paymentDate, String fullNameOnPaymentCard, String cardNumber,
            String expiringDate, String issueNumber, String securityNumber, String status, Invoice invoice) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.fullNameOnPaymentCard = fullNameOnPaymentCard;
        this.cardNumber = cardNumber;
        this.expiringDate = expiringDate;
        this.issueNumber = issueNumber;
        this.securityNumber = securityNumber;
        this.status = status;
        this.invoice = invoice;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount='" + amount + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", fullNameOnPaymentCard='" + fullNameOnPaymentCard + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expiringDate='" + expiringDate + '\'' +
                ", issueNumber='" + issueNumber + '\'' +
                ", securityNumber='" + securityNumber + '\'' +
                ", status='" + status + '\'' +
                ", invoice=" + invoice +
                ", user=" + user +
                '}';
    }
}
