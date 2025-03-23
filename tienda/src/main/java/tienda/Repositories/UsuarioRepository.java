package tienda.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import tienda.Models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
