package ru.webapp.exchange.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.webapp.exchange.models.Expense;
import ru.webapp.exchange.models.User;
import ru.webapp.exchange.security.UserDetailsImpl;
import ru.webapp.exchange.services.ExpenseService;
import ru.webapp.exchange.services.UserService;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/exp")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String add(Model model, Principal principal) {
        Expense expense = new Expense();
        model.addAttribute("expense", expense);
        return "exp/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("expense") @Valid Expense expense,
                         BindingResult br, Principal principal) {
        UserDetailsImpl details = (UserDetailsImpl)
                userService.loadUserByUsername(principal.getName());
        User user = details.getUser();
        expense.setOwner(user);
        expense.setAddedAt(new Date());

        if (br.hasErrors())
            return "exp/new";

        expenseService.save(expense);
        return "redirect:/auth/hello";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("expense", expenseService.findById(id));
        model.addAttribute("id", id);
        return "exp/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("expense") @Valid Expense expense,
                         BindingResult br, @PathVariable("id") int id, Principal principal) {
        UserDetailsImpl details = (UserDetailsImpl)
                userService.loadUserByUsername(principal.getName());
        User user = details.getUser();
        expense.setOwner(user);

        if (br.hasErrors())
            return "exp/edit";

        expenseService.update(id, expense);
        return "redirect:/auth/hello";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        expenseService.delete(id);
        return "redirect:/auth/hello";
    }

    @PostMapping("/currency")
    public String updateCurrency(@RequestParam String currency, HttpSession session) {
        session.setAttribute("currency", currency);

        return "redirect:/auth/hello";
    }
}
