package renew0304.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import renew0304.jpashop.domain.Member;

import java.util.List;
/**
 * SpringBoot JPA 사용시 @PersistenceContext -> @Autowired 변경 가능
*/
@RequiredArgsConstructor
@Repository
public class MemberRepository {
    /**
     1. PersistenceContext
     @PersistenceContext
     private EntityManager em;
        2. lombok
    private final EntityManager em;
    */

    private final EntityManager em;
    // 1. 회원 저장
    public void save(Member member){
        em.persist(member);
    }
    // 2. 회원 조회(PK)
    public Member findMember(Long id){
        return em.find(Member.class,id);
    }
    // 3. 전체 조회 (JPQL로 작성)
    // SQL 과 JPQL 차이점 SQL -> 테이블 대상, JPQL -> Entity 객체 대상 조회
    public List<Member> findAll(){
        // [JPQL, 반환타입]
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }
    // 4. 이름 검색 (파라미터 바인딩)
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }

}
