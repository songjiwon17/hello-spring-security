package kr.ac.hansung.service;

import kr.ac.hansung.dto.ProductDto;
import kr.ac.hansung.entity.Product;
import kr.ac.hansung.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 페이징하기 위한 getProducts 메서드
    @Transactional(readOnly = true)
    public Page<Product> getProducts(String keyword, Pageable pageable){
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        // 검색어가 있으면 이름에 포함된 상품만 검색해서 반환
        return productRepository.findByNameContaining(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public Product save(ProductDto dto) {
        Product product = new Product(
            dto.getName(), dto.getPrice(), dto.getDescription(), dto.getStock()
        );
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
