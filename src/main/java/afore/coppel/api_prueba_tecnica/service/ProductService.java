package afore.coppel.api_prueba_tecnica.service;

import afore.coppel.api_prueba_tecnica.model.Product;
import afore.coppel.api_prueba_tecnica.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Recupera y devuelve la lista completa de productos.
     * @return Una lista de objetos Product.
     */
    public List<Product> getAllProducts() {
        // Usamos el metodo findAll() heredado de JpaRepository
        return productRepository.findAll();
    }

    /**
     * Guarda un nuevo producto, validando que el SKU no exista.
     */
    public Product createProduct(Product product) {
        // Validación del SKU único
        if (productRepository.existsBySku(product.getSku())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El SKU '" + product.getSku() + "' ya existe."
            );
        }

        // El metodo save() de JpaRepository hace la inserción.
        return productRepository.save(product);
    }

    /**
     * Obtiene un producto buscando por su SKU.
     * @param sku El SKU del producto a buscar.
     * @return El objeto Product encontrado.
     * Si el producto no existe, lo cual debe ser mapeado a 404.
     */
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new NoSuchElementException("Producto con SKU " + sku + " no encontrado."));
    }

    /**
     * Lógica para actualizar un producto por SKU.
     */
    @Transactional
    public Product updateProduct(String sku, Product productDetails) {

        // 1. Buscar el producto existente por SKU.
        // Si no se encuentra, se lanza una excepción
        // (que idealmente debería ser manejada por un @ControllerAdvice para devolver un 404).
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con SKU: " + sku));

        // 2. Actualizar los campos del producto existente con los nuevos detalles.
        // **IMPORTANTE**: Ajusta estos setters a los campos reales de tu entidad Product.
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setBrand(productDetails.getBrand());
        product.setDescription(productDetails.getDescription());

        // product.setCategory(productDetails.getCategory()); // Ejemplo de otro campo

        // 3. Guardar la entidad actualizada.
        Product updatedProduct = productRepository.save(product);

        // 4. Generar log de auditoría (opcional, si tienes un componente de auditoría)
        // auditService.logUpdate(sku, "Producto actualizado por el usuario actual.");

        return updatedProduct;
    }
}