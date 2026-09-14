package Prima.api.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Prima.api.IService.CompraIService;
import Prima.api.Entidades.Compra;
import Prima.api.Repository.CompraRepository;

@Service
public class CompraService implements CompraIService {
    @Autowired
    private CompraRepository cRepository;
    public List<Compra> findAllCompras() {
        return cRepository.findAll();
    }
    public Compra saveCompra(Compra compra) {
        return cRepository.save(compra);
    }
}