/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.webapp.ecom.Service.product;

import java.util.List;
import java.util.Optional;

import com.webapp.ecom.dto.ImageDto;
import com.webapp.ecom.dto.ProductDto;
import com.webapp.ecom.exceptions.AlreadyExistsException;
import com.webapp.ecom.model.Category;
import com.webapp.ecom.model.Image;
import com.webapp.ecom.repository.CategoryRepository;
import com.webapp.ecom.repository.ImageRepository;
import com.webapp.ecom.request.AddProductRequest;
import com.webapp.ecom.request.UpdateProductRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.webapp.ecom.exceptions.ProductNotFoundException;
import com.webapp.ecom.model.Product;
import com.webapp.ecom.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author ajhar
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

//    @Autowired
    private final ProductRepository  productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
//        Check if the category is found in the DB
//        if yes set it as the new product category
//        if not, then save it as a new category
//        then set it as the new product category
        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getBrand()+" "+ request.getName()+" already exists, Instead of adding product, try updating the already existing product");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                    }
                );
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository
            .findById(id)
            .orElseThrow(
                () -> new ProductNotFoundException("Product not found")
            );
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
            .ifPresentOrElse(productRepository::delete,
                ()->{throw new ProductNotFoundException("Product not found");
            }
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId){

        return productRepository.findById(productId)
                .map(existingProduct-> updateExistingProduct(existingProduct,request))
                .map(productRepository::save)
                .orElseThrow(()->new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }


    @Override
    public List<Product> getALlProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category)  {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
//        return (long) productRepository.findByBrandAndName(brand, name).size();
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                                    .map(image->modelMapper.map(image,ImageDto.class))
                                    .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
    
} 
