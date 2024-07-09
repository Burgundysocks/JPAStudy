package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    

    @Test
    public void 상품주문() throws Exception {
        Member member = createMember();

        Item item = createBook("B", 10000, 10);

        int orderCount = 1;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);


        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(orderCount, getOrder.getOrderItems().size(), "주문한 상품수 가 같아야 함");
        assertEquals(getOrder.getTotalPrice(), 10000 * orderCount, "주문 가격은 주문량 * 가격 이다");
        assertEquals(item.getStockQuantity(), 9, "주문 수량 만큼 재고가 줄어야 한다");

    }

    @Test
    public void 상품수량재고초과() throws Exception {
        assertThrows(NotEnoughStockException.class, () -> {
            Member member = createMember();
            Item item = createBook("B", 10000, 10);

            int orderCount = 11;
            orderService.order(member.getId(), item.getId(), orderCount);

            fail("재고 수량 부족 예외 발생해야함");

        });
    }
    @Test
    public void 상품취소() throws Exception {
        Member member = createMember();
        Item item = createBook("B", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(),"취소 상태 여야 한다.");
        assertEquals(10, item.getStockQuantity(),"주문취소 상품은 수량이 복구되야 한다.");

    }


    private Item createBook(String name, int price, int stoke) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stoke);
        em.persist(item);
        return item;
    }

    private Member createMember() {
        Member member = new Member();


        member.setName("A");
        member.setAddress(new Address("서울", "테헤란로", "100000"));
        em.persist(member);
        return member;
    }


}