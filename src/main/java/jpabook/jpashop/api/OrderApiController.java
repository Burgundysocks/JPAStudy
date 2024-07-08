package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDTO;
import jpabook.jpashop.repository.order.query.OrderQueryDTO;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    //엔티티 직접노출방법 사실 이건 그냥 예시지 절대 이렇게 하면 안됨
    @GetMapping("/api/v1/orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
                // order item 강제 초기화
            }
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDTO> orderV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<OrderDTO> collect = orders.stream()
                .map(o -> new OrderDTO(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDTO> collect = orders.stream()
                .map(o -> new OrderDTO(o))
                .collect(Collectors.toList());

        return collect;
    }
    @GetMapping("/api/v3.1/orders")
    //이거면 거의 끝남
    public List<OrderDTO> orderV3_page(@RequestParam(value="offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit",defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDTO> collect = new ArrayList<>();
        for (Order o : orders) {
            collect.add(new OrderDTO(o));
        }


        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDTO>ordersV4(){
        return orderQueryRepository.findOrderQueryDTO();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDTO> ordersV5(){
        return orderQueryRepository.findOrderDTO_Optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDTO> ordersV6(){
        return orderQueryRepository.findOrderDTO_flat();
    }

    @Getter @Setter
    static class OrderDTO{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDTO> orderItems;
        //List<OrderItem>으로 OrderItem은 엔티티라서 이것조차 DTO로 바꿔야함

        public OrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDTO(orderItem))
                    .collect(Collectors.toList());
        }
    }
    @Getter
    static class OrderItemDTO{

        private String itemName; //상품명
        private int orderPrice; //주문가격
        private int count; //주문수량

        public OrderItemDTO(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();

        }
    }



}
