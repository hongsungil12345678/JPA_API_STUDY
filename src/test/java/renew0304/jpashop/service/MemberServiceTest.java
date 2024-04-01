package renew0304.jpashop.service;

import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import renew0304.jpashop.domain.Member;
import renew0304.jpashop.repository.MemberRepository;

import static org.junit.Assert.*;
// Test는 실제 springbootapplication 위에서 동작하므로 돌리고 테스트
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Test

    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("hong");

        //when
        Long savedId = memberService.sign(member);

        //then 저장된 회원과 찾아온 회원이 같은지 확인
        assertEquals(member,memberRepository.findMember(savedId));
    }

    /** @Test(expected = IllegalStateException.class) 로 대체
     * try{ memberService.sign(member2);}
     * catch(IllegalStateException e){
     * return;}
     */
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("hong");

        Member member2 = new Member();
        member2.setName("hong");
        //when
        memberService.sign(member1);
        memberService.sign(member2);

        //then
        fail("예외 발생");
    }

}