package com.app.autismo.service.impl;

import com.app.autismo.model.Role;
import com.app.autismo.model.User;
import com.app.autismo.repository.RoleRepository;
import com.app.autismo.repository.UserRepository;
import com.app.autismo.security.SecurityConfig;
import com.app.autismo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Lazy;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityConfig securityConfig;

    public UserServiceImpl(UserRepository userRepository, @Lazy SecurityConfig securityConfig, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityConfig = securityConfig;
    }

    @Override
    public User saveUser(User user) {
        // Verificar se o objeto 'user' é nulo
        if (user == null) {
            throw new IllegalArgumentException("O usuário não pode ser nulo");
        }

        // Verificar se o password é nulo ou vazio
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser nula ou vazia");
        }

        // Codificar a senha do usuário
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Buscar a função do usuário
        Role userRole = roleRepository.findByName("ROLE_USER");

        // Verificar se o userRole é nulo
        if (userRole == null) {
            throw new IllegalStateException("O papel 'ROLE_USER' não encontrado");
        }

        // Configurar o papel do usuário
        user.setRoles(new HashSet<>(Set.of(userRole)));

        // Salvar e retornar o usuário
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
