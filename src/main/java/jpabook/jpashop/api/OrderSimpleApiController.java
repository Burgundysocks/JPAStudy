package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDTO;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * XToOne(ManyToOne,OneToOne)
 * Order
 * Order->Member
 * Order->Delivery
 * 이렇게 연관 관계
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;
    
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
        //무한루프에 빠지게 됨, 이럴경우 모든 양방향 통신에 @JsonIgnore을 걸어야하지만 그럼 에러 생김....
    }

    @GetMapping("/api/v2/simple-orders")
    //쿼리문이 5번 나감(N+1)문제
    public List<SimpleOrderDTO> ordersV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<SimpleOrderDTO> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(new SimpleOrderDTO(order));
        }
        return result;

    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDTO> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDTO> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(new SimpleOrderDTO(order));
        }
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDTO> ordersV4() {
        return orderSimpleQueryRepository.findOrderDTO();
    }

    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }

    }


}
