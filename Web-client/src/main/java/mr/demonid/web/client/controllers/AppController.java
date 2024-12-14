package mr.demonid.web.client.controllers;

import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.ProductInfo;
import mr.demonid.web.client.dto.UserInfo;
import mr.demonid.web.client.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        System.out.println("Get catalog...");
        List<ProductInfo> products = catalogService.getProducts();
        System.out.println("Num of products: " + products.size());
        System.out.println("Catalog: " + products);
        model.addAttribute("products", products);
        return "/home";
    }


}

/*
    @GetMapping
    public String getFile(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User,
            Model model)
    {
        System.out.println("do getFile()....");
        try {
            String url = "http://localhost:8070/download";
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            byte[] image = downloadService.downloadFileWithToken(url+"?filename=pk8000.png", accessToken);
            String text = downloadService.downloadTextFileWithToken(url+"?filename=read.me", accessToken);
            model.addAttribute("imageBase64", Base64.getEncoder().encodeToString(image));
            model.addAttribute("fileContent", text);
            return "/home-page";
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            model.addAttribute("errorMessage", "ОШИБКА!");
            model.addAttribute("errorDetails", e.getMessage());
            return "/error";
        }
    }

 */