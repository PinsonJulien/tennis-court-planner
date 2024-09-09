package com.pinson.tennis_backend.security.users;

import com.pinson.tennis_backend.commons.controllers.BaseController;
import com.pinson.tennis_backend.commons.google.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    public UserController() {
        super("1.0");
    }

    // Get all
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAll() {
        final String id = this.generateId();
        final List<UserDTO> users = this.userService.findAll();

        final HttpServletRequest request =

        return ResponseEntity.ok(
            this.generateResponse(

                id,
                    "",
                    users
            )
        );
    }


}
