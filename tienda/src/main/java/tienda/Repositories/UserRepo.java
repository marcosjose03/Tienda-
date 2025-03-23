package tienda.Repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tienda.Models.Usuario;

@Repository // Anotación que indica que esta interfaz es un componente de acceso a datos en
            // Spring.
public interface UserRepo extends JpaRepository<Usuario, Long> {
    
    boolean existsByUsername(String username);

    /**
     * Busca un usuario en la base de datos utilizando su nombre de usuario.
     * 
     * @param username El nombre de usuario del usuario que se desea buscar.
     * @return Un objeto Optional que contiene el usuario si se encuentra, o está
     *         vacío si no se encuentra.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica si un correo electrónico ya está registrado en la base de datos.
     * 
     * @param email El correo electrónico que se desea verificar.
     * @return true si el correo electrónico ya existe; false si no existe.
     */
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}