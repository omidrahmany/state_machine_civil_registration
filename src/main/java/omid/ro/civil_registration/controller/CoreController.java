package omid.ro.civil_registration.controller;

import lombok.RequiredArgsConstructor;
import omid.ro.civil_registration.dto.OutputMessage;
import omid.ro.civil_registration.dto.RegisterInputDto;
import omid.ro.civil_registration.entities.Person;
import omid.ro.civil_registration.services.GeneralService;
import omid.ro.civil_registration.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("core")
@RequiredArgsConstructor
public class CoreController {

    private final PersonService service;
    private final GeneralService generalService;

    @PostMapping
    public Person persist(@RequestBody Person person) {
        return service.insertPerson(person);
    }

    @GetMapping("/{id}")
    public Person get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/fire-event/{id}")
    public ResponseEntity<OutputMessage> fireEvent(@PathVariable Long id) {
        return generalService.giveReward(id);
    }

    @PutMapping("/register")
    public ResponseEntity<OutputMessage> register(@RequestBody RegisterInputDto dto) {
        return generalService.register(dto);
    }

}
