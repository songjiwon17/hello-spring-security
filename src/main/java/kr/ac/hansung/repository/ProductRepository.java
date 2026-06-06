package kr.ac.hansung.repository;

import kr.ac.hansung.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 상품 개수 세기
    long countByStockEquals(int stock);

    // @Query를 이용한 명시적 LIKE 검색
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    Page<Product> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);
}
