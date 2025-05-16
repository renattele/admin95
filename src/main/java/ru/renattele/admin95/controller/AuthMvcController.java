package ru.renattele.admin95.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.renattele.admin95.api.AuthMvcApi;

@Controller
public class AuthMvcController implements AuthMvcApi {
    @Override
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("ok");
    }
}
