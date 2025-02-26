package org.swe.cart.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="transaction")
public class Transaction {

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)

    private Integer payer_id;
    private Integer payee_id;
    private float amount;
    private Instant created_at;

    public Integer getPayerId(){
        return this.payer_id;
    }

    public void setPayerId(Integer payer_id){
        this.payer_id = payer_id;
    }

    public Integer getPayeeId(){
        return this.payee_id;
    }

    public void setPayeeId(Integer payee_id){
        this.payee_id = payee_id;
    }

    public float getAmount(){
        return this.amount;
    }

    public void setAmount(float amount){
        this.amount = amount;
    }

    public Instant getCreatedAt(){
        return this.created_at;
    }

}