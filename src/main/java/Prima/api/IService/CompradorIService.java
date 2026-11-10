package Prima.api.IService;
import java.util.List;
import Prima.api.Entidades.Comprador;

public interface CompradorIService  {
public List<Comprador> findAllComprador();
public Comprador saveComprador(Comprador comprador);
public Comprador buscarPorCorreo(String correo);
public String iniciarSesion(Comprador comprador);
public Comprador compradorDeToken(String authorization);
public boolean esGerente(Comprador comprador);
}
