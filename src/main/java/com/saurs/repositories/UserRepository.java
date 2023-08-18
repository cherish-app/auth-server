package com.saurs.repositories;

import com.saurs.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;

public class UserRepository implements PanacheRepositoryBase<User, UUID> {
}
