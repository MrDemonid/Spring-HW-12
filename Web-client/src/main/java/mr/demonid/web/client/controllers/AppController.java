package mr.demonid.web.client.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.ProductInfo;
import mr.demonid.web.client.service.CatalogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@AllArgsConstructor
public class AppController {

    private CatalogService catalogService;

    @GetMapping
    public String baseDir() {
        System.out.println("redirect to index...");
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
}

