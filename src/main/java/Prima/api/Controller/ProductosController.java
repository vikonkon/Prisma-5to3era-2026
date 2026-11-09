package Prima.api.Controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Prima.api.Entidades.Comprador;
import Prima.api.Entidades.Productos;
import Prima.api.IService.CompradorIService;
import Prima.api.IService.ProductosIService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/productos")
public class ProductosController {
    @Autowired
    private ProductosIService aService;
    @Autowired
    private CompradorIService compradorService;

    private static final Set<String> CATEGORIAS = Set.of("remeras", "pantalones", "abrigos", "calzado");

    // Cualquiera puede ver el catálogo (sin los productos eliminados)
    @GetMapping
    public List<Productos> getAllProductos() {
        return aService.findAllProductos();
    }

    // ---------- Solo GERENTE ----------

    // Agregar un producto nuevo
    @PostMapping
    public ResponseEntity<?> createProductos(@RequestHeader(value = "Authorization", required = false) String auth,
                                             @RequestBody Map<String, Object> datos) {
        ResponseEntity<?> denegado = exigirGerente(auth);
        if (denegado != null) return denegado;
        Productos nuevo = new Productos(); // id 0 = producto nuevo (nunca pisa uno existente)
        String error = cargarDatos(nuevo, datos);
        if (error != null) return ResponseEntity.badRequest().body(error);
        nuevo.setActivo(true);
        return ResponseEntity.ok(aService.saveProductos(nuevo));
    }

    // Editar un producto existente (nombre, precio, descripción, imagen, categoría y stock)
    @PutMapping("/{id}")
    public ResponseEntity<?> editarProducto(@RequestHeader(value = "Authorization", required = false) String auth,
                                            @PathVariable Long id,
                                            @RequestBody Map<String, Object> datos) {
        ResponseEntity<?> denegado = exigirGerente(auth);
        if (denegado != null) return denegado;
        Productos existente = aService.findById(id).orElse(null);
        if (existente == null) return ResponseEntity.status(404).body("El producto no existe");
        String error = cargarDatos(existente, datos);
        if (error != null) return ResponseEntity.badRequest().body(error);
        return ResponseEntity.ok(aService.saveProductos(existente));
    }

    // Renovar stock: suma unidades al stock actual. Body: {"cantidad": 10}
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> reponerStock(@RequestHeader(value = "Authorization", required = false) String auth,
                                          @PathVariable Long id,
                                          @RequestBody Map<String, Integer> body) {
        ResponseEntity<?> denegado = exigirGerente(auth);
        if (denegado != null) return denegado;
        Integer unidades = body.get("cantidad");
        if (unidades == null || unidades <= 0) {
            return ResponseEntity.badRequest().body("Ingresá una cantidad mayor a 0");
        }
        try {
            return ResponseEntity.ok(aService.reponerStock(id, unidades));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // "Eliminar": el producto deja de aparecer en la tienda. No se borra de la base de datos
    // para no romper las compras y facturas que ya lo incluyen.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@RequestHeader(value = "Authorization", required = false) String auth,
                                              @PathVariable Long id) {
        ResponseEntity<?> denegado = exigirGerente(auth);
        if (denegado != null) return denegado;
        Productos existente = aService.findById(id).orElse(null);
        if (existente == null) return ResponseEntity.status(404).body("El producto no existe");
        existente.setActivo(false);
        aService.saveProductos(existente);
        return ResponseEntity.ok("Producto eliminado");
    }

    // ---------- Ayudas ----------

    // Devuelve null si el que pide es gerente; si no, la respuesta de error que corresponde
    private ResponseEntity<?> exigirGerente(String auth) {
        Comprador c = compradorService.compradorDeToken(auth);
        if (c == null) return ResponseEntity.status(401).body("Iniciá sesión de nuevo");
        if (!compradorService.esGerente(c)) return ResponseEntity.status(403).body("Solo el gerente puede hacer esto");
        return null;
    }

    // Copia los datos que llegan del formulario al producto. Devuelve null si está todo bien o el mensaje de error.
    private String cargarDatos(Productos p, Map<String, Object> d) {
        String nombre = texto(d.get("nombre")).trim();
        if (nombre.isEmpty()) return "El nombre es obligatorio";

        float precio;
        int cantidad;
        try {
            precio = (float) Double.parseDouble(texto(d.get("precio")));
            cantidad = (int) Double.parseDouble(texto(d.get("cantidad")));
        } catch (NumberFormatException e) {
            return "El precio y el stock tienen que ser números";
        }
        if (precio <= 0) return "El precio tiene que ser mayor a 0";
        if (cantidad < 0) return "El stock no puede ser negativo";

        String categoria = texto(d.get("categoria")).trim().toLowerCase();
        if (!CATEGORIAS.contains(categoria)) return "Elegí una categoría: remeras, pantalones, abrigos o calzado";

        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setDescripcion(texto(d.get("descripcion")).trim());
        p.setURLimagen(texto(d.get("urlimagen")).trim());
        p.setCantidad(cantidad);
        p.setCategoria(categoria);
        return null;
    }

    private String texto(Object valor) {
        return valor == null ? "" : valor.toString();
    }
}
