package org.swe.cart.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy="group", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ShopList> lists = new HashSet<>();

    @OneToMany(mappedBy="group", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Item> items = new HashSet<>();

    @OneToMany(mappedBy="group", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<GroupMember> members = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<GroupInvite> invites = new HashSet<>();

    @CreationTimestamp
    @Column(name="created_at")
    private Instant createdAt;

}
