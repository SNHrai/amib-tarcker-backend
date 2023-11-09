package com.tracker_application.tracker.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMP_MASTER")
public class User implements UserDetails {

    @Id
    @Column(name = "EMP_ID", length = 55, unique = true, nullable = false)
    private String id;

    @Column(name = "EMP_NAME")
    private String empName;

    @Column(name = "EMP_NUMBER")
    private String mobileNumber;

    // @Column(name = "VERTICAL_HEAD_ID")
    // private int verticalId;

    @Column(name = "EMP_PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<TaskTracker> trackers;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Role> userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = userRoles.stream()
                .map(x -> new SimpleGrantedAuthority(x.getRole()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.empName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
