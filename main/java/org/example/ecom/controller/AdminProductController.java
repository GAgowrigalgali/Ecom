package org.example.ecom.controller;

import org.example.ecom.dtos.request.ProductRequest;
import org.example.ecom.dtos.response.ProductResponse;
import org.example.ecom.entity.Product;
import org.example.ecom.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/products")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService){
        this.productService = productService;
    }
    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest request) {
        return productService.create(request);
    }
    @PutMapping("/id")
    public ProductResponse update(@PathVariable Long id,@RequestBody ProductRequest request){
        return productService.update(id,request);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }



}
