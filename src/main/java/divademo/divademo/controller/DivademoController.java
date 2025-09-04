package divademo.divademo.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import divademo.divademo.model.Greeting;

@RestController
@RequestMapping("/greetings")
public class DivademoController {

    private final Map<Integer, Greeting> greetingRepo = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<Greeting> getAllGreeting() {
        return greetingRepo.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Greeting> getGreetingById(@PathVariable int id) {
        Greeting greeting = greetingRepo.get(id);
        if (greeting == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(greeting);
    }

    @PostMapping
    public Greeting createGreeting(@RequestBody Greeting greeting) {
        greeting.setId(id++);
        greetingRepo.put(greeting.getId(), greeting);
        return greeting;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Greeting> updateGreetingMessageById(@PathVariable int id, @RequestBody Greeting updatedGreeting) {
        Greeting greeting = greetingRepo.get(id);
        if (greeting == null)
            ResponseEntity.notFound().build();

        greeting.setMessage(updatedGreeting.getMessage());
        greetingRepo.put(id, greeting);
        return ResponseEntity.ok(greeting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Greeting> deleteGreetingsById(@PathVariable int id) {
        Greeting greeting = greetingRepo.get(id);
        if (greeting == null)
            return ResponseEntity.notFound().build();
        
        greetingRepo.remove(id);
        return ResponseEntity.noContent().build();
    }
}
