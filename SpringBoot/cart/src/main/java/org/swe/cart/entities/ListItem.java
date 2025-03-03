package org.swe.cart.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="listItem")
public class ListItem {
    //TODO Implement this

    @Id
    @ManyToOne
    @JoinColumn(name="item_id", nullable=false, unique=true)
    private Item item;

    @Id
    @ManyToOne
    @JoinColumn(name="list_id", nullable=false)
    private List list;

    @Id
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="communal", columnDefinition="boolean default false")
    private Boolean communal;

    @Column(name="quantity", columnDefinition="int default 1")
    private Integer quantity;

    @Column(name="createdAt")
    private Instant createdAt;

    public Item getItem(){
        return this.item;
    }

    public void setItem(Item item){
        this.item = item;
    }

    public List getList(){
        return this.list;
    }

    public void setList(List list){
        this.list = list;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Boolean getCommunal(){
        return this.communal;
    }

    public void setCommunal(Boolean communal){
        this.communal = communal;
    }

    public Integer getQuantity(){
        return this.quantity;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public Instant getCreatedAt(){
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }



}
