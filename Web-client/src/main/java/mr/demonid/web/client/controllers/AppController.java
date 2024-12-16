package mr.demonid.web.client.controllers;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.ProductInfo;
import mr.demonid.web.client.links.PaymentServiceClient;
import mr.demonid.web.client.service.CartService;
import mr.demonid.web.client.service.CatalogService;
import mr.demonid.web.client.service.PaymentService;
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
    private PaymentService paymentService;


    @GetMapping
    public String baseDir() {
        System.out.println("redirect to index...");
        return "redirect:/index";
    }

    /**
     * Отображение главной страницы магазина.
     * @param session Текущая сессия, для хранения настроек.
     */
    @GetMapping("/index")
    public String index(HttpSession session, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? authentication.getName() : null);

        List<ProductInfo> products;
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


    /**
     * Фильтр категорий товара. Просто задает тип категории и уходит обратно на страницу.
     * @param session  Текущая сессия, где мы будем временно хранить тип категории.
     * @param category Выбранный пользователем тип категории товара.
     */
    @GetMapping("/set-category")
    public String setCategory(HttpSession session, Model model, @RequestParam("category") String category) {
        session.setAttribute("category", category);
        return "redirect:/index";
    }

    /**
     * Используется для перенаправления пользователя на форму авторизации.
     */
    @GetMapping("do-login")
    public String doLogin() {
        return "redirect:/index";
    }


    /**
     * Добавляем товар в корзину.
     */
    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("productId") Long productId,
                             @RequestParam("quantity") Integer quantity,
                             Model model,
                             HttpServletRequest request)
    {
        System.out.println("Product ID: " + productId);
        System.out.println("Quantity: " + quantity);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Пользователь анонимный
            System.out.println("Anonim: " + getAnonymousId(request));
        }
        // Отправляем товар в корзину
        cartService.addToCart(productId.toString(), quantity);
        return "redirect:/index";
    }

    /**
     * Переход на страницу карзины.
     * Только для авторизированных пользователей.
     * Хотя никто не мешает авторизировать пользователя и попозже, когда
     * нажмет кнопку оплаты.
     */
    @GetMapping("cart")
    public String placeOrder(Model model) {
        List<CartItem> items = cartService.getItems();
        System.out.println("--> all cart items: " + items);
        model.addAttribute("cartItems", items);
        List<String> payments = paymentService.getPaymentStrategies();
        model.addAttribute("paymentMethods", payments);
        return "/cart";
    }

    /**
     * Формирование запроса на оплату товаров.
     * @param paymentMethod Метод оплаты.
     */
    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("paymentMethod") String paymentMethod, Model model) {
        // Логика обработки выбранного способа оплаты
        System.out.println("Выбранный способ оплаты: " + paymentMethod);

        // Добавьте логику, например, сохранение данных или вызов сервиса
        model.addAttribute("message", "Выбранный способ оплаты: " + paymentMethod);

        // Возвращаем страницу подтверждения или перенаправляем
        return "paymentResult"; // paymentResult.html
    }



//    public void setAnonymousCookie(HttpServletResponse response) {
//        Cookie cookie = new Cookie("ANON_ID", UUID.randomUUID().toString());
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
////        cookie.setSameSite("Lax"); // Защита от CSRF
//        response.addCookie(cookie);
//    }

    public String getAnonymousId(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}

