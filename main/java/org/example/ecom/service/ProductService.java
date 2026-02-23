package org.example.ecom.service;

import org.example.ecom.dtos.request.ProductRequest;
import org.example.ecom.dtos.response.ProductResponse;
import org.example.ecom.entity.Product;
import org.example.ecom.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    //constructor based dependancy injection --> loosely
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    //this Product object comes from the request body --> Hibernate inserts in table
    public List<Product> getAll(Product product){
        return productRepository.findAll();
    }
    public ProductResponse getById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found!"));
        return mapToResponse(product);
    }

    public ProductResponse update(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());

        Product updated = productRepository.save(product);

        return mapToResponse(updated);
    }

    public void delete(Long id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        productRepository.deleteById(id);
    }




       public ProductResponse create(ProductRequest request) {
            Product product = new Product();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
           product.setCategory(request.getCategory());
            Product saved = productRepository.save(product);

            return mapToResponse(saved);
        }
        private ProductResponse mapToResponse(Product product) {
          ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setQuantity(product.getQuantity());
            response.setCategory(product.getCategory());
            return response;
        }
    public List<ProductResponse> getAll() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }



}

