package Prima.api.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Prima.api.IService.CompraIService;
import Prima.api.Entidades.Compra;

@RestController
@RequestMapping("/api/compra")

public class CompraController {
    @Autowired
    private CompraIService aService;

    @GetMapping
    public List<Compra> getAllCompras() {
        return aService.findAllCompras();
    }

    @PostMapping
    public Compra createCompra(@RequestBody Compra compra) {
        return aService.saveCompra(compra);
    }
}

