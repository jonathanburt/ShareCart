package org.swe.cart.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name="debt")
public class Debt {
    @Id
    @JoinColumn(name="creditor_id", nullable=false)
    private User creditor;

    @Id
    @JoinColumn(name="debtor_id", nullable=false)
    private User debtor;

    @Column(name="amount", nullable=false)
    private Float amount;

    @Column(name="createdAt", nullable=false)
    private Instant createdAt;



    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public User getDebtor() {
        return debtor;
    }

    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
