package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional//이걸 꼭 붙여줘야함
    //근데 이거 테스트에 있으면 db롤백함
    @Rollback(value = false) //롤백되기 싫으면 이거 붙여
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setName("memberA");
        Long saveId = memberRepository.save(member);
        //when
        Member findMember = memberRepository.find(saveId);
        //then

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember).isEqualTo(member);
//        findmember == member 같으니까 착각하지마
        //같은 트랜젝션 안에 있어서 같은 영속성 컨텍스트이니까 같음
        //1차 캐시에서 같다고 생각함

    }
}