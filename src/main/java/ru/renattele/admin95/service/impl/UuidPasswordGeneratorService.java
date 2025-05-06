package ru.renattele.admin95.service.impl;

import org.springframework.stereotype.Service;
import ru.renattele.admin95.service.PasswordGeneratorService;

import java.util.UUID;

@Service
public class UuidPasswordGeneratorService implements PasswordGeneratorService {
    @Override
    public String generatePassword() {
        return UUID.randomUUID().toString();
    }
}
