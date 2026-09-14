package Prima.api.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Prima.api.IService.ProductosIService;
import Prima.api.Entidades.Productos;
import Prima.api.Repository.ProductosRepository;

@Service
public class ProductosService implements ProductosIService{
    @Autowired
    private ProductosRepository pRepository;
    public List<Productos> findAllProductos() {
        return pRepository.findAll();
    }
    public Productos saveProductos(Productos producto) {
        return pRepository.save(producto);
    }

}
