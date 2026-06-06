package kr.ac.hansung.controller;

import kr.ac.hansung.dto.ProductDto;
import kr.ac.hansung.entity.Product;
import kr.ac.hansung.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 페이징 화면 처리(상품 5개씩, id기준으로 정렬해서 보여지도록)
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        // URL 파라미터(page, size)로 페이지 요청 객체 생성, id 순 정렬 (오름차순)
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));

        // 빈 문자열("")을 null로 정규화
        String normalizedKeyword = (keyword != null && !keyword.isBlank()) ? keyword : null;

        Page<Product> productPage;
        if (normalizedKeyword != null) {
            // 검색어가 있으면 키워드로 검색
            productPage = productService.searchProducts(normalizedKeyword, pageRequest);
        } else {
            // 검색어가 없으면 전체 목록 조회
            productPage = productService.getProducts(pageRequest);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("keyword", normalizedKeyword);

        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "products/detail";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new ProductDto());
        return "products/add";
    }

    @PostMapping
    public String save(@ModelAttribute ProductDto dto) {
        productService.save(dto);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }
}
