package com.mon.reactivespring.springreactivemongocrud.controller;

import com.mon.reactivespring.springreactivemongocrud.dto.ProductDto;
import com.mon.reactivespring.springreactivemongocrud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Flux<ProductDto> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProduct(@PathVariable String id){
        return productService.getProduct(id);
    }

    @GetMapping("/product-range")
    public Flux<ProductDto> updateProduct(@RequestParam double min, @RequestParam double max){
        return productService.getProductInPriceRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return productService.saveProduct(productDtoMono);
    }

    @PutMapping("/update/{id}")
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable String  id){
        return productService.updateProduct(productDtoMono, id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return productService.deleteProduct(id);
    }
}
