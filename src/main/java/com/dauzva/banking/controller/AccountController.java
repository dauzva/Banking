package com.dauzva.banking.controller;

import com.dauzva.banking.exception.InsufficientFundsException;
import com.dauzva.banking.model.BankAccount;
import com.dauzva.banking.model.User;
import com.dauzva.banking.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AccountController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        User user = userService.getUserRepository().findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB!"));

        BankAccount account = userService.getBankAccountForUser(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("bankAccount", account);
        return "dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal UserDetails currentUser, @RequestParam("amount") double amount, RedirectAttributes redirectAttributes) {
        User user = userService.getUserRepository().findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB!"));

        BankAccount account = userService.getBankAccountForUser(user.getId());

        try {
            account.deposit(amount);
            userService.getBankAccountRepository().save(account);
            redirectAttributes.addFlashAttribute("success", "deposit");
            logger.info("Deposit successful for user: {} amount: {}", user.getUsername(), amount);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "invalidAmount");
            logger.error("Invalid deposit amount: {} for user: {}", amount, user.getUsername());
        }
        return "redirect:/dashboard";

    }

    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal UserDetails currentUser, @RequestParam("amount") double amount, RedirectAttributes redirectAttributes) {
        User user = userService.getUserRepository().findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB!"));

        BankAccount account = userService.getBankAccountForUser(user.getId());

        try {
            account.withdraw(amount);
            userService.getBankAccountRepository().save(account);
            redirectAttributes.addFlashAttribute("success", "withdraw");
            logger.info("Withdrawal successful for user: {} amount: {}", user.getUsername(), amount);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "invalidAmount");
            logger.error("Invalid withdrawal amount: {} for user: {}", amount, user.getUsername());
        } catch (InsufficientFundsException e) {
            redirectAttributes.addFlashAttribute("error", "insufficientFunds");
            logger.error("Insufficient funds for withdrawal: {} for user: {}", amount, user.getUsername());
        }
        return "redirect:/dashboard";
    }
}