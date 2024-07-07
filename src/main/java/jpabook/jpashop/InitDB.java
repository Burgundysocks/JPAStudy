package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 총 주문 2개 데이터 넣기
 *  *userA
 *      *JPA1 Book
 *      *JPA2 Book
 *
 *  *userB
 *      *Spring1 Book
 *      *Spring2 Book
 */
@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        public void dbInit1(){
            Member member = getMember1();
            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA1 Book");
            book1.setPrice(10000);
            book1.setStockQuantity(100);

            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 Book");
            book2.setPrice(20000);
            book2.setStockQuantity(100);

            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 5);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 10);


            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Member getMember1() {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울", "111", "100000"));
            return member;
        }

        public void dbInit2(){
            Member member2 = getMember2();
            em.persist(member2);

            Book book1 = new Book();
            book1.setName("Spring1 Book");
            book1.setPrice(10000);
            book1.setStockQuantity(100);

            em.persist(book1);

            Book book2 = new Book();
            book2.setName("Spring2 Book");
            book2.setPrice(20000);
            book2.setStockQuantity(100);

            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 5);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 10);


            Delivery delivery = new Delivery();
            delivery.setAddress(member2.getAddress());
            Order order = Order.createOrder(member2, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Member getMember2() {
            Member member = new Member();
            member.setName("userB");
            member.setAddress(new Address("부산", "2", "200000"));
            return member;
        }

    }

}

