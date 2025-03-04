package org.swe.cart.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="groupMemeber")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id; //Temp
    //TODO Implement this
}
