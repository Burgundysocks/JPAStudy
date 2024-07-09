package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional //이게 있어야 함 무조건!
@Transactional(readOnly = true) //읽기가많으면 이렇게 세팅
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    //필드 인젝션은 테스트같은데서 수정을 못하는 단점이있음
    //그래서 생성자 인젝션 만들고 마지막에 final!

    //세터 인젝션  가짜주입가능
    //단점은 누군가가 저걸 바꿀 수 있어..
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//그래서 이렇게 생성자 인젝션을 씀
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    //근데 귀찮으면 롬복으로 @RequiredArgsConstructor 하면 final붙은거 인젝션 해줌

    /**
     * 회원가입
     */
    @Transactional //얘는 기본이 false여서 따로 달아줘도 됨 우선순위 있음
    public long join(Member member) {
        validateDuplicateMember(member); //중복회원 검증로직
        memberRepository.save(member);
        return member.getId();
    }

    //중복회원 검증 비즈니스 로직
    private void validateDuplicateMember(Member member) {
        //Exeption
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //전체조회
//    @Transactional(readOnly = true) //읽기에는 readonly true넣기
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    //아이디로 한명 조회
//    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }


    //변경감지
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get();;
        member.setName(name);
    }
}
