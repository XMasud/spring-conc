package com.example.spring_conc.entity;

import com.example.spring_conc.exception.InvalidStatusException;
import com.example.spring_conc.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer quantity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Version
    private Long version;

    public void updateProduct(String name, BigDecimal price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


    public void decreaseQuantity(int orderedQty){

        if (orderedQty <= 0) {
            throw new IllegalArgumentException("Ordered quantity must be greater than zero");
        }

        if (quantity < orderedQty) {
            throw new OutOfStockException();
        }

        this.quantity -= orderedQty;
    }
}
