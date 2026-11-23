package Prima.api.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Prima.api.Entidades.Comprador;
import Prima.api.IService.CompradorIService;
import Prima.api.IService.FacturasIService;

// Las facturas se crean solas al comprar (CompraService). Acá solo se consultan.
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/facturas")
public class FacturasController {
    @Autowired
    private FacturasIService aService;
    @Autowired
    private CompradorIService compradorService;

    // El gerente ve todas las facturas; un cliente solo las suyas
    @GetMapping
    public ResponseEntity<?> getFacturas(@RequestHeader(value = "Authorization", required = false) String auth) {
        Comprador c = compradorService.compradorDeToken(auth);
        if (c == null) return ResponseEntity.status(401).body("Iniciá sesión de nuevo");
        Long filtro = compradorService.esGerente(c) ? null : c.getId();
        return ResponseEntity.ok(aService.resumenes(filtro));
    }

    // Una factura completa: la puede ver el gerente o el cliente que la hizo
    @GetMapping("/{id}")
    public ResponseEntity<?> getFactura(@RequestHeader(value = "Authorization", required = false) String auth,
                                        @PathVariable Long id) {
        Comprador c = compradorService.compradorDeToken(auth);
        if (c == null) return ResponseEntity.status(401).body("Iniciá sesión de nuevo");
        Map<String, Object> factura = aService.detalle(id);
        if (factura == null) return ResponseEntity.status(404).body("La factura no existe");
        Object dueno = factura.remove("compradorId");
        if (!compradorService.esGerente(c) && !c.getId().equals(dueno)) {
            return ResponseEntity.status(403).body("Esta factura es de otra cuenta");
        }
        return ResponseEntity.ok(factura);
    }
}
