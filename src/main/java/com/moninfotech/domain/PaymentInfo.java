package com.moninfotech.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Date;

@Entity
public class PaymentInfo extends BaseEntity {
    private String status;
    private Date tran_date;
    private String tran_id;
    private String val_id;
    private Double amount;
    private Double store_amount;
    private String card_type;
    private String card_no;
    private String currency;
    private String bank_tran_id;
    private String card_issuer;
    private String card_brand;
    private String card_issuer_country;
    private String card_issuer_country_code;
    private String currency_type;
    private String value_a;
    private String value_b;
    private String value_c;
    private String value_d;
    private String varify_sign;
    @Column(length = 500)
    private String verify_key;
    private byte risk_level;
    private String risk_title;

    @OneToOne
    private Transaction transaction;


    public static class Status {
        private Status() {
        }

        public static String VALID = "VALID";
        public static String FAILED = "FAILED";
        public static String CANCELLED = "CANCELLED";
    }

    public boolean isValid() {
        System.out.println(this);
        if (!Status.VALID.equals(this.status))
            return false;

        String valString = this.tran_id + "SAyEM" + this.getAmount().intValue();
        if (this.getValue_a() == null || !this.getValue_a().equals(valString))
            return false;
        // VALIDATION API VALIDATION

        return true;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTran_date() {
        return tran_date;
    }

    public void setTran_date(Date tran_date) {
        this.tran_date = tran_date;
    }

    public String getTran_id() {
        return tran_id;
    }

    public void setTran_id(String tran_id) {
        this.tran_id = tran_id;
    }

    public String getVal_id() {
        return val_id;
    }

    public void setVal_id(String val_id) {
        this.val_id = val_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getStore_amount() {
        return store_amount;
    }

    public void setStore_amount(Double store_amount) {
        this.store_amount = store_amount;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBank_tran_id() {
        return bank_tran_id;
    }

    public void setBank_tran_id(String bank_tran_id) {
        this.bank_tran_id = bank_tran_id;
    }

    public String getCard_issuer() {
        return card_issuer;
    }

    public void setCard_issuer(String card_issuer) {
        this.card_issuer = card_issuer;
    }

    public String getCard_brand() {
        return card_brand;
    }

    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }

    public String getCard_issuer_country() {
        return card_issuer_country;
    }

    public void setCard_issuer_country(String card_issuer_country) {
        this.card_issuer_country = card_issuer_country;
    }

    public String getCard_issuer_country_code() {
        return card_issuer_country_code;
    }

    public void setCard_issuer_country_code(String card_issuer_country_code) {
        this.card_issuer_country_code = card_issuer_country_code;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getValue_a() {
        return value_a;
    }

    public void setValue_a(String value_a) {
        this.value_a = value_a;
    }

    public String getValue_b() {
        return value_b;
    }

    public void setValue_b(String value_b) {
        this.value_b = value_b;
    }

    public String getValue_c() {
        return value_c;
    }

    public void setValue_c(String value_c) {
        this.value_c = value_c;
    }

    public String getValue_d() {
        return value_d;
    }

    public void setValue_d(String value_d) {
        this.value_d = value_d;
    }

    public String getVarify_sign() {
        return varify_sign;
    }

    public void setVarify_sign(String varify_sign) {
        this.varify_sign = varify_sign;
    }

    public String getVerify_key() {
        return verify_key;
    }

    public void setVerify_key(String verify_key) {
        this.verify_key = verify_key;
    }

    public byte getRisk_level() {
        return risk_level;
    }

    public void setRisk_level(byte risk_level) {
        this.risk_level = risk_level;
    }

    public String getRisk_title() {
        return risk_title;
    }

    public void setRisk_title(String risk_title) {
        this.risk_title = risk_title;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "status='" + status + '\'' +
                ", tran_date=" + tran_date +
                ", tran_id='" + tran_id + '\'' +
                ", val_id='" + val_id + '\'' +
                ", amount=" + amount +
                ", store_amount=" + store_amount +
                ", card_type='" + card_type + '\'' +
                ", card_no='" + card_no + '\'' +
                ", currency='" + currency + '\'' +
                ", bank_tran_id='" + bank_tran_id + '\'' +
                ", card_issuer='" + card_issuer + '\'' +
                ", card_brand='" + card_brand + '\'' +
                ", card_issuer_country='" + card_issuer_country + '\'' +
                ", card_issuer_country_code='" + card_issuer_country_code + '\'' +
                ", currency_type='" + currency_type + '\'' +
                ", value_a='" + value_a + '\'' +
                ", value_b='" + value_b + '\'' +
                ", value_c='" + value_c + '\'' +
                ", value_d='" + value_d + '\'' +
                ", varify_sign='" + varify_sign + '\'' +
                ", verify_key='" + verify_key + '\'' +
                ", risk_level=" + risk_level +
                ", risk_title='" + risk_title + '\'' +
                "} " + super.toString();
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
