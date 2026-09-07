package Prima.api.Entidades;
import java.util.List;
import jakarta.persistence.*;
@Entity
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private float precio;
    private String descripcion;
    private String URLimagen;
    private int cantidad;

    @ManyToMany(mappedBy = "Productos")
    private List<Comprador> compradores;
    public Vendedor() {
    }

}
