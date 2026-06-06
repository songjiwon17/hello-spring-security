package kr.ac.hansung.repository;

import kr.ac.hansung.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 상품 개수 세기
    long countByStockEquals(int stock);

    // 이름에 키워드가 포함된(Containing) 상품을 찾아 페이징 처리해 주는 메서드
    Page<Product> findByNameContaining(String keyword, Pageable pageable);
}
