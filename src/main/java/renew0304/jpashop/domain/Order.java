package renew0304.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// order는 명령어로 잡혀있어서 테이블명을 orders로 설정
@Entity
@Getter@Setter
@NoArgsConstructor(access =AccessLevel.PROTECTED)
@Table(name="orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    // M : 1 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //fk이름이 member_id가 된다.
    private Member member; //주문회원

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 1 : 1 관계시 (FK)를 어디다 둬도 상관 없는데 주로 액세스 많이하는 곳에서 둔다.
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // ORDER,CANCEL


    // == 연관관계 메서드 == //
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 == //
    // 생성시 연관관계 설정 및 기타 정보 한번에
    public static Order createOrder(Member member,Delivery delivery,OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for(OrderItem o:orderItems){
            order.addOrderItem(o);
        }
        order.setOrderStatus(OrderStatus.ORDER); //초기 ORDER로 설정
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 == //
    
    // 1. 주문 취소
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료 된 상품입니다.");
        }
        // 주문 취소
        this.setOrderStatus(OrderStatus.CANCEL);
        // 주문 취소 되었으므로 재고 다시 추가
        for(OrderItem o : orderItems){// 각각 아이템에 대해서 cancel 처리
            o.cancel(); // 수량 원복
        }
    }
    
    // 2. 조회
    // 전체 주문 가격 조회
    public int getTotalPrice(){
        //(주문수량 * 가격 이므로)
        // 1. for
        // for (OrderItem o : orderItems){totalPrice += o.getTotalPrice();}

        // 2. stream
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }
    
}
