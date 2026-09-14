package Prima.api.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Prima.api.Entidades.Facturas;

public interface FacturasRepository extends JpaRepository<Facturas, Long>  {

}
