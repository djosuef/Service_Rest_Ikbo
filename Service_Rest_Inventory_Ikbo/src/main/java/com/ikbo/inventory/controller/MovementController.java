package com.ikbo.inventory.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikbo.inventory.entity.Movement;
import com.ikbo.inventory.entity.Product;
import com.ikbo.inventory.service.MovementService;
import com.ikbo.inventory.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovementController {

    private final MovementService movementService;

    private final ProductService productService;

    @PostMapping(path = "/out_inventory", consumes={"application/json"})
    public ResponseEntity<?> outInventoryv2(@RequestBody String movement) throws JSONException{

        JSONObject jsonObject = new JSONObject(movement);

        //Se toma el movimiento que se va a realizar
        JSONObject movementObj = jsonObject.getJSONObject("movement");
        Product product = productService.getProductById(movementObj.getJSONObject("product").getLong("id"));
        String quantityStr = movementObj.get("quantity").toString();

        Movement movementOut = new Movement(movementObj.getString("typeMov"), 
                                            LocalDateTime.now(), 
                                            new BigDecimal(quantityStr),
                                            product);

        //Se toma el movimiento que se va a usar como base de la salida
        Long movementInputId = jsonObject.getLong("applyoninput");
        Optional<Movement> movementInput = Optional.ofNullable(movementService.getMovementById(movementInputId));


        ResponseEntity<?> validationMovement = movementService.validateMovemet(productService, movementOut, movementInput);
        if(validationMovement.getStatusCode().isError()) {
            return validationMovement;
        }

        movementService.outInventory(movementOut.getProduct(), movementOut.getQuantity(), movementInput);
        movementService.substracQuantity(movementInput.get(), movementOut.getQuantity());
        productService.decreaseProductStock(movementOut.getProduct().getId(), movementOut.getQuantity(), movementInput);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/input_inventory", consumes={"application/json"})
    public ResponseEntity<?> inInventory(@RequestBody Movement movement) {

        System.out.println(movement);

        ResponseEntity<?> validationMovement = movementService.validateMovemet(productService, movement, Optional.empty());

        if(validationMovement.getStatusCode().isError()) {
            return validationMovement;
        }

        movementService.InputInventory(movement.getProduct(), movement.getQuantity(), movement.getExpirationDate());
        productService.increaseProductStock(movement.getProduct().getId(), movement.getQuantity());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/movement/{id}")
    public ResponseEntity<?> getMovementById(@PathVariable Long id) {
        return ResponseEntity.ok(movementService.getMovementById(id));
    }


}
