package kr.ac.hansung.controller;

import kr.ac.hansung.dto.PasswordChangeDto;
import kr.ac.hansung.service.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 비밀번호 변경 폼 표시 (GET /user/password)
    @GetMapping("/user/password")
    public String changePasswordForm(Model model) {
        // Thymeleaf의 th:object="${passwordChangeDto}"와 매핑할 빈 DTO 객체를 모델에 담아 보냄
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "user/password";
    }

    // 비밀번호 변경 처리 (POST /user/password)
    @PostMapping("/user/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("passwordChangeDto") PasswordChangeDto dto,
            BindingResult bindingResult,
            RedirectAttributes ra) {

        // 입력값 검증(@NotBlank 등) 에러가 있는 경우 다시 입력 폼으로 이동
        if (bindingResult.hasErrors()) {
            return "user/password";
        }

        // 새 비밀번호와 새 비밀번호 확인 값이 일치하는지 검증
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "새 비밀번호가 일치하지 않습니다");
            return "user/password";
        }

        try {
            // 현재 로그인한 사용자의 이메일(username)과 비밀번호 정보들을 서비스로 넘겨 처리
            userService.changePassword(userDetails.getUsername(),
                    dto.getCurrentPassword(), dto.getNewPassword());

            // 리다이렉트 후 대시보드나 홈 화면에서 띄워줄 성공 메시지
            ra.addFlashAttribute("successMessage", "비밀번호가 변경되었습니다");

        } catch (IllegalArgumentException e) {
            // 현재 비밀번호가 틀려 서비스에서 예외가 발생한 경우 에러 메시지를 바인딩하고 폼으로 복귀
            bindingResult.rejectValue("currentPassword", "wrong", e.getMessage());
            return "user/password";
        }

        // 성공 시 홈 화면으로 리다이렉트
        return "redirect:/home";
    }
}
