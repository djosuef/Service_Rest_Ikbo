package com.ikbo.inventory.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ikbo.inventory.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Product findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.stock > :stock")
    public List<Product> findProductsWithStockGreaterThan(@Param("stock") BigDecimal stock);

    @Query(value ="""
       SELECT product.id as product_id, 
	   name as product_name,
       movement.id as lot_id, 
       movement.expiration_date as lot_expiration, 
       movement.current_quantity as lot_stock, 
	   CASE 
        WHEN movement.expiration_date < CURRENT_DATE THEN 'Vencido'
        WHEN movement.expiration_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY) THEN 'Por vencer'
        ELSE 'Vigente'
        END AS status
        FROM product
        Inner Join movement
        On product.id = movement.id_product
        where movement.current_quantity > 0 """, nativeQuery = true)
    public List<Object[]> findProductStates();

   /*  @Query("SELECT p FROM Product p WHERE p.expirationDate > DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY)")
    public List<Product> findVigenteProducts();

   @Query("SELECT p FROM Product p WHERE p.expirationDate BETWEEN CURRENT_DATE AND CURRENT_DATE + 3")
    public List<Product> findPorVencerProducts();

    @Query("SELECT p FROM Product p WHERE p.expirationDate < CURRENT_DATE")
    public List<Product> findVencidoProducts();*/

    //public List<Product> findByExpirationState(String expirationState);
}
