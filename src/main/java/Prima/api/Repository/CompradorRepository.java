package Prima.api.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Prima.api.Entidades.Comprador;

public interface CompradorRepository extends JpaRepository<Comprador, Long> {
}
