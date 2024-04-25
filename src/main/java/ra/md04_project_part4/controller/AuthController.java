package ra.md04_project_part4.controller;

import ra.md04_project_part4.model.dto.request.FormLogin;
import ra.md04_project_part4.model.dto.request.FormRegister;
import ra.md04_project_part4.model.dto.response.JWTResponse;
import ra.md04_project_part4.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
public class AuthController {
    @Autowired
    private IUserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<JWTResponse> doLogin(@RequestBody FormLogin formLogin) {
        return new ResponseEntity<>(userService.login(formLogin), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> doRegister(@RequestBody FormRegister formRegister) {
        boolean check = userService.register(formRegister);
        if (check) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Account create successfully");
            return new ResponseEntity<>(map, HttpStatus.CREATED);
        } else {
            throw new RuntimeException("something is error");
        }
    }
}
