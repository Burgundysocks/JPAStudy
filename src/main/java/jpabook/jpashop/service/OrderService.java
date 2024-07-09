package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {



    private final OrderRepository orderRepository;
    private final MemberRepositoryOld memberRepositoryOld;
    private final ItemRepository itemRepository;


    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepositoryOld.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문생성
        Order order = Order.createOrder(member,delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        //cascade가 있기에 따로 delivery를 저장하지 않아도 한번에 저장함!!!그래서 order만 저장해도 orderitem, delivery 한번에 저장가능
        //하지만 현재 프로젝트 계획에서는 order에서만 orderitem과 delivery를 사용하기에 cascade를 사용함
        //만약 다른데서 사용할 일이 있다면 사용하면 안됨 그럴땐 repository를 만들어서 해결해야해
        return order.getId();

    }

    /**
     * 주문취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    /**
     * 주문 검색
     */
    public List<Order> findOrder(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }

}
