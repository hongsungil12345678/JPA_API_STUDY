package renew0304.jpashop.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import renew0304.jpashop.domain.Address;
import renew0304.jpashop.domain.Member;
import renew0304.jpashop.domain.Order;
import renew0304.jpashop.domain.OrderStatus;
import renew0304.jpashop.domain.item.Book;
import renew0304.jpashop.domain.item.Item;
import renew0304.jpashop.exception.NotEnoughStockException;
import renew0304.jpashop.repository.OrderRepository;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("JPA_APP", 10000, 20);
        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //then
        Order getOrder = orderRepository.findOne(orderId);
            // OrderStatus 가 ORDER이 맞는지 검증
        assertEquals("Expected : 상태 - ORDER", OrderStatus.ORDER,getOrder.getOrderStatus());
        assertEquals("Expected : 주문 상품 종류 수",1,getOrder.getOrderItems().size());
        assertEquals("Expected : 가격 * 수량",10000*2,getOrder.getTotalPrice());
        assertEquals("Expected : 재고 수량이 줄어야한다",18,book.getStockQuantity());

    }

    // exception 발생 시 NotEnoughStockException.class
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량_초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("JPA_BOOK", 20000, 50);
        int orderCount = 55;

        //when
        orderService.order(member.getId(),item.getId(),orderCount);// 재고보다 수량이 많으므로 예외 발생해야함

        //then
        fail("재고 수량 예외 발생해야함"); //여기까지 통과하면 안된다는 것을 알려주기 위해서
    }
    @Test
    public void 주문_취소() throws Exception {
        //given
        Member member= createMember();
        Book item = createBook("주문_취소",30000,5);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);//주문
        //when
        orderService.cancelOrder(orderId);// 주문 취소
        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소-> 상태 CANCEL",OrderStatus.CANCEL,getOrder.getOrderStatus());
        assertEquals("주문이 취소될 경우 재고가 증가해야한다.",5,item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("서울시","양천구","123-123"));
        em.persist(member);
        return member;
    }
}