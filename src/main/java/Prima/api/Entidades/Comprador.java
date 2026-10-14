package Prima.api.Entidades;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Comprador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String direccion;
    private int cuit;

    @OneToMany(mappedBy = "comprador")
    private List<Compra> compras;
    @ManyToMany
    @JoinTable(
    name = "comprador_producto",
    joinColumns = @JoinColumn(name = "comprador_id"),
    inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Productos> productos;


    public List<Productos> getProductos() {
        return productos;
    }

public void setProductos(List<Productos> productos) {
    this.productos = productos;
}

    public Comprador(Long id, String nombre, String apellido, String correo, String contrasena, String direccion,
            int cuit, List<Compra> compras) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.direccion = direccion;
        this.cuit = cuit;
        this.compras = compras;
    }

    public Comprador(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCuit() {
        return cuit;
    }

    public void setCuit(int cuit) {
        this.cuit = cuit;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }
    
}
