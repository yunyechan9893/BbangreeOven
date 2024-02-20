package com.bbangle.bbangle.model;

import com.bbangle.bbangle.dto.MemberInfoRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "member")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birth")
    private String birth;

    @Column(name = "profile")
    private String profile;

    @Column(name = "is_deleted", columnDefinition = "tinyint")
    private boolean isDeleted;
  
    @Builder
    public Member(Long id, String email, String phone, String name, String nickname,
                  String birth, boolean isDeleted, String profile)  {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.isDeleted = isDeleted;
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
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

    public Member update(String nickname){
        this.nickname = nickname;
        return this;
    }

    public void updateInfo(MemberInfoRequest request) {
        this.birth = request.birthDate();
        this.nickname = request.nickname();
        this.phone = request.phone();
    }

}
