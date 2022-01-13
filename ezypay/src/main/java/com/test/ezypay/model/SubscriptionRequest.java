package com.test.ezypay.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="subscription")
public class SubscriptionRequest {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="idNumber")
    private Long idNumber;

    @Column(name="subType")
    private String subType;

    @Column(name="subPrice")
    private BigDecimal subPrice;

    @Column(name="startDate")
    private String startDate;

    @Column(name="endDate")
    private String endDate;

    private String subInput;

    public Long getIdNumber() {return idNumber;}

    public void setIdNumber(Long idNumber) {this.idNumber = idNumber;}

    public String getSubType() {return subType;}

    public void setSubType(String subType) {this.subType = subType;}

    public BigDecimal getSubPrice() {return subPrice;}

    public void setSubPrice(BigDecimal subPrice) {this.subPrice = subPrice;}

    public String getStartDate() {return startDate;}

    public void setStartDate(String startDate) {this.startDate = startDate;}

    public String getEndDate() {return endDate;}

    public void setEndDate(String endDate) {this.endDate = endDate;}

    public String getSubInput() {return subInput;}

    public void setSubInput(String subInput) {this.subInput = subInput;}
}
