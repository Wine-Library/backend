package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        String lowerCaseEmail = email.toLowerCase();
        return userRepository.findByEmail(lowerCaseEmail).orElseThrow(()
                -> new UsernameNotFoundException(
                "User with email " + email + " not found"));
    }
}
