package com.mon.reactivespring.springreactivemongocrud.repository;

import com.mon.reactivespring.springreactivemongocrud.dto.ProductDto;
import com.mon.reactivespring.springreactivemongocrud.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);
}
