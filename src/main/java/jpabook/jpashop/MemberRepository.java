package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;
    //DAO와 비슷한 개념이라고 보면 됨

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
        //멤버를 반환하지 않는이유?
        //커멘드랑 쿼리를 분리해라!!!
        //저장하고나면 리턴값을 거의 만들지않음
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }


}
