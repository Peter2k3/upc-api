package upc.api.service;

import mx.ccgsgroup.ccgs_backend_api.dto.LoginDTO;
import mx.ccgsgroup.ccgs_backend_api.entity.User;

import java.util.Map;

public interface IAuthService {

    Map<String, String> login(LoginDTO loginDTO) throws Exception;

    User register(User user);
}
