package summitmail.controllers;

import org.apache.coyote.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import summitmail.models.*;
import summitmail.services.CustomerService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        response.put("customer", createdCustomer);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") String id) {
        Map response = new HashMap<String, Object>();
        Customer customer = service.getCustomer(id);
        if (customer == null) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); }
        response.put("customer", customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity getCustomers(
            @PathVariable(name = "resultsPerPage", required = false) int resultsPerPage,
            @PathVariable(name = "page", required = false) int page) {
        Map response = new HashMap<String, Map<String, ?>>();
        int results = (Integer)resultsPerPage == 0 ? 20 : resultsPerPage;
        int pageCount = (Integer)page == 0 ? 0 : page;
        Map<String, ?> customers = service.getCustomers(resultsPerPage, page);
        if (customers.isEmpty()) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); }
        response.put("customers", customers);
        return ResponseEntity.ok(response);
    }

    @Override
    ResponseEntity<Map> index() {
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
