package com.mon.reactivespring.springreactivemongocrud;

import com.mon.reactivespring.springreactivemongocrud.controller.ProductController;
import com.mon.reactivespring.springreactivemongocrud.dto.ProductDto;
import com.mon.reactivespring.springreactivemongocrud.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringReactiveMongoCrudApplicationTests {

	@MockBean
	private ProductService productService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void getProductsTest() {
		// given
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("101", "pencil", 15, 20_000),
				new ProductDto("102", "game", 7, 110_000));

		// when
		when(productService.getProducts()).thenReturn(productDtoFlux);
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		// then
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("101", "pencil", 15, 20_000))
				.expectNext(new ProductDto("102", "game", 7, 110_000))
				.verifyComplete();
	}

	@Test
	public void getProductTest(){
		// given
		Mono<ProductDto> productDto = Mono.just(new ProductDto("101", "pencil", 15, 20_000));
		// when
		when(productService.getProduct(any())).thenReturn(productDto);
		// then
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/101")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> p.getName().equals("pencil"))
				.verifyComplete();
	}

	@Test
	public void addProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "pencil", 15, 20_000));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		webTestClient.post().uri("/products")
				.body(productDtoMono, ProductDto.class)
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class);
	}

	@Test
	public void updateProductTest(){
		// given
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "pencil", 15, 20_000));

		// when
		when(productService.updateProduct(productDtoMono, "101")).thenReturn(productDtoMono);

		// then
		webTestClient.put().uri("/products/update/101")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class);
	}

	@Test
	public void deleteProductTest(){
		// given
		String productId = "101";
		// when
		when(productService.deleteProduct(productId)).thenReturn(Mono.empty());
		// then
		webTestClient.delete().uri("/products/delete/103")
				.exchange()
				.expectStatus().isOk();
	}

}
