package org.swe.cart.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)

    @ManyToOne
    @JoinColumn(name="creditor_id", nullable=false)
    private User creditor;

    @ManyToOne
    @JoinColumn(name="debtor_id", nullable=false)
    private User debtor;

    @Column(name="amount", nullable=false)
    private Float amount;

    @Column(name="createdAt", nullable=false)
    @CreationTimestamp
    private Instant createdAt;

}
