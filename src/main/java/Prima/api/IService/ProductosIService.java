package Prima.api.IService;

import java.util.List;
import java.util.Optional;
import Prima.api.Entidades.Productos;

public interface ProductosIService {
    public List<Productos> findAllProductos();
    public Productos saveProductos(Productos productos);
    public Optional<Productos> findById(Long id);
    public Productos reponerStock(Long id, int unidades);
}
