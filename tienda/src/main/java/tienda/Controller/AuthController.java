package tienda.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tienda.Service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        try {
                // Llama al servicio para autenticar al usuario y generar un token JWT
            var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
    
                // Crea un objeto de respuesta con el token y el estado de éxito
            var authResponseDto = new AuthResponseDto(
                    jwtToken,
                    AuthStatus.LOGIN_SUCCESS,
                    "Inicio de sesion exitoso"
            );
            
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(authResponseDto);
    
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            AuthStatus status = AuthStatus.LOGIN_FAILED;
    
            if (errorMessage.contains("Usuario no encontrado")) {
                errorMessage = "Usuario no encontrado";
            } else if (errorMessage.contains("La cuenta no ha sido verificada")) {
                errorMessage = "La cuenta no ha sido verificada. Por favor, revise su correo electrónico.";
            } else if (errorMessage.contains("Bad credentials")) {
                errorMessage = "Usuario o contraseña incorrectos";
            }
    
            var authResponseDto = new AuthResponseDto(null, status, errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponseDto);
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponseDto> signUp(@RequestBody AuthRequestDto authRequestDto) {
        try {
            var jwtToken = authService.signUp(authRequestDto.nombre(), authRequestDto.username(),
                    authRequestDto.password(), authRequestDto.email());
            var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY, "Usuario creado correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
        } catch (Exception e) {

            String errorMessage =e.getMessage();
            AuthStatus status =AuthStatus.USER_NOT_CREATED;
            e.printStackTrace();

            if (e.getMessage().contains("Username already exists ")){
                errorMessage = "El nombre de usuario ya esta en uso";
            }else if (e.getMessage().contains("Email already exists")){
                errorMessage = "El correo electronico ya esta registrado";
            }

            var authResponseDto = new AuthResponseDto(null,status,errorMessage);
            
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(authResponseDto);
        }

    }
}