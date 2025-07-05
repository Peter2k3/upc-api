package upc.api.model.dto;

import lombok.Data;
import upc.api.model.Role;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserRegisterDTO {

    private String name;
    private String lastname;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();

}
