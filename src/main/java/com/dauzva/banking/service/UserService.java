package com.dauzva.banking.service;

import com.dauzva.banking.model.BankAccount;
import com.dauzva.banking.model.User;
import com.dauzva.banking.repository.BankAccountRepository;
import com.dauzva.banking.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired; // Added this import
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Getter
    private final UserRepository userRepository;
    @Getter
    private final BankAccountRepository bankAccountRepository;
    private final PasswordEncoder passwordEncoder;

    // Explicitly annotate constructor for autowiring.
    // Spring will now ensure these dependencies are provided when UserService is created.
    @Autowired
    public UserService(UserRepository userRepository, BankAccountRepository bankAccountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    @Transactional // Ensures that the entire operation (user and bank account creation) is atomic
    public User registerUser(User user) {
        // Check for existing username and email to prevent duplicates
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        String newAccountNumber = generateUniqueAccountNumber();
        BankAccount bankAccount = new BankAccount(newAccountNumber, savedUser);
        bankAccountRepository.save(bankAccount);

        savedUser.setBankAccount(bankAccount);

        return savedUser;
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        } while (bankAccountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }

    public BankAccount getBankAccountForUser(Long userId) {
        return bankAccountRepository.findByUser_Id(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Bank account not found for user ID: " + userId));
    }

}
