package Prima.api.IService;
import java.util.List;
import api.Entidades.Comprador;
public interface CompradorIService  {
public List<Comprador> findAllComprador();
public Comprador saveComprador(Comprador comprador);
}
