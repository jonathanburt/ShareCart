package org.swe.cart.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="group")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @CreationTimestamp
    private Instant createdAt;

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

}
