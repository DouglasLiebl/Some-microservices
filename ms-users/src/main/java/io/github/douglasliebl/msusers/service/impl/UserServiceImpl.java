package io.github.douglasliebl.msusers.service.impl;

import io.github.douglasliebl.msusers.model.repositories.UserRepository;
import io.github.douglasliebl.msusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
}
