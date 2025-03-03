package org.swe.cart.entities;

import java.time.Instant;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="group")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy="group")
    private Set<List> lists;

    @OneToMany(mappedBy="group")
    private Set<Item> items;

    @OneToMany(mappedBy="group")
    private Set<GroupMember> groupMembers;

    @CreationTimestamp
    private Instant createdAt;



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

    public Set<List> getLists() {
        return lists;
    }

    public void setLists(Set<List> lists) {
        this.lists = lists;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(Set<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

}
