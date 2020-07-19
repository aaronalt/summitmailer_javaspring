package summitmail.controllers;

import org.apache.coyote.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import summitmail.models.*;
import summitmail.services.CustomerService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/customer")
@RestController
public class CustomerController extends ApiController {

    @Autowired
    CustomerService service;

    public CustomerController() {
        super();
    }

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody CustomerRegistry registry) {
        Map response = new HashMap<String, Object>();
        Customer createdCustomer = service.createCustomer(registry, response);
        if (createdCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("info", createdCustomer);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") String id) {
        Map response = new HashMap<String, Object>();
        Customer customer = service.getCustomer(id);
        if (customer.isEmpty()) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); }
        else {
            response.put("info", customer);
            return ResponseEntity.ok(response);
        }
    }

    @Override
    ResponseEntity<Map> index() {
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
