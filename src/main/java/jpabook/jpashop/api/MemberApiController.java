package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> members() {
        return memberService.findAll();
    }
    @GetMapping("api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findAll();
        List<MemberDTO> collect = new ArrayList<>();
        for (Member m : findMembers) {
            collect.add(new MemberDTO(m.getName()));
        }

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count; // 수 세기
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMember2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    //Put은 멱등하다 : 똑같은 수정을 여러번 해도 결과가 같음
    private updateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid updateMemberRequest request){

        memberService.update(id, request.getName());
        Member member = memberService.findById(id);
        return new updateMemberResponse(member.getId(), member.getName());
    }






    //별도의 DTO

    @Getter @Setter
    static class  updateMemberRequest{
        private String name;

    }

    @Getter @Setter
    @AllArgsConstructor
    static class  updateMemberResponse{
        private Long id;
        private String name;
    }


    @Getter @Setter
    static class CreateMemberRequest {
        private String name;
    }

    @Getter @Setter
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
