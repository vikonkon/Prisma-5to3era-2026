package Prima.api.Controller;

import Prima.api.Entidades.Compra;
import Prima.api.Entidades.Comprador;
import Prima.api.Service.CompraService;
import Prima.api.IService.CompradorIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;
    @Autowired
    private CompradorIService compradorService;

    @PostMapping("/nueva")
    public ResponseEntity<?> crearCompra(@RequestHeader(value = "Authorization", required = false) String auth,
                                         @RequestBody Map<String, List<Long>> datos) {
        Comprador comprador = compradorService.compradorDeToken(auth);
        if (comprador == null) {
            return ResponseEntity.status(401).body("Tenés que iniciar sesión para comprar");
        }
        try {
            Compra guardada = compraService.registrarCompra(datos.get("productosIds"), comprador);
            // Devolvemos solo el resumen (así no se filtran datos del comprador)
            return ResponseEntity.ok(Map.of(
                    "id", guardada.getId(),
                    "subtotal", guardada.getSubtotal(),
                    "iva", guardada.getIva(),
                    "total", guardada.getTotal()));
        } catch (RuntimeException e) {
            // Si algo falla (ej. sin stock), devolvemos el error al frontend
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
