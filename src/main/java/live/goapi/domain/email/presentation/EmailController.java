package live.goapi.domain.email.presentation;

import live.goapi.domain.email.presentation.dto.request.EmailSentDto;
import live.goapi.domain.email.service.EmailCheckService;
import live.goapi.domain.email.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailSendService emailSendService;
    private final EmailCheckService emailCheckService;

    @PostMapping(value = "/send" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailSentDto emailSentDto) {
        emailSendService.sendEmail(emailSentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> mailCheck(@Email @RequestParam String email, @RequestParam String authKey) {
        emailCheckService.checkEmail(email, authKey);
        return ResponseEntity.ok().build();
    }
}
