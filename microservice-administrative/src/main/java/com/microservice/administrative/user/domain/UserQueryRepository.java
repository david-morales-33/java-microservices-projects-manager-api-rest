package com.microservice.administrative.user.domain;


import java.util.List;
import java.util.Optional;

import com.microservice.administrative.shared.domain.UserId;

public interface UserQueryRepository {
    Optional<User> find(UserId userId);

    List<User> searchAll();
}
