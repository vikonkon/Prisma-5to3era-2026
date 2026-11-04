package Prima.api.IService;

import java.util.List;
import Prima.api.Entidades.Facturas;

public interface FacturasIService {
    public List<Facturas> findAllFacturas();
    public Facturas saveFacturas(Facturas facturas);
}
