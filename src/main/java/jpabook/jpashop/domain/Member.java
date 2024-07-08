package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;


    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    //회원정보를 조회할 때 @JsonIgnore을 통해 order을 없앨 수 있음
    //하지만 이건 좀 불편해 어디는 필요할 수 있고 어딘 필요 없을 수 있잖아
    //그리고 엔티티를 변경하면 api스펙이 변경되버림 그래서 쓰면 안됨..



}
