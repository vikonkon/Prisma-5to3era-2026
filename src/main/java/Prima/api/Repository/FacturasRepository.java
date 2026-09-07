package Prima.api.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import api.entidades.Facturas;

public interface FacturasRepository extends JpaRepository<Facturas, Long>  {

}
