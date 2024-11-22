package ru.ostritsov.projectTSM.service;

import ru.ostritsov.projectTSM.model.Role;

public interface RoleService {
    Role findByName(String name);
}
