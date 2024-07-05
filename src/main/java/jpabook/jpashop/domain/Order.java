package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id@GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    //cascade = CascadeType.All로 인해 order를 저장할때 관련된 orderitems가 같이 연쇄적으로 저장됨, 삭제도 마찬가지
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;//주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //연관관계 편의 메소드

    //Order 엔티티에 Member 엔티티를 설정
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
//    this.member = member;: Order 엔티티의 member 필드를 설정합니다.
//    member.getOrders().add(this);: Member 엔티티의 orders 리스트에 현재 Order 엔티티를 추가합니다.


//    Order 엔티티에 OrderItem 엔티티를 추가
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
//    orderItems.add(orderItem);: Order 엔티티의 orderItems 리스트에 OrderItem을 추가합니다.
//    orderItem.setOrder(this);: OrderItem 엔티티의 order 필드를 현재 Order 엔티티로 설정합니다.
    //Order와 OrderItem 간의 양방향 관계를 일관되게 유지

//    Order 엔티티에 Delivery 엔티티를 설정
    public void delivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
//    this.delivery = delivery;: Order 엔티티의 delivery 필드를 설정합니다.
//    delivery.setOrder(this);: Delivery 엔티티의 order 필드를 현재 Order 엔티티로 설정합니다.
//    이를 통해 Order와 Delivery 간의 양방향 관계를 일관되게 유지



}
