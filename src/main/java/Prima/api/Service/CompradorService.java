package Prima.api.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Prima.api.IService.CompradorIService;
import Prima.api.Entidades.Comprador;
import Prima.api.Repository.CompradorRepository;

@Service
public class CompradorService implements CompradorIService {
    @Autowired
    private CompradorRepository cRepository;

    // Sesiones iniciadas (token -> id del comprador). Viven en memoria: si se reinicia Spring Boot hay que volver a ingresar.
    private final Map<String, Long> sesiones = new ConcurrentHashMap<>();

    public List<Comprador> findAllComprador() {
        return cRepository.findAll();
    }
    public Comprador saveComprador(Comprador comprador) {
        return cRepository.save(comprador);
    }
    public Comprador buscarPorCorreo(String correo) {
        return cRepository.findByCorreo(correo);
    }

    // Se llama al hacer login: crea un token para ese comprador
    public String iniciarSesion(Comprador comprador) {
        String token = UUID.randomUUID().toString();
        sesiones.put(token, comprador.getId());
        return token;
    }

    // Recibe el header "Authorization: Bearer <token>" y devuelve el comprador (o null si no hay sesión)
    public Comprador compradorDeToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) return null;
        Long id = sesiones.get(authorization.substring(7).trim());
        if (id == null) return null;
        return cRepository.findById(id).orElse(null);
    }

    // El rol se lee de la base de datos, sin importar mayúsculas ("gerente", "GERENTE"...)
    public boolean esGerente(Comprador comprador) {
        return comprador != null
                && comprador.getRol() != null
                && "GERENTE".equalsIgnoreCase(comprador.getRol().trim());
    }
}
