package com.pinson.tennis_backend.roles.controllers;

import com.pinson.tennis_backend.commons.controllers.BaseController;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.roles.dtos.RoleDTO;
import com.pinson.tennis_backend.roles.services.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private IRoleService roleService;

    @GetMapping("/")
    public BaseApiResponse<List<RoleDTO>> index() {
        final String method = "roles.index";
        final List<RoleDTO> roles = this.roleService.findAll();

        return this.createResponse(
            HttpStatus.OK,
            method,
            roles
        );
    }

}
