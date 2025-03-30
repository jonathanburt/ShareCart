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
@Table(name="transaction")
public class Transaction {

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)

    @ManyToOne
    @JoinColumn(name="payer_id", nullable = false)
    private User payer;

    @ManyToOne
    @JoinColumn(name="payee_id", nullable = false)
    private User payee;

    @Column(name="amount")
    private float amount;

    @Column(name="created_at")
    @CreationTimestamp
    private Instant created_at;

}