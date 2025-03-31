package org.swe.cart.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.swe.cart.embeddables.GroupMemberKey;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name="groupMember")
public class GroupMember {

    @EmbeddedId
    private GroupMemberKey id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupRole role;

    @ManyToOne
    @MapsId("userid")
    @JoinColumn(name="user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @MapsId("groupid")
    @JoinColumn(name="group_id", nullable=false)
    @JsonBackReference
    private Group group;

    @Column(name="created_at")
    @CreationTimestamp
    private Instant created_at;

}
