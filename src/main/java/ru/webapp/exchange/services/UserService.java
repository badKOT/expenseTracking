package ru.webapp.exchange.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.webapp.exchange.models.User;
import ru.webapp.exchange.repositories.UserRepository;
import ru.webapp.exchange.security.UserDetailsImpl;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public Optional<User> findOne(int id) {
        return repo.findById(id);
    }

    @Transactional
    public void save(User user) {
        repo.save(user);
    }

    @Transactional
    public void update(int id, User newUser) {
        newUser.setId(id);
        Optional<User> oldUser = repo.findById(id);
        newUser.setPassword(oldUser.get().getPassword());
        repo.save(newUser);
    }

    @Transactional
    public void delete(int id) {
        repo.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repo.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetailsImpl(user.get());
    }
}
