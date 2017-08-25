package com.moninfotech.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by sayemkcn on 3/23/17.
 */
@Entity
public class Transaction extends BaseEntity {
    private boolean success;
    private int amount;
    private boolean isDebit;

    @OneToOne(mappedBy = "transaction")
    private PaymentInfo paymentInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isDebit() {
        return isDebit;
    }

    public void setDebit(boolean debit) {
        isDebit = debit;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "success=" + success +
                ", amount=" + amount +
                ", isDebit=" + isDebit +
                ", paymentInfo=" + paymentInfo +
                "} " + super.toString();
    }
}
