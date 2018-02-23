package com.acf.taxgames.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Item {

    private static final NumberFormat nf = new DecimalFormat("#.00");

    private Integer receiptNbr;
    private Integer qty;
    private String productType;
    private Boolean isImported;
    private String desc;
    private BigDecimal price;
    private BigDecimal salesTax = BigDecimal.valueOf(0.00).setScale(2);
    private BigDecimal importTax = BigDecimal.valueOf(0.00).setScale(2);

    // Default no args constructor
    public Item() {

    }

    public Item(String[] csvValues) {
        // ReceiptNbr,Qty,ProductType,Imported,Desc,Price
        receiptNbr = Integer.parseInt(csvValues[0]);
        qty = Integer.parseInt(csvValues[1]);
        productType = csvValues[2];
        isImported = new Boolean(csvValues[3]);
        desc = csvValues[4];
        price = new BigDecimal(csvValues[5]).setScale(2);

    }

    public Integer getReceiptNbr() {
        return receiptNbr;
    }

    public void setReceiptNbr(Integer receiptNbr) {
        this.receiptNbr = receiptNbr;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean getImported() {
        return isImported;
    }

    public void setImported(Boolean imported) {
        isImported = imported;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }

    public BigDecimal getImportTax() {
        return importTax;
    }

    public void setImportTax(BigDecimal importTax) {
        this.importTax = importTax;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Double getTotalTax() {
        return salesTax.doubleValue() + importTax.doubleValue();
    }

    public Double getExtendedPrice() {
        return salesTax.doubleValue() + importTax.doubleValue() + price.doubleValue();
    }

    @Override
    public String toString() {
        return "Item{" +
                "receiptNbr=" + receiptNbr +
                ", qty=" + qty +
                ", productType=" + productType +
                ", isImported=" + isImported +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                ", salesTax=" + salesTax +
                ", importTax=" + importTax +
                ", totalTax=" + nf.format(getTotalTax()) +
                ", extendedPrice: " + nf.format(getExtendedPrice()) +
                '}';
    }
}
