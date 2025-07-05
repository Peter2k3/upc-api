package upc.api.service;

import java.util.Map;

import upc.api.model.User;
import upc.api.model.dto.LoginDTO;
import upc.api.model.dto.UserRegisterDTO;

public interface IAuthService {

    Map<String, String> login(LoginDTO loginDTO) throws Exception;

    User register(UserRegisterDTO user);
}
