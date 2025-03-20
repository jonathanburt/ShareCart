package org.swe.cart.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.swe.cart.embeddables.InviteUserKey;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="invite")
@Data
public class GroupInvite {

    @EmbeddedId
    private InviteUserKey id;
    
    @ManyToOne
    @MapsId("groupid")
    @JoinColumn(name = "group_id", nullable = false)
    @JsonBackReference
    private Group group;

    @ManyToOne
    @MapsId("userid")
    @JoinColumn(name="user_id", nullable = false)
    @JsonBackReference
    private User user;

    @CreationTimestamp
    @Column(name="created_at")
    private Instant created_at;
}
