package com.sample.ecomm.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sample.ecomm.model.Product;
import com.sample.ecomm.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class ProductController {
		
		@Autowired
		ProductService service;
		
		
		@GetMapping("/products")
		public List<Product> getAllProducts(){
			return service.getAllProducts();
		}
		
		@GetMapping("/product/{id}")
		public ResponseEntity<Product> getProductById(@PathVariable int id) {
			Product product = service.getProductById(id);
			if(product !=null)
				return new ResponseEntity(product,HttpStatus.OK);
			else
				return new ResponseEntity(product,HttpStatus.NOT_FOUND);
		}
		
		@PostMapping("/product")
		public ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart MultipartFile imageFile){
			try {
				Product prod= service.addProduct(product,imageFile);
				return new ResponseEntity(product,HttpStatus.CREATED);
			}catch(Exception e) {
				return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@GetMapping("/product/{productId}/image")
		public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
			Product product = service.getProductById(productId);
			byte[] imageFile = product.getImageData();
			
			return ResponseEntity.ok()
					.contentType(MediaType.valueOf(product.getImageType()))
					.body(imageFile);
		}
		@PutMapping("/product/{productId}")
		public ResponseEntity<String> updateProduct(@PathVariable int productId,@RequestPart Product product,
				@RequestPart MultipartFile imageFile){
			Product prod;
			try {
				 prod = service.updateProduct(productId,product,imageFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
			}
			if(prod != null) {
				return new ResponseEntity<>("Updated",HttpStatus.OK);
			}else {
				return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
			}
		}
		
		@DeleteMapping("/product/{productId}")
		public ResponseEntity<String> deleteProduct(@PathVariable int productId){
			Product product = service.getProductById(productId);
			if(product != null) {
				service.deleteProduct(productId);
				return new ResponseEntity<>("Deleted",HttpStatus.OK);
			}else {
				return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
			}
		}
		
		@GetMapping("/products/search")
		public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
			List<Product> products = service.searchProduct(keyword);
			return new ResponseEntity<>(products,HttpStatus.OK);
		}
}
