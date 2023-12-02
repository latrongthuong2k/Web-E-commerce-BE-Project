package com.ecommerce.myapp.Services.Implement;

import com.ecommerce.myapp.DTO.Category.ReqCreateCategory;
import com.ecommerce.myapp.DTO.Mapper.ProductMapper;
import com.ecommerce.myapp.DTO.Product.*;
import com.ecommerce.myapp.DTO.Product.SizesDto;
import com.ecommerce.myapp.DTO.Product.SupplierDto;
import com.ecommerce.myapp.Entity.Bill.Supplier;
import com.ecommerce.myapp.Entity.ProductConnectEntites.*;
import com.ecommerce.myapp.Exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.Repositories.Product.*;
import com.ecommerce.myapp.Services.CategoryService;
import com.ecommerce.myapp.Services.ProductService;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.s3.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final SuppliersRepository supplierRepository;
    private final CategoryService categoryService;
    private final ClientTypeRepository clientTypeRepository;
    private final ColorsRepository colorRepository;
    private final SizesRepository sizeRepository;
    private final TagsRepository tagRepository;
    private final ProductMapper productMapper;
    private final TermImageRepository termImageRepository;
    private final InventoryRepository inventoryRepository;

    // Services

    @Override
    public Product addNewProduct(ReqProductDetailDTO newProduct) {
        // find in repository
        Category category = categoryService.getCategory(newProduct.category());
        Supplier supplier = supplierRepository.findById(newProduct.supplier())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier id " + newProduct.supplier() + " is not found"));

        Inventory inventory = inventoryRepository.findById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id " + 1 + " is not found"));
        inventory.setStockQuantity(newProduct.stockQuantity());
        List<ClientType> clientType = clientTypeRepository.findAllById(newProduct.clientTypes()
                .stream().map(ClientTypeDto::id).toList());
        List<Colors> colors = colorRepository.findAllById(newProduct.colors()
                .stream().map(ColorsDto::id).toList());
        List<Sizes> sizes = sizeRepository.findAllById(newProduct.sizes()
                .stream().map(SizesDto::id).toList());
        List<Tags> tags = tagRepository.findAllById(newProduct.tags()
                .stream().map(TagsDto::id).toList());
//        List<ClientType> clientType = clientTypeRepository.findAllById(newProduct.clientTypes());
//        List<Colors> colors = colorRepository.findAllById(newProduct.colors());
//        List<Sizes> sizes = sizeRepository.findAllById(newProduct.sizes());
//        List<Tags> tags = tagRepository.findAllById(newProduct.tags());

        Product product = Product
                .builder()
                .productName(newProduct.productName())
                .description(newProduct.description())
                .price(newProduct.price())
                .category(category)
                .supplier(supplier)
                .clientType(clientType)
                .colors(colors)
                .sizes(sizes)
                .tags(tags)
                .status(0)
                .inventory(inventory)
                .build();
        return productRepository.save(product);
    }

    private Product foundProduct(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "product id [%s] is not found".formatted(productId)
        ));
    }

    @Override
    public Product foundProductByName(String productName) {
        return productRepository.findByProductName(productName).orElseThrow(() -> new ResourceNotFoundException(
                "user with id [%s] not found".formatted(productName)
        ));
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "user with id [%s] not found".formatted(productId)
        ));
    }

    @Override
    public ResProductDetailDTO getProductDetailById(Integer productId) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
                    "user with id [%s] not found".formatted(productId)
            ));
            List<ColorsDto> colors = colorRepository.findByProductId(productId);
            List<SizesDto> sizes = sizeRepository.findByProductId(productId);
            List<TagsDto> tags = tagRepository.findByProductId(productId);
            List<ClientTypeDto> clientTypes = clientTypeRepository.findByProductId(productId);
            return new ResProductDetailDTO(
                    product.getId(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getInventory().getStockQuantity(),
                    product.getCategory().getId(),
                    colors,
                    sizes,
                    tags,
                    clientTypes,
//                    product.getColors().stream().map(Colors::getId).toList(),
//                    product.getSizes().stream().map(Sizes::getId).toList(),
//                    product.getTags().stream().map(Tags::getId).toList(),
//                    product.getClientType().stream().map(ClientType::getId).toList(),
                    product.getSupplier().getId(),
                    product.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Trả về toàn bộ product
    @Override
    public List<ProductBasicInfoDTO> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toDto).toList();
    }


    // Lấy 1 trang các sản phẩm tìm được
    @Override
    public Page<ProductBasicInfoDTO> getProductPage(Pageable pageable, String query) {
        if (!query.isEmpty()) {
            return productRepository.findByProductNameOrCreatedAtOrPrice(query, pageable);
        }
        return productRepository.findAllProduct(pageable);
    }

    // cập nhật sản phẩm theo id, và truyền vào response Product
    @Override
    public Product updateProductById(Integer productId, ReqProductDetailDTO productDetail) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product id " + productId + " is not found"));
        product.setProductName(productDetail.productName());
        product.setPrice(productDetail.price());
        product.setDescription(productDetail.description());
        product.setCategory(categoryService.getCategory(productDetail.category()));
        product.setSupplier(supplierRepository.findById(productDetail.supplier())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier id " +
                                                                 productDetail.supplier() + " is not found")));
        Inventory inventory = inventoryRepository.findByProductId(productId);
        inventory.setStockQuantity(productDetail.stockQuantity());
        product.setInventory(inventory);
        product.setClientType(clientTypeRepository.findAllById(productDetail.clientTypes().stream().map(ClientTypeDto::id).toList()));
        product.setColors(colorRepository.findAllById(productDetail.colors().stream().map(ColorsDto::id).toList()));
        product.setSizes(sizeRepository.findAllById(productDetail.sizes().stream().map(SizesDto::id).toList()));
        product.setTags(tagRepository.findAllById(productDetail.tags().stream().map(TagsDto::id).toList()));
