package com.moninfotech.domain;

import javax.persistence.Entity;

/**
 * Created by sayemkcn on 3/23/17.
 */
@Entity
public class Transaction extends BaseEntity {
    private boolean status;
    private int amount;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "status=" + status +
                ", amount=" + amount +
                "} " + super.toString();
    }
}
