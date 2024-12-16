package mr.demonid.web.client.controllers;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.ProductInfo;
import mr.demonid.web.client.service.CartService;
import mr.demonid.web.client.service.CatalogService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Controller
@AllArgsConstructor
public class AppController {

    private CatalogService catalogService;
    private CartService cartService;


    @GetMapping
    public String baseDir() {
        System.out.println("redirect to index...");
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Пользователь анонимный
            System.out.println("Anonim: " + getAnonymousId(request));
        }

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal());
        if (isAuthenticated) {
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("username", null);
        }
        model.addAttribute("isAuthenticated", isAuthenticated);


        List<ProductInfo> products;
        System.out.println("Get catalog...");
        // Создаем список категорий продуктов.
        List<String> categories = catalogService.getCategories();
        categories.add("All");
        // Настраиваем страницу
        String category = (String) session.getAttribute("category");    // смотрим, есть ли в сессии данные о текущей категории?
        if (category != null && !category.equals("All")) {
            products = catalogService.getProductsByCategory(category);
        } else {
            products = catalogService.getProducts();
            category = "All";
        }
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", category);
        model.addAttribute("products", products);

        model.addAttribute("cartItemCount", cartService.getProductCount());
        return "/home";
    }



    @GetMapping("/set-category")
    public String setCategory(HttpSession session, Model model, @RequestParam("category") String category) {
        System.out.println("Set category..." + category);
        session.setAttribute("category", category);
        return "redirect:/index";
    }

    @GetMapping("do-login")
    public String doLogin(HttpSession session, Model model) {
        return "redirect:/index";
    }


    /**
     * Добавляем товар в корзину.
     */
    @PostMapping("/cart")
    public String placeOrder(@RequestParam("productId") Long productId,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("price") BigDecimal price,
                             Model model,
                             HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("Product ID: " + productId);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: " + price);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Пользователь анонимный
            System.out.println("Anonim: " + getAnonymousId(request));
        }
        List<CartItem> cartItems = cartService.getItems();

        model.addAttribute("cartItems");

        // открываем заказ
        cartService.addToCart(productId.toString(), quantity);
        return "redirect:/index";
    }


    public void setAnonymousCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("ANON_ID", UUID.randomUUID().toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
//        cookie.setSameSite("Lax"); // Защита от CSRF
        response.addCookie(cookie);
    }

    public String getAnonymousId(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}

