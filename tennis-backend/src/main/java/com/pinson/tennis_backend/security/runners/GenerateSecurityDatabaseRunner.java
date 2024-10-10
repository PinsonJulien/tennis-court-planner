package com.pinson.tennis_backend.security.runners;

import com.pinson.tennis_backend.roles.dtos.CreateRoleDTO;
import com.pinson.tennis_backend.roles.dtos.RoleDTO;
import com.pinson.tennis_backend.roles.enums.RoleEnum;
import com.pinson.tennis_backend.roles.services.IRoleService;
import com.pinson.tennis_backend.users.dtos.CreateUserDTO;
import com.pinson.tennis_backend.users.dtos.UserDTO;
import com.pinson.tennis_backend.users.services.IUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
public class GenerateSecurityDatabaseRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(GenerateSecurityDatabaseRunner.class);

    private static final RoleEnum SUPER_ADMINISTRATOR_ROLE = RoleEnum.SUPER_ADMIN;
    private static final String DOMAIN = "tennismasters.com";
    private static final int PASSWORD_LENGTH = 255;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // On Application startup, create the roles if they do not exist.
        //this.createDefaultRoles();
        // If no super admin exists, we create one.
        //this.createSuperAdministratorAccount();

        // print all users
        logger.info("All users:");
        final List<UserDTO> users = this.userService.findAll();
        users.forEach(
            user -> {
                logger.info("User: {}", user.username());
                logger.info("Roles: {}", user.roles());
            }
        );

        // print super admin and it's roles

        final RoleDTO superAdminRole = this.roleService.findByName(SUPER_ADMINISTRATOR_ROLE.name());
        final List<UserDTO> superAdmins = this.userService.findUsersByRole(superAdminRole.id());
        superAdmins.forEach(
            superAdmin -> {
                logger.info("Super administrator: {}", superAdmin.username());
                logger.info("Roles: {}", superAdmin.roles());
            }
        );



    }

    private void createDefaultRoles() {
        // Check if the application already have roles in the database.
        if (!this.roleService.findAll().isEmpty())
            return;

        logger.info("Creating default roles...");
        final Set<RoleEnum> roleEnums = EnumSet.allOf(RoleEnum.class);
        final Set<CreateRoleDTO> roles = CreateRoleDTO.from(roleEnums);

        roles.forEach(
            role -> {
                try {
                    if (!this.roleService.existsByName(role.name()))
                        this.roleService.create(role);
                } catch (final Exception e) {
                    logger.error("Cannot create role {}: {}", role.name(), e.getMessage());
                }
            }
        );
    }

    private void createSuperAdministratorAccount() {
        // Creates a super administrator if none exists yet.

        // Check if the super administrator role exists.
        final String roleName = SUPER_ADMINISTRATOR_ROLE.name();
        final RoleDTO superAdministratorRole = this.roleService.findByName(roleName);
        System.out.println("trying to find role: " + roleName);
        final List<UserDTO> superAdministrators = this.userService.findUsersByRole(superAdministratorRole.id());
        System.out.println("super admins: " + superAdministrators);

        if (!superAdministrators.isEmpty())
            return;

        logger.info("Creating super administrator...");

        // If no super administrator exists, we create one.
        // It will have a randomly generated password, which will be output in the console.
        final String password = this.generateRandomPassword();
        final CreateUserDTO superAdministrator = new CreateUserDTO(
            roleName,
            roleName + "@" + DOMAIN,
            password
        );

        try {
            final UserDTO createdUser = this.userService.create(superAdministrator);
            final UserDTO updatedUser = this.userService.addRole(createdUser.id(), superAdministratorRole.id());

            logger.info("--------------------------------------------");
            logger.info("Super administrator created: {}", updatedUser.username());
            logger.info("Password: {}", password);
            logger.info("--------------------------------------------");

        } catch (final Exception e) {
            logger.error("Cannot create super administrator: {}", e.getMessage());
        }
    }

    private String generateRandomPassword() {
        // Generates a random password.
        return RandomStringUtils.randomAlphanumeric(PASSWORD_LENGTH);
    }

}
