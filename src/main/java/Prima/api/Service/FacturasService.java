package Prima.api.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Prima.api.IService.FacturasIService;
import Prima.api.Entidades.Facturas;
import Prima.api.Repository.FacturasRepository;

@Service
public class FacturasService implements FacturasIService {
    @Autowired
    private FacturasRepository fRepository;
    public List<Facturas> findAllFacturas() {
        return fRepository.findAll();
    }
    public Facturas saveFacturas(Facturas facturas) {
        return fRepository.save(facturas);
    }
}