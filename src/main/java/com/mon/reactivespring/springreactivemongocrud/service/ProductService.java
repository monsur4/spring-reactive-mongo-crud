package com.mon.reactivespring.springreactivemongocrud.service;

import com.mon.reactivespring.springreactivemongocrud.dto.ProductDto;
import com.mon.reactivespring.springreactivemongocrud.entity.Product;
import com.mon.reactivespring.springreactivemongocrud.repository.ProductRepository;
import com.mon.reactivespring.springreactivemongocrud.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Flux<ProductDto> getProducts(){
        return repository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProduct(String id){
        return repository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductInPriceRange(double min, double max){
        return repository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(repository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id){
        return repository.findById(id)
                .flatMap(p -> productDtoMono.map(AppUtils::dtoToEntity).doOnNext(prod -> prod.setId(id)))
                .flatMap(repository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return repository.deleteById(id);
    }

}
