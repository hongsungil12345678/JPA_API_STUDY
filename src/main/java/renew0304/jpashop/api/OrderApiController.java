package renew0304.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import renew0304.jpashop.domain.Address;
import renew0304.jpashop.domain.Order;
import renew0304.jpashop.domain.OrderStatus;
import renew0304.jpashop.repository.OrderRepository;
import renew0304.jpashop.repository.OrderSearch;
import renew0304.jpashop.repository.OrderSimpleQueryDto;
import renew0304.jpashop.repository.OrderSimpleQueryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  xToOne (ManyToOne, OneToOne) 관계 최적화
 *  Order
 *  Order - > Member , Order -> Delivery
 * */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * 엔티티 노출
     * order -> member, order -> delivery는 지연로딩 이므로 실제 엔티티가 아닌 프록시 존재
     * jackson 라이브러리는 기본적으로 이 프록시 객체를 json 으로 생성하는 방법을 모른다. 예외발생
     * Hibername5Module 을 스프링 빈으로 등록하여 해결
     * Hibernate5Module 모듈 등록, Lazy = null (build.gradle- 'com.fasterxml.jackson.datatype:jackson-datatype-hibernate5-jakarta')
     * 양방향 관계 문제 발생 -> @JsonIgnore
     * */

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        for(Order o : allByString){
            o.getMember().getName();// Lazy 강제 초기화
            o.getDelivery().getAddress(); // Lazy 강제 초기ㅗ하
        }
        return allByString;
    }
    /**
     * V2 : 엔티티를 조회해서 DTO로 변환 (fetch join 사용 X)
     * */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto>ordersV2(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = allByString.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getOrderStatus();
            address = order.getMember().getAddress();
        }
    }

    /**
     * v3 엔티티 조회, DTO 변환 (fetch join 사용)
     * */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> allWithMemberDelivery = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = allWithMemberDelivery.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;

    }

    /**
     * v4 DTO 바로 조회
    */
    @GetMapping("/api/v4/simpe-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDto();

    }



}
