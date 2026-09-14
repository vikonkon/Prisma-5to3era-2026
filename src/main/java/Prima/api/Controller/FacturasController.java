package Prima.api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Prima.api.IService.FacturasIService;
import Prima.api.Entidades.Facturas;

@RestController
@RequestMapping("/api/facturas")

public class FacturasController {
    @Autowired
    private FacturasIService aService;

    @GetMapping
    public List<Facturas> getAllFacturas() {
        return aService.findAllFacturas();
    }

    @PostMapping
    public Facturas createFacturas(@RequestBody Facturas facturas) {
        return aService.saveFacturas(facturas);
    }
}