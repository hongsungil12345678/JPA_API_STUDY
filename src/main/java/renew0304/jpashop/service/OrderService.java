package renew0304.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renew0304.jpashop.domain.*;
import renew0304.jpashop.domain.item.Item;
import renew0304.jpashop.repository.ItemRepository;
import renew0304.jpashop.repository.MemberRepository;
import renew0304.jpashop.repository.OrderRepository;
import renew0304.jpashop.repository.OrderSearch;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    
    // 주문
    @Transactional
    public Long order(Long memberId,Long itemId,int quantity){
        // 엔티티 조회 (회원, 아이템)
        Member member = memberRepository.findMember(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 조회된 회원 엔티티에서 가져옴
        delivery.setStatus(DeliveryStatus.READY);
        // 주문상품 생성 (OrderItem 생성 메서드 사용)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), quantity);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }
    
    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 번호로 조회
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }


}
