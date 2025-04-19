package com.saif.pfe.controllers;

import com.saif.pfe.models.Pfe;
import com.saif.pfe.models.User;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.UserRepository;
import com.saif.pfe.security.jwt.JwtUtils;
import com.saif.pfe.services.PfeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pfes")
public class PfeController {

    @Autowired
    private PfeService pfeService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    // Create or Update
    @PostMapping
    public ResponseEntity<Pfe> createOrUpdatePfe(@RequestBody Pfe pfe) {
        return ResponseEntity.ok(pfeService.saveOrUpdatePfe(pfe));
    }

    // Find by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pfe> getPfeById(@PathVariable Long id) {
        Optional<Pfe> pfe = pfeService.getPfeById(id);
        return pfe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Find all
    @GetMapping
    public ResponseEntity<List<Pfe>> getAllPfes(@ModelAttribute SearchCriteria searchCriteria, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix
        }

        // Use JwtUtil to extract accountUsernameId from the token
        String accountUsernameId = jwtUtils.getUserNameFromJwtToken(token);

        System.out.println("the account username id extracted from token is " + accountUsernameId);

        Optional<User> user=userRepository.findByUsername(accountUsernameId);
        if(user.isPresent()) {
            return ResponseEntity.ok(pfeService.getAllPfes(searchCriteria,user.get()));
        }else{
            return ResponseEntity.notFound().build();
        }


    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfe(@PathVariable Long id) {
        pfeService.deletePfe(id);
        return ResponseEntity.noContent().build();
    }
}

