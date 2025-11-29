package com.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static class AuthRequest {
        @NotBlank
        @Size(max = 100)
        public String username;
        @NotBlank
        @Size(min = 4, max = 100)
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        if (req == null || req.username == null || req.username.isBlank() || req.password == null || req.password.isBlank()) {
            return ResponseEntity.badRequest().body("Dados inválidos");
        }
        if (usuarioRepository.findByUsername(req.username) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }
        Usuario u = new Usuario();
        u.setUsername(req.username.trim());
        u.setPasswordHash(encoder.encode(req.password));
        usuarioRepository.save(u);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req, HttpSession session) {
        if (req == null || req.username == null || req.password == null) {
            return ResponseEntity.badRequest().body("Dados inválidos");
        }
        Usuario u = usuarioRepository.findByUsername(req.username);
        if (u == null) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
        if (!encoder.matches(req.password, u.getPasswordHash())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
        session.setAttribute("usuarioId", u.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object id = session.getAttribute("usuarioId");
        if (id == null) {
            return ResponseEntity.status(401).build();
        }
        Usuario u = usuarioRepository.findById((Long) id).orElse(null);
        if (u == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(u.getUsername());
    }
}
