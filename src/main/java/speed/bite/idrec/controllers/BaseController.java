package speed.bite.idrec.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import speed.bite.idrec.responses.BasicResponse;

import java.util.HashMap;

@RestController
@RequestMapping("/")
@Slf4j
public class BaseController {
    @GetMapping(path = {"", "/", "health", "health/"})
    public ResponseEntity status() {
        return BasicResponse.ok("we are open to get queries.");
    }
}
