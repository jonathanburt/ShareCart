package org.swe.cart.entities;

import java.time.Instant;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="`group`")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="name", unique = true)
    private String name;

    @OneToMany(mappedBy="group")
    private Set<List> lists;

    @OneToMany(mappedBy="group")
    private Set<Item> items;

    @OneToMany(mappedBy="group")
    @JsonManagedReference
    private Set<GroupMember> members;

    @CreationTimestamp
    @Column(name="created_at")
    private Instant createdAt;

}
