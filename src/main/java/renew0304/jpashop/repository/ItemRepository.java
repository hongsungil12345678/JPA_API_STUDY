package renew0304.jpashop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import renew0304.jpashop.domain.item.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    // 저장
    public void save(Item item){
        // 없을 경우
        if(item.getId()== null){
            em.persist(item); // 새로운 객체일 경우 id값이 null 이므로 em.persist 해준다.
        }else{
            em.merge(item); // 업데이트와 비슷
        }
    }

    // 조회
    // 1. 단건 조회
    public Item findOne(Long id){
        return em.find(Item.class,id);// [ 엔티티class명, PK]
    }
    // 2. 전체 조회
    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
