package com.pinson.tennis_backend.users.controllers;

import com.pinson.tennis_backend.commons.controllers.BaseController;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.users.dtos.UserDTO;
import com.pinson.tennis_backend.users.dtos.requests.AddRoleToUserRequestDTO;
import com.pinson.tennis_backend.users.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public BaseApiResponse<List<UserDTO>> index() {
        final String method = "users.index";
        final List<UserDTO> users = this.userService.findAll();

        return this.createResponse(
            HttpStatus.OK,
            method,
            users
        );
    }

    @GetMapping("/{id}")
    public BaseApiResponse<UserDTO> show(
        @PathVariable final UUID id
    ) {
        final String method = "users.show";
        try {
            final UserDTO user = this.userService.findById(id);

            return this.createResponse(
                HttpStatus.OK,
                method,
                user
            );
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.NOT_FOUND;
            final String domain = "User";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }

    @GetMapping("/deleted")
    public BaseApiResponse<List<UserDTO>> indexDeleted() {
        final String method = "users.index.deleted";
        final List<UserDTO> users = this.userService.findAllDeleted();

        return this.createResponse(
            HttpStatus.OK,
            method,
            users
        );
    }

    @DeleteMapping("/{id}")
    public BaseApiResponse<UserDTO> destroy(
        @PathVariable final UUID id
    ) {
        final String method = "users.destroy";
        try {
            this.userService.delete(id);

            return this.createResponse(
                HttpStatus.NO_CONTENT,
                method,
                null
            );
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.NOT_FOUND;
            final String domain = "User";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }

    @PostMapping("/{id}/restore")
    public BaseApiResponse<UserDTO> restore(
        @PathVariable final UUID id
    ) {
        final String method = "users.restore";
        try {
            final UserDTO user = this.userService.restore(id);

            return this.createResponse(
                HttpStatus.OK,
                method,
                user
            );
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.NOT_FOUND;
            final String domain = "User";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }

    @PostMapping("/{id}/roles")
    public BaseApiResponse<UserDTO> addRole(
        @PathVariable final UUID id,
        @RequestBody final AddRoleToUserRequestDTO body
    ) {
        final String method = "users.roles.add";
        try {
            final UserDTO user = this.userService.addRole(id, body.roleId());

            return this.createResponse(
                HttpStatus.OK,
                method,
                user
            );
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.NOT_FOUND;
            final String domain = "User";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    public BaseApiResponse<UserDTO> removeRole(
        @PathVariable final UUID id,
        @PathVariable final Long roleId
    ) {
        final String method = "users.roles.remove";
        try {
            final UserDTO user = this.userService.removeRole(id, roleId);

            return this.createResponse(
                HttpStatus.OK,
                method,
                user
            );
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.NOT_FOUND;
            final String domain = "User";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }




}
