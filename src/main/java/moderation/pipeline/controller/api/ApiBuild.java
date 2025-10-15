package moderation.pipeline.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import moderation.pipeline.service.facade.Facade;

@RestController
@RequestMapping("/build")
public class ApiBuild {
    @Autowired
    private Facade facade;

    @PostMapping("/single")
    public ResponseEntity<String> build(@RequestParam String moderatorName) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(facade.appendBlock(moderatorName));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/single/front")
    public ResponseEntity<String> buildAtHead(@RequestParam String moderatorName) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(facade.appendBlockFront(moderatorName));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/pipe")
    public ResponseEntity<String> buildPipeline(@RequestParam String types) {
        try {
            String[] arr = types.split(",");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(facade.buildPipeline(java.util.Arrays.asList(arr)));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }
}
