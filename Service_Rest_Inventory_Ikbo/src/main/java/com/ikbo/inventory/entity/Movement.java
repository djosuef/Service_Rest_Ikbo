package com.ikbo.inventory.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "movement")
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeMov;
    private LocalDateTime dateMov;
    private BigDecimal quantity;
    private Date expirationDate;
    private BigDecimal currentQuantity;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

    public Movement(String typeMov, LocalDateTime dateMov, BigDecimal quantity, Product product) {
        this.typeMov = typeMov;
        this.dateMov = dateMov;
        this.quantity = quantity;
        this.product = product;
    
    }

    

}
