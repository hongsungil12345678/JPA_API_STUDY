package renew0304.jpashop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import renew0304.jpashop.domain.Address;
import renew0304.jpashop.domain.Member;
import renew0304.jpashop.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }
    // @Valid 이후 BindingResult가 있으면 -> 에러 발생 시 팅기지 않고 에러를 실어서 표현한다.
    /* 참고 name 필드 에러 메세지를 뽑아서 출력해준다. (MemberForm.class 에서 @NotEmpty(message = "회원 이름은 필수 입니다.")
        <div class="form-group">
      <label th:for="name">이름</label>
      <input type="text" th:field="*{name}" class="form-control"
             placeholder="이름을 입력하세요"
             th:class="${#fields.hasErrors('name')}? 'form-controlfieldError' : 'form-control'">
      <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
    </div>
    */
    @PostMapping("/members/new")
    public String createMemberForm(@Valid MemberForm memberForm, BindingResult result){
        // Valid 오류 시
        if(result.hasErrors()){
            return "members/createMemberForm"; //에러 발생 시 createMemberForm으로 이동(에러 출력)
        }
        // 주소 생성
        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        //객체 생성
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);
        // 저장
        memberService.sign(member);
        return "redirect:/";
    }
    @GetMapping("/members")
    public String memberList(Model model){
        List<Member> members = memberService.findMembers();
        // memberList.html에서 for문으로 출력
        /* 참고
       <tbody>
        <tr th:each="member : ${members}">
            <td th:text="${member.id}"></td>
            <td th:text="${member.name}"></td>
            <td th:text="${member.address?.city}"></td> //타임리프에서 ?를 사용하면 null 을 무시한다.
            <td th:text="${member.address?.street}"></td>
            <td th:text="${member.address?.zipcode}"></td>
        </tr>
      </tbody>
      */
        model.addAttribute("members",members);// [key:"members", value: members]
        return "members/memberList";
    }

}
