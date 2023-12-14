package com.ecommerce.myapp.services.admin.impl;

import com.ecommerce.myapp.dto.Mapper.ProductPageMapper;
import com.ecommerce.myapp.dto.category.ResCategory;
import com.ecommerce.myapp.dto.product.*;
import com.ecommerce.myapp.exceptions.DuplicateResourceException;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.bill.Supplier;
import com.ecommerce.myapp.model.product.*;
import com.ecommerce.myapp.model.warehouse.Inventory;
import com.ecommerce.myapp.repositories.product.*;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.s3.S3Service;
import com.ecommerce.myapp.services.admin.CategoryService;
import com.ecommerce.myapp.services.admin.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(rollbackOn = Exception.class)
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
    private final ProductPageMapper productPageMapper;
    private final InventoryRepository inventoryRepository;

    // Services

    @Override
    public Product addNewProduct(ReqProductDetailDTO newProduct) {
        Optional<Product> productCheck = productRepository.findByProductName(newProduct.productName());
        if (productCheck.isPresent())
            throw new DuplicateResourceException("Product is exists please input another name");
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
    }

    // Trả về toàn bộ product
    @Override
    public List<ProductBasicInfoDTO> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productPageMapper::toDto).toList();
    }


    // Lấy 1 trang các sản phẩm tìm được
    @Override
    public Page<ProductBasicInfoDTO> getProductPage(Pageable pageable, String query) {
        Page<Product> productPage = productRepository.findByProductNameOrCreatedAtOrPrice(query, pageable);
        return productPage.map(productPageMapper::toDto);
    }

    // cập nhật sản phẩm theo id, và truyền vào response Product
    @Override
    public Product updateProductById(Integer productId, ReqProductDetailDTO dto) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product id " + productId + " is not found"));
        System.out.println();
        product.setProductName(dto.productName());
        product.setPrice(dto.price());
        product.setDescription(dto.description());
        product.setCategory(categoryService.getCategory(dto.category()));
        product.setSupplier(supplierRepository.findById(dto.supplier())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier id " +
                                                                 dto.supplier() + " is not found")));
        Inventory inventory = inventoryRepository.findByProductId(productId);
        inventory.setStockQuantity(dto.stockQuantity());
        product.setInventory(inventory);
        product.setClientType(clientTypeRepository.findAllById(dto.clientTypes().stream().map(ClientTypeDto::id).toList()));
        product.setColors(colorRepository.findAllById(dto.colors().stream().map(ColorsDto::id).toList()));
        product.setSizes(sizeRepository.findAllById(dto.sizes().stream().map(SizesDto::id).toList()));
        product.setTags(tagRepository.findAllById(dto.tags().stream().map(TagsDto::id).toList()));
//        product.setColors(colorRepository.findAllById(productDetail.colors()));
//        product.setSizes(sizeRepository.findAllById(productDetail.sizes()));
//        product.setTags(tagRepository.findAllById(productDetail.tags()));
        return productRepository.save(product);
    }

    @Override
    public ProductGroupDto getProductGroupDto() {
        // use service
        List<ResCategory> categories = categoryService.getAllCategory();
        // repository
        List<SupplierDto> suppliers = supplierRepository.findAllSupplier();
        List<SizesDto> sizes = sizeRepository.findAllSizes();
        List<TagsDto> tags = tagRepository.findAllTags();
        List<ColorsDto> colors = colorRepository.findAllColors();
        List<ClientTypeDto> clientTypes = clientTypeRepository.findAllClientTypes();
        return new ProductGroupDto(categories, colors, sizes, tags, clientTypes, suppliers);
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
        for (MultipartFile file : files) {
            String key = UUID.randomUUID().toString();
            try {
                String imageCode = "product-images/%s/%s".formatted(productId, key);
                s3Service.putObject(
                        bucketName,
                        imageCode,
                        file.getBytes()
                );
                ProductImage productImage = ProductImage.builder().Key(key).product(product).build();
                productImages.add(productImage);
            } catch (IOException e) {
                throw new RuntimeException(String.format("failed to upload product image with id %s", key), e);
            }
        }
        productImageRepository.saveAll(productImages);
    }

    @Override
    public List<String> getImagesByID(Integer productId, String bucketName) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product id " + productId + " is not"));
        List<ProductImage> listOutputImages = productImageRepository.findByProductId(product.getId());
        if (listOutputImages.isEmpty()) {
            throw new ResourceNotFoundException("Product with id [%s] output images not found".formatted(productId));
        }

        // S3のためのキーリストを作成する
        List<String> s3Keys = listOutputImages.stream()
                .map(image -> String.format("product-images/%s/%s", productId, image.getKey())).toList();

        // S3からURLを取得する
        return s3Service.getObjects(bucketName, s3Keys);
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