//        product.setColors(colorRepository.findAllById(productDetail.colors()));
//        product.setSizes(sizeRepository.findAllById(productDetail.sizes()));
//        product.setTags(tagRepository.findAllById(productDetail.tags()));
        return productRepository.save(product);
    }

    @Override
    public AddPageDTO getEntitiesConnectedToProduct() {
        // use service
        List<ReqCreateCategory> categories = categoryService.getAllCategory();
        // repository
        List<SupplierDto> suppliers = supplierRepository.findAllSupplier();
        List<SizesDto> sizes = sizeRepository.findAllSizes();
        List<TagsDto> tags = tagRepository.findAllTags();
        List<ColorsDto> colors = colorRepository.findAllColors();
        List<ClientTypeDto> clientTypes = clientTypeRepository.findAllClientTypes();
        return new AddPageDTO(categories, colors, sizes, tags, clientTypes, suppliers);
    }

    // Cập nhật ngày hết hạn
    @Override
    public void updateDiscountInfo(Integer productId, PricingInfoDTO pricingInfoDTO) {
        Product product = foundProduct(productId);
        product.setDiscountPrice(pricingInfoDTO.discountPrice());
        product.setDiscountExpirationDate(pricingInfoDTO.expirationDate());
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("No product found with id: " + id);
        }
    }

    @Override
    public Page<Product> findFilteredProducts(Integer categoryId,
                                              BigDecimal price,
                                              Set<Colors> colorsIds,
                                              Set<Sizes> sizeIds,
                                              Set<Tags> tagIds,
                                              Pageable pageable) {
        return productRepository.findProductsInCategoryAndSubCategoriesOrByFilters(categoryId, price, colorsIds,
                sizeIds,
                tagIds,
                pageable
        );
    }

    // Thêm ảnh của sản phẩm
    @Override
    public void addImages(Integer productId, List<MultipartFile> files, String bucketName) {
        Product product = foundProduct(productId);
        List<ProductImage> productImages = new ArrayList<>();
        List<TermImage> termImages = new ArrayList<>();
        for (MultipartFile file : files) {
            String key = UUID.randomUUID().toString();
            try {
                String imageCode = "product-images/%s/%s".formatted(productId, key);
                s3Service.putObject(
                        bucketName,
                        imageCode,
                        file.getBytes()
                );
                if (bucketName.equals(s3Buckets.getTermBucket())) {
                    TermImage termData = TermImage.builder().Key(key).product(product).build();
                    termImages.add(termData);
                } else {
                    ProductImage productImage = ProductImage.builder().Key(key).product(product).build();
                    productImages.add(productImage);
                }
            } catch (IOException e) {
                throw new RuntimeException(String.format("failed to upload product image with id %s", key), e);
            }
        }
        if (bucketName.equals(s3Buckets.getTermBucket())) {
            termImageRepository.saveAll(termImages);
        } else
            productImageRepository.saveAll(productImages);
    }

    @Override
    public Map<String, String> getImagesByID(Integer productId, String bucketName) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product id " + productId + " is not"));
        List<ProductImage> listOutputImages = productImageRepository.findByProductId(product.getId());
        if (listOutputImages.isEmpty()) {
            throw new ResourceNotFoundException("Product with id [%s] output images not found".formatted(productId));
        }

        // S3のためのキーリストを作成する
        List<String> s3Keys = listOutputImages.stream()
                .map(image -> String.format("product-images/%s/%s", productId, image.getKey())).toList();

        // S3からURLを取得する
        Map<String, String> s3Urls = s3Service.getObjects(bucketName, s3Keys);

        Map<String, String> imageMap = new HashMap<>();
        for (ProductImage image : listOutputImages) {
            String s3Key = String.format("product-images/%s/%s", productId, image.getKey());
            String url = s3Urls.get(s3Key);
            imageMap.put(image.getKey(), url);
        }

        return imageMap;
    }


    // Xoá ảnh của sản phẩm
    @Override
    public void deleteImage(Integer productId, String imageKey, String bucketName) {
        Optional<ProductImage> productImageOptional = productImageRepository.findByKeyAndProductId(productId, imageKey);
        if (productImageOptional.isPresent()) {
            // Xoá ảnh từ S3
//            String imageCode = String.format("product-images/%s/%s", productId, imageKey);
//            s3Service.deleteObject(bucketName, imageCode);

            // Xoá thông tin ảnh từ cơ sở dữ liệu
            productImageRepository.delete(productImageOptional.get());
        } else {
            throw new ResourceNotFoundException("Image with key [%s] not found for product id [%s]".formatted(imageKey, productId));
        }
    }

    @Override
    public void saveSubmittedImages(Integer productId, List<String> sourceKey, String sourceBucket, String destinationBucket) {
        s3Service.moveObjects(sourceKey, sourceBucket, destinationBucket);
//        List<TermImage> termImage = termImageRepository.findByProductId(productId);
//        // Xoá thông tin ảnh từ cơ sở dữ liệu
//        termImageRepository.deleteAll(termImage);
    }
}
