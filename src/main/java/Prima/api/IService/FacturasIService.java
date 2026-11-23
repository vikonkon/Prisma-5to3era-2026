package Prima.api.IService;

import java.util.List;
import java.util.Map;
import Prima.api.Entidades.Facturas;

public interface FacturasIService {
    public List<Facturas> findAllFacturas();
    public Facturas saveFacturas(Facturas facturas);
    // Lista resumida de facturas (null = todas; un id = solo las de ese comprador)
    public List<Map<String, Object>> resumenes(Long compradorId);
    // Factura completa lista para mostrar (null si no existe)
    public Map<String, Object> detalle(Long id);
}
