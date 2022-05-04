package org.springframework.sandbox.rfc7807;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@GetMapping("/greeting")
	public String greet(@RequestParam String name) {
		return "Hello, " + name;
	}

}
