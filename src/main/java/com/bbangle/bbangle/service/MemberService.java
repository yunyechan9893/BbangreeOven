package com.bbangle.bbangle.service;

import java.util.Optional;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("findById() >>>>> no Member by Id"));
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("findByEmail() >>>> no Member by Email"));
    }

    public Member findByNickname(String nickname){
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(("findByNickname() >>>>> no Member by Nickname")));
    }

    public boolean checkingNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isEmpty();
    }

}
