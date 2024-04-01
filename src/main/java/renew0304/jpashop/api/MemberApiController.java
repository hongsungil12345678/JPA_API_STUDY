package renew0304.jpashop.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import renew0304.jpashop.domain.Member;
import renew0304.jpashop.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /**
     *  등록 v1 : 요청 값으로 Member 엔티티를 직접 받아서 구현,RequestBody에 엔티티 매핑
     *  엔티티에 추가 로직이 들어간다는 한계점
    */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.sign(member);
        return new CreateMemberResponse(id);
    }
    @Data
    static class CreateMemberRequest{
        private String name;
    }
    @Data
    static class CreateMemberResponse{
        private Long id;
        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }

    /**
     * 등록 V2 : 요청 값으로 Member 엔티티를 DTO를 통해서 RequestBody에 매피
     */
     @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
         Member member = new Member();
         member.setName(request.getName());//DTO

         Long id = memberService.sign(member);
         return new CreateMemberResponse(id);
     }

     /**
      *  수정 API
      */
     @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id")Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){
         memberService.update(id,request.getName());// name-> request
         Member findMember = memberService.findOne(id);
         return new UpdateMemberResponse(findMember.getId(), findMember.getName());

     }
     @Data
     static class UpdateMemberRequest{
         private String name;
     }@Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
         private Long id;
         private String name;
     }
     
     /**
      * 회원 조회 V1 : 응답 값으로 엔티티를 직접 외부에 노출
      * 추가 로직이 필요하다, 엔티티의 모든 값이 노출된다, 응답 스펙에 따른 로직 추가
      * 코드가 종속적임 비효율적이다
      */
     @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
         return memberService.findMembers();
     }

     /**
      * 회원 조회  V2 : 응답 값으로 엔티티가 아닌 별도의 DTO
      * */
     // Q : Result<T> 는 무슨 의미인지 모르겠음 추가로 확인
     @GetMapping("/api/v2/members")
    public Result findMembersV2(){
         List<Member> findMembers = memberService.findMembers();
         List<MemberDto> collect = findMembers.stream()
                 .map(m -> new MemberDto(m.getName()))
                 .collect(Collectors.toList());
         return new Result(collect);
     }
     @Data
    @AllArgsConstructor
    static class Result<T>{
         private T data;
     }
     @Data
    @AllArgsConstructor
    static class MemberDto{
         private String name;
     }

}
