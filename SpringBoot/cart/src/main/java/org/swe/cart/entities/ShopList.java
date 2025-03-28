package org.swe.cart.entities;

import java.time.Instant;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="list", uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "name"}))
public class ShopList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="list", cascade = CascadeType.ALL)
    Set<ListItem> items;

    @ManyToOne
    @JoinColumn(name="group_id",nullable=false)
    @JsonBackReference
    private Group group;

    @Column(name="created_at")
    @CreationTimestamp
    private Instant createdAt;   
}
