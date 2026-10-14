package Prima.api.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Prima.api.IService.CompradorIService;
import Prima.api.Entidades.Comprador;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comprador")
public class CompradorController {
@Autowired
private CompradorIService aService;
@GetMapping
public List<Comprador> getAllComprador() {
return aService.findAllComprador();
}
@PostMapping
public Comprador createComprador(@RequestBody Comprador comprador) {
return aService.saveComprador(comprador);
}
}