package com.ikbo.inventory.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ikbo.inventory.entity.Movement;
import com.ikbo.inventory.entity.Product;
import com.ikbo.inventory.repository.MovementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementService {
    @Autowired
    private MovementRepository movementRepository;

    public void outInventory(Product product, BigDecimal quantity, Optional<Movement> movementOld) {
                    movementRepository.save(Movement.builder()
                    .product(product)
                    .quantity(quantity)
                    .typeMov("OUT")
                    .expirationDate(null)
                    .dateMov(LocalDateTime.now())
                    .build());
        // TODO Auto-generated method stub
    }

    public void substracQuantity(Movement movement, BigDecimal quantity) {
        movement.setCurrentQuantity(movement.getCurrentQuantity().subtract(quantity));
        movementRepository.save(movement);
    }

    public void InputInventory(Product product, BigDecimal quantity, Date expirationDate) {
        movementRepository.save(Movement.builder()
                .product(product)
                .quantity(quantity)
                .currentQuantity(quantity)
                .typeMov("INPUT")
                .expirationDate(expirationDate)
                .dateMov(LocalDateTime.now())
                .build());
    }

     public ResponseEntity<?> validateMovemet(ProductService productService, Movement movementNew, Optional<Movement> movementOld) {

        //Que exista el producto
        if(productService.getProductById(movementNew.getProduct().getId()) == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        //Que la cantidad del movimiento sea mayor a 0
        if(movementNew.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than zero");
        }

        //Que la fecha de expiracion sea mayor a la fecha actual
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        if(movementNew.getTypeMov().equals("INPUT") && movementNew.getExpirationDate().before(date))
        {   
            return ResponseEntity.badRequest().body("Expiration date must be greater than current date");
        }

        //Validaciones especificas para el movimiento de salida
        if(movementNew.getTypeMov().equals("OUT")){

            //Que la cantidad del movimiento sea menor o igual al stock del producto
            if(productService.getProductById(movementNew.getProduct().getId()).getStock().compareTo(movementNew.getQuantity()) < 0) {
                return ResponseEntity.badRequest().body("Quantity greater than stock");
            }

            //Que exista el movimiento de entrada seleccionado para aplicar la salida
            if(movementOld.isEmpty()) {
                return ResponseEntity.badRequest().body("Movement not found");
            }

            //Que la cantidad del movimiento de salida sea menor o igual a la cantidad actual del movimiento de entrada seleccionado
            if(movementOld.get().getCurrentQuantity().compareTo(movementNew.getQuantity()) < 0) {
                return ResponseEntity.badRequest().body("Quantity must be less than or equal to the input movement selected");
            }

            //Que el producto del movimiento de entrada seleccionado sea el mismo que el producto del movimiento de salida
            if(movementOld.get().getProduct().getId() != movementNew.getProduct().getId()) {
                return ResponseEntity.badRequest().body("Product must be the same as the input movement selected");
            }
        } 

        return ResponseEntity.ok().build();

     }

    public Movement getMovementById(Long id) {
        return movementRepository.findById(id).orElse(null);
    }
}
