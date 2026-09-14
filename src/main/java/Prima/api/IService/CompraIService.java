package Prima.api.IService;

import java.util.List;
import Prima.api.Entidades.Compra;

public interface CompraIService {
    public List<Compra> findAllCompras();
    public Compra saveCompra(Compra compra);
}