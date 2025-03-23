package tienda.Service;

import tienda.Models.Usuario;
import tienda.Repositories.UserRepo;
import tienda.Util.JwtUtils;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

    @Override
    public String login(String username, String password) {

        var authToken = new UsernamePasswordAuthenticationToken(username, password);
        var authenticate = authenticationManager.authenticate(authToken);

        return JwtUtils.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername());
    }



    @Override
    public String signUp(String nombre, String username, String password, String email) {
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("El Username ya existe");
        }

        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("El correo electrónico ya existe");
        }

        Usuario user = new Usuario();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setNombre(nombre);
        user.setEmail(email);
        user.setFechaRegistro(LocalDateTime.now());

        user = userRepo.save(user);
        System.out.print("Usuario guardado" + user.getUsername());

        return JwtUtils.generateToken(username);
    }


    @Override
    public String verifyToken(String token) {
        var usernameOptional = JwtUtils.getUsernameFromToken(token);
        if (usernameOptional.isPresent()) {
            return usernameOptional.get();
        }
        throw new RuntimeException("Token invalid");
    }
}
