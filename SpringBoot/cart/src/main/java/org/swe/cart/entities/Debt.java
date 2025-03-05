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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
