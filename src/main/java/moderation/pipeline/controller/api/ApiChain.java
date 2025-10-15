package moderation.pipeline.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import moderation.pipeline.controller.input.DataDto;
import moderation.pipeline.service.facade.Facade;

@RestController
@RequestMapping("/chain")
public class ApiChain {

    @Autowired
    private Facade facade;

    @GetMapping("/print")
    public ResponseEntity<String> printChain() {
        try {
            facade.printPipeline();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Printed to console");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/test1")
    public ResponseEntity<Object> checkChain(@RequestBody DataDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(facade.testPipelineII(dto.getData(), dto.getUsername(), dto.getPassword()));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        } finally {
            System.out.println("Request completed");
        }
    }
}
