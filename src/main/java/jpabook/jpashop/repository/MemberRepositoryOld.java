package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryOld {


    private final EntityManager em;
    //스프링이 엔티티매니저를 만들어 주입

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    //sql과 유사, 기능적으론 동일 sql은 테이블을 대상으로 쿼리를 하나 jpql은 엔티티르 대상으로 함

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        //select m from Member m where m.name = :name 에서 :name은 파라미터
    }




}
