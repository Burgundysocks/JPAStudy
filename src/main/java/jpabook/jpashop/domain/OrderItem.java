package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; //주문가격
    private int count; //주문수량

//    protected OrderItem() {}
//    //이렇게 하면 Order orderitem = new OrderItem(); 이거안됨
//    @NoArgsConstructor(access = AccessLevel.PROTECTED) 이거하면 안만들어도 됨


    //생성 메서드//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //비즈니스 로직//
    public void cancel() {
        getItem().addStock(count); //재고수 원복

    }

    //조회로직

    /**
     * 주문상품 전체 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
