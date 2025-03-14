package org.swe.cart.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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
@Table(name="user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@Column(nullable=false,unique=true)
	private String username;

	@Column(nullable=false,unique=true)
	private String email;

	@Column(nullable=false)
	private String password; //Either store password as Base64 encoded String or byte[] since it will be encrypted

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<GroupMember> groupMemberships = new HashSet<>();

	//TODO Add two one-to-many debts (one creditor one debtor) and Transaction
	//TODO Add one-to-many listItem

	private Instant createdAt;
}
