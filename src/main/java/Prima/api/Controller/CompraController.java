package Prima.api.Controller;

import Prima.api.Entidades.Compra;
import Prima.api.Entidades.Comprador;
import Prima.api.Service.CompraService;
import Prima.api.IService.CompradorIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private static final Set<String> TIPOS_DE_PAGO = Set.of("Tarjeta de crédito", "Tarjeta de débito");
    private static final Set<String> MARCAS = Set.of("Visa", "Mastercard", "American Express");

    @Autowired
    private CompraService compraService;
    @Autowired
    private CompradorIService compradorService;

    @PostMapping("/nueva")
    public ResponseEntity<?> crearCompra(@RequestHeader(value = "Authorization", required = false) String auth,
                                         @RequestBody Map<String, Object> datos) {
        Comprador comprador = compradorService.compradorDeToken(auth);
        if (comprador == null) {
            return ResponseEntity.status(401).body("Tenés que iniciar sesión para comprar");
        }
        // Lista de ids de prendas (un id repetido = una unidad más)
        List<Long> ids = new ArrayList<>();
        if (datos.get("productosIds") instanceof List<?> lista) {
            for (Object o : lista) {
                if (o instanceof Number n) ids.add(n.longValue());
                else return ResponseEntity.badRequest().body("Hay un producto inválido en el carrito");
            }
        }

        // Medio de pago: del navegador solo llegan el tipo, la marca y los últimos 4 dígitos (nunca el número completo ni el código)
        String tipo = String.valueOf(datos.get("medioPago"));
        String ultimos4 = String.valueOf(datos.get("ultimos4"));
        String marca = String.valueOf(datos.get("marca"));
        if (!TIPOS_DE_PAGO.contains(tipo) || !ultimos4.matches("\\d{4}")) {
            return ResponseEntity.badRequest().body("Completá los datos de pago");
        }
        String medioPago = tipo + (MARCAS.contains(marca) ? " " + marca : "") + " terminada en " + ultimos4;

        try {
            Compra guardada = compraService.registrarCompra(ids, comprador, medioPago);
            // Devolvemos solo el resumen (así no se filtran datos del comprador)
            return ResponseEntity.ok(Map.of(
                    "id", guardada.getId(),
                    "facturaId", guardada.getFactura().getId(),
                    "subtotal", guardada.getSubtotal(),
                    "iva", guardada.getIva(),
                    "total", guardada.getTotal()));
        } catch (RuntimeException e) {
            // Si algo falla devolvemos el error 
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
