package com.test.ezypay.model;

import java.math.BigDecimal;
import java.util.List;

public class SubscriptionResponse {

    private String subscriptionType;

    private BigDecimal subscriptionPrice;

    List<String> invoiceDate;

    public String getSubscriptionType() {return subscriptionType;}

    public void setSubscriptionType(String subscriptionType) {this.subscriptionType = subscriptionType;}

    public BigDecimal getSubscriptionPrice() {return subscriptionPrice;}

    public void setSubscriptionPrice(BigDecimal subscriptionPrice) {this.subscriptionPrice = subscriptionPrice;}

    public List<String> getInvoiceDate() {return invoiceDate;}

    public void setInvoiceDate(List<String> invoiceDate) {this.invoiceDate = invoiceDate;}

}
