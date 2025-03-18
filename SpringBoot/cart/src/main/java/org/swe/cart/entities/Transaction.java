package org.swe.cart.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Integer payer_id;
    private Integer payee_id;
    private float amount;

    @Column(name="created_at")
    @CreationTimestamp
    private Instant created_at;

}