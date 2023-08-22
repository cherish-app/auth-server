package com.saurs.config;

import com.saurs.enums.RoleEnum;
import com.saurs.models.Role;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class InitRoles {

    private static final Logger LOGGER = Logger.getLogger(InitRoles.class.getName());

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Initializing roles...");
        if (Role.count() == 0) {
            Role admin = new Role(RoleEnum.ADMIN);
            Role user = new Role(RoleEnum.USER);
            Role.save(admin);
            Role.save(user);
            LOGGER.info("Roles initialized.");
        } else {
            LOGGER.info("Roles already initialized. Skipping...");
        }
    }
}
