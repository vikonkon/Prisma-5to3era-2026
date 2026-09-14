package Prima.api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Prima.api.IService.ProductosIService;
import Prima.api.Entidades.Productos;

@RestController
@RequestMapping("/api/productos")

public class ProductosController {
    @Autowired
    private ProductosIService aService;

    @GetMapping
    public List<Productos> getAllProductos() {
        return aService.findAllProductos();
    }

    @PostMapping
    public Productos createProductos(@RequestBody Productos productos) {
        return aService.saveProductos(productos);
    }
}