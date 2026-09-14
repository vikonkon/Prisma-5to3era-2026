package Prima.api.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Prima.api.IService.CompradorIService;
import Prima.api.Entidades.Comprador;
import Prima.api.Repository.CompradorRepository;

@Service
public class CompradorService implements CompradorIService {
    @Autowired
    private CompradorRepository cRepository;
    public List<Comprador> findAllComprador() {
        return cRepository.findAll();
    }
    public Comprador saveComprador(Comprador comprador) {
        return cRepository.save(comprador);
    }
}
