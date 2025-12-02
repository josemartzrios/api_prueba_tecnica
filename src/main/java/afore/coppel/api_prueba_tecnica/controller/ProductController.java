package afore.coppel.api_prueba_tecnica.controller;

import afore.coppel.api_prueba_tecnica.model.Product;
import afore.coppel.api_prueba_tecnica.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products") // Mapea la ruta base /products
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /products
     * Lista todos los productos disponibles.
     * respuesta '200'
     */
    @GetMapping // Mapea las solicitudes GET a /products
    public ResponseEntity<List<Product>> listAllProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            // Si la lista está vacía, se podría devolver 404 Not Found,
            // pero el estándar de APIs REST sugiere devolver 200 OK con lista vacía ([]).
            // Para el propósito del ejercicio, devolveremos 200 OK, cumpliendo con la definición
            // de que el endpoint está accesible.
            return ResponseEntity.ok(products);
        }

        // Devuelve 200 OK con el cuerpo de la lista de productos
        return ResponseEntity.ok(products);
    }

    /**
     * POST /products
     * Crea un nuevo producto. Requiere rol ADMIN.
     * @return 201 Created con el objeto de producto.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // La validación de seguridad (rol ADMIN) la maneja Spring Security antes de llegar aquí.

        Product newProduct = productService.createProduct(product);

        // Devuelve 201 Created
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }


    /**
     * Endpoint GET /products/{sku}
     * Obtiene el producto por SKU. Accesible para todos (permitAll en SecurityConfig).
     * Mapea la excepción a 404.
     */
    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        try {
            Product product = productService.getProductBySku(sku);
            return ResponseEntity.ok(product);
        } catch (NoSuchElementException e) {
            // Si el servicio lanza NoSuchElementException, devolvemos un 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // Manejo de otros errores internos, devolviendo 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /products/{sku}
     * Actualiza un producto existente. Requiere el rol ADMIN.
     */
    @PutMapping("/{sku}")
    // Anotación para asegurar que solo los usuarios con el rol 'ADMIN' puedan acceder.
    // Esto es crucial para cumplir con tu especificación de seguridad.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String sku,
            @RequestBody Product productDetails) {

        // El SKU del path debería coincidir con el SKU del cuerpo,
        // pero por seguridad usamos el del path para la búsqueda.
        Product updatedProduct = productService.updateProduct(sku, productDetails);

        // Retorna el producto actualizado con el estado 200 OK
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}