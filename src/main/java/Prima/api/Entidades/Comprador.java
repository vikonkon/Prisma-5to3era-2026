package Prima.api.Entidades;
import jakarta.persistence.*;
import java.util.List;
@Entity
public class Comprador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private float compra;

    @ManyToMany(mappedBy = "Comprador")
    private List<Peleador> peleadores;
    public Comprador() {
    }

    public Comprador(Long id, String nombre, float compra) {
    this.id = id;
    this.nombre = nombre;
    this.compra = compra;
    
    }

    public void setId(Long id) {
    this.id = id;
    }
    public void setNombre(String nombre) {
    this.nombre = nombre;
    }

    public void setcompra(Float compra) {
    this.compra= compra;
    }


    public void setPeso(float peso) {
    this.peso = peso;
    }   

    public Long getId() {
    return id;
    }

    public String getNombre() {
    return nombre;
    }

    public Long getBonificadorDanio() {
    return bonificadorDanio;
    }

    public float getPeso() {
    return peso;
    }

}
