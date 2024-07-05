package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false)//이렇게 하면 커밋해서 인터스문이 보임 없으면 인서트문 안보임
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("DB");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();//이렇게 하면 커밋없이 보임
        assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test()
    public void 중복확인() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        try{
            memberService.join(member2);
        }catch (IllegalStateException e){
            return;
        }
        //then
        fail("예외발생!");
    }

}