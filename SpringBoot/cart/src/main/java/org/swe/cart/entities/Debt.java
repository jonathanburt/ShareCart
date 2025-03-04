package org.swe.cart.entities;

import java.time.Instant;

import org.swe.cart.embeddables.DebtTransactKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;


@Entity
@Table(name="debt")
public class Debt {

    @EmbeddedId
    private DebtTransactKey id;

    @ManyToOne
    @MapsId("creditorid")
    @JoinColumn(name="creditor_id", nullable=false)
    private User creditor;

    @ManyToOne
    @MapsId("debtorid")
    @JoinColumn(name="debtor_id", nullable=false)
    private User debtor;

    @Column(name="amount", nullable=false)
    private Float amount;

    @Column(name="createdAt", nullable=false)
    private Instant createdAt;

    public DebtTransactKey getId() {
        return id;
    }

    public void setId(DebtTransactKey id) {
        this.id = id;
    }

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
