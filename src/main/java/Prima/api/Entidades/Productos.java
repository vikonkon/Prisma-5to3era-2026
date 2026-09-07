package Prima.api.Entidades;
import java.util.List;
import jakarta.persistence.*;
@Entity
public class Productos {
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
    public Productos() {
    }
    
    
    public Productos(long id, String nombre, float precio, String descripcion, String uRLimagen, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        URLimagen = uRLimagen;
        this.cantidad = cantidad;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public float getPrecio() {
        return precio;
    }
    public void setPrecio(float precio) {
        this.precio = precio;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getURLimagen() {
        return URLimagen;
    }
    public void setURLimagen(String uRLimagen) {
        URLimagen = uRLimagen;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    
}

