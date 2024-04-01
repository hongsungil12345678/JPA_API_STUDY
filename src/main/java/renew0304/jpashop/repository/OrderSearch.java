package renew0304.jpashop.repository;

import lombok.Getter;
import lombok.Setter;
import renew0304.jpashop.domain.OrderStatus;

@Getter@Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;

}
