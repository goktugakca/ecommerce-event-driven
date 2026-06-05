package com.goktugakca.order_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")//order sql'de ayrılmış bir kelime olduğundan kendimiz orders olarak isim verdik tabloya
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productSku;
    private int quantity;
}
