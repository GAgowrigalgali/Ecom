package org.example.ecom.repository;

import org.example.ecom.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;



    public interface ProductRepository extends JpaRepository<Product, Long>{
}
