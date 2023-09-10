package ru.webapp.exchange.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.webapp.exchange.models.Currency;
import ru.webapp.exchange.models.Expense;
import ru.webapp.exchange.models.User;
import ru.webapp.exchange.security.UserDetailsImpl;
import ru.webapp.exchange.services.CurrencyService;
import ru.webapp.exchange.services.RegistrationService;
import ru.webapp.exchange.services.UserService;
import ru.webapp.exchange.validator.UserValidator;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final UserService userService;
    private final CurrencyService currencyService;

    @Autowired
    public AuthController(UserValidator userValidator, RegistrationService registrationService,
                          UserService userService, CurrencyService currencyService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.userService = userService;
        this.currencyService = currencyService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/login?error";
    }

    @GetMapping("/new")
    public String newUserPage(@ModelAttribute("user") User user) {
        return "/auth/new";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult br) {
        userValidator.validate(user, br);
        if (br.hasErrors())
            return "/auth/new";

        registrationService.register(user);

        return "redirect:/auth/hello";
    }

    @GetMapping("/hello")
    public String helloPage(Principal principal, Model model,
                            HttpSession session) {

        UserDetailsImpl details = (UserDetailsImpl)
                userService.loadUserByUsername(principal.getName());
        User user = details.getUser();
        Currency[] currencies = currencyService.getCurrencies();
        model.addAttribute("user", user);
        model.addAttribute("currencies", currencies);

        String currency = (String) session.getAttribute("currency");
        if (currency == null) {
            currency = "RUB";
            session.setAttribute("currency", currency);
        }

        List<Expense> expenses = currencyService.switchToCurrency(user.getExpenseList(), currency, currencies);

        model.addAttribute("expenses", expenses);
        return "auth/hello";
    }

    @PostMapping("/hello")
    public String updateCurrencies() {
        Currency[] currencies = currencyService.updateExchange();
        return "redirect:/auth/hello";
    }
}