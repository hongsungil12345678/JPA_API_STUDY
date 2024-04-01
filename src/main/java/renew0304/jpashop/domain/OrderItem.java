package renew0304.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import renew0304.jpashop.domain.item.Item;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    private int orderPrice; //주문가격
    private int quantity; //주문 수량



    // == 생성 메서드 == //
    public static OrderItem createOrderItem(Item item,int orderPrice,int quantity){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setQuantity(quantity);

        item.removeStock(quantity);// 생성시 주문이 들어왔으므로 재고 줄여야함
        return orderItem;
    }


    // == 비즈니스 로직 == //

    // 1. 주문 취소
    public void cancel() {
        getItem().addStock(quantity);// 재고 수량 원복
    }
    // 2. 주문 가격 반환 ( 주문가격 * 수량 )
    public int getTotalPrice() {
        return getOrderPrice() * getQuantity();
    }
}
