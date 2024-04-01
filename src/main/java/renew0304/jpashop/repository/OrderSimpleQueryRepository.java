package renew0304.jpashop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDto(){
        return em.createQuery(
                "select new renew0304.jpashop.repository.OrderSimpleQueryDto(o.id,m.name,o.orderDate,o.orderStatus,d.address)"+
                        " from Order o"+
                        " join o.member m"+
                        " join o.delivery d",OrderSimpleQueryDto.class)
                .getResultList();
    }
}
