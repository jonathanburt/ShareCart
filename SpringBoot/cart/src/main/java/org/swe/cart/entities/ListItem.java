package org.swe.cart.entities;

import java.time.Instant;

import org.swe.cart.embeddables.ListItemKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="listItem")
public class ListItem {

    @EmbeddedId
    private ListItemKey id;

    @ManyToOne
    @MapsId("itemid")
    @JoinColumn(name="item_id", nullable=false, unique=true) //We probably shouldnt have this field be unique, we should ensure uniqueness within a list using other logic
    private Item item;

    @ManyToOne
    @MapsId("listid")
    @JoinColumn(name="list_id", nullable=false)
    private List list;

    @ManyToOne
    @MapsId("userid")
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="communal", columnDefinition="boolean default false")
    private Boolean communal;

    @Column(name="quantity", columnDefinition="int default 1")
    private Integer quantity;

    @Column(name="createdAt")
    private Instant createdAt;

    public ListItemKey getId() {
        return id;
    }

    public void setId(ListItemKey id) {
        this.id = id;
    }

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
