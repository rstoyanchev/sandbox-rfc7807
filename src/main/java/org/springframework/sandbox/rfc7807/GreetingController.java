package org.springframework.sandbox.rfc7807;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.sandbox.rfc7807.exception.InvalidNameException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final Pattern ONLY_ALPHABETS_PATTERN = Pattern.compile("[A-z]+");

    @GetMapping("/greeting")
    public String greet(@RequestParam String name) {
        this.validateName(name);
        return "Hello, " + name;
    }

    private void validateName(String name) {
        Matcher m = ONLY_ALPHABETS_PATTERN.matcher(name);
        if (name.isBlank()) {
            throw new InvalidNameException("Name parameter cannot be blank");
        }
        if (!m.matches()) {
            throw new InvalidNameException("Name should have only Alphabets");
        }
    }

}
