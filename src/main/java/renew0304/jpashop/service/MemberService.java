package renew0304.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renew0304.jpashop.domain.Member;
import renew0304.jpashop.repository.MemberRepository;

import java.util.List;
/**
 * JPA에서 모든 데이터 변경, 비즈니스 로직은 Transactional 안에서 동작해야한다.
 * @Transactional(readOnly = true) -> 읽기 전용, 최적화에 유리하다. 단순 조회
 * 데이터 쓰는 경우에는 따로 @Transactional 처리했다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    /**
     1) 필드 Injection - 권장 X
        @Autowired
        private MemberRepository memberRepository;

     2) 생성자 Injection - 생성자 주입
        @Autowired -> 생성자가 하나만 있을 경우 생략 가능
        public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
        }

     3) lombok - RequiredArgsConstructor 사용 - final 필드만으로 생성자 자동 생성
     private final MemberRepository memberRepository;
    */

    private final MemberRepository memberRepository;
    // 1. 회원가입
    @Transactional
    public Long sign(Member member){
        validateDuplicateMember(member);// 중복 회원 검증
        memberRepository.save(member);// 저장
        // em.persist 하는 순간 영속성 컨텍스트에 객체를 올려서 PK 값이 항상 생성 되어있는 것을 보장한다. (항상 값이 들어있다)
        return member.getId();
    }
    // 2. 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    
    // 3. 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findMember(memberId);
    }

    // 4. 중복 회원 검증
    private void validateDuplicateMember(Member member){
        List<Member> result = memberRepository.findByName(member.getName());
        // EXCEPTION (존재하는 경우)
        if(!result.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    // 5. 회원 정보 수정
    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findMember(id);
        member.setName(name);
    }
}
