package Prima.api.IService;

import java.util.List;
import Prima.api.Entidades.Productos;

public interface ProductosIService {
    public List<Productos> findAllProductos();
    public Productos saveProductos(Productos productos);
}

