package org.swe.cart.security;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.swe.cart.entities.User;

public class CustomUserDetails implements UserDetails {
    
    private final User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(user.getGroupMemberships().stream()
            .map(groupMember -> (GrantedAuthority) () -> 
                "ROLE_" + groupMember.getRole().name() + "_GROUP_" + groupMember.getGroup().getId())
            .collect(Collectors.toSet()));
        authorities.add((GrantedAuthority) () -> "ROLE_GLOBAL_" + user.getGlobalRole().name());

        return authorities;
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getUsername();
    }

    public Integer getId(){
        return user.getId();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public Instant getCreatedAt(){
        return user.getCreatedAt();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
