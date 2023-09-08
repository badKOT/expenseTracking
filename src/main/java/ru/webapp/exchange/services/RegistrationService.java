package ru.webapp.exchange.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.webapp.exchange.models.User;
import ru.webapp.exchange.repositories.UserRepository;

@Service
public class RegistrationService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }
}
