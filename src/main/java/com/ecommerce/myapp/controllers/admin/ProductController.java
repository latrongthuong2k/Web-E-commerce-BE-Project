package com.ecommerce.myapp.controllers.admin;

import com.ecommerce.myapp.dto.product.*;
import com.ecommerce.myapp.model.product.Colors;
import com.ecommerce.myapp.model.product.Product;
import com.ecommerce.myapp.model.product.Sizes;
import com.ecommerce.myapp.model.product.Tags;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.services.admin.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/product")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

    private final ProductService productService;
    //    private final InventoryService inventoryService;
    private final S3Buckets s3Buckets;

    /**
     * 指定したクエリパラメータに基づいて製品のページ分けされたリストを取得します。
     *
     * @param page      取得するページ番号。提供されていない場合のデフォルトは0です。
     * @param size      1ページあたりの項目数。提供されていない場合のデフォルトは20です。
     * @param query     製品をフィルタリングするための検索クエリ。提供されていない場合のデフォルトは空の文字列です。
     * @param sortField 製品を並べ替えるためのフィールド。提供されていない場合のデフォルトは「productName」です。
     * @param sortDir   並べ替えの方向。「asc」は昇順、「desc」は降順を意味します。提供されていない場合のデフォルトは「asc」です。
     * @return ページ分けされた製品リストと総ページ数を含むマップを含むResponseEntityオブジェクト。
     */
    @Cacheable(value = "productPage", key = "{#page,#sortField,#sortDir,#query}")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getProductById(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "productName") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ProductBasicInfoDTO> productPage = productService.getProductPage(pageable, query);
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        // response.put("currentPage", productPage.getNumber());
        // response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    /**
     * 商品詳細の情報を取得するメソッドです。
     *
     * @param id クライアントから送信される商品（product）のID
     * @return ResProductDetailDTO型の商品の詳細情報をHTTPレスポンスとともに返します。HTTPステータスは200（OK）です。
     */
    @GetMapping("/get")
    public ResponseEntity<ResProductDetailDTO> getProductById(@RequestParam(value = "productId") Integer id) {
        ResProductDetailDTO product = productService.getProductDetailById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * カテゴリID、価格、色、サイズ、タグによる製品のフィルタリングを実行し、指定されたページの製品リストを取得します。
     *
     * @param page:       ページ番号（デフォルトは0）
     * @param size:       ページサイズ（デフォルトは20）
     * @param categoryId: フィルタするカテゴリID
     * @param price:      フィルタする価格
     * @param colorsIds:  フィルタする色のID集合
     * @param sizeIds:    フィルタするサイズのID集合
     * @param tagIds:     フィルタするタグのID集合
     * @return 指定されたフィルタリングパラメータに基づいて取得された製品のリスト
     */
    @GetMapping("/filter-products")
    public ResponseEntity<List<ResViewProductDTO>> getFilterProductPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "price", defaultValue = "0") BigDecimal price,
            @RequestParam(name = "colorsIds") Set<Colors> colorsIds,
            @RequestParam(name = "sizeIds") Set<Sizes> sizeIds,
            @RequestParam(name = "tagIds") Set<Tags> tagIds) {
        Pageable pageable = PageRequest.of(page, size);
        // Get page product
        Page<Product> products = productService.findFilteredProducts(categoryId, price, colorsIds, sizeIds, tagIds, pageable);
        // Mapper to responseList
        List<ResViewProductDTO> responseList = products.stream()
                .map(product -> new ResViewProductDTO(product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getClientType(),
                        product.getColors(),
                        product.getSizes(),
                        product.getTags(),
                        product.getImages())).toList();
        return ResponseEntity.ok(responseList);
    }

    /**
     * 商品グループDtoを取得するエンドポイント
     *
     * @return ProductGroupDtoを含むレスポンスエンティティ
     */
    @GetMapping("/product-connected-entities")
    public ResponseEntity<ProductGroupDto> getGroupDto() {
        return ResponseEntity.ok(productService.getProductGroupDto());
    }

    /**
     * 商品情報を更新します。
     *
     * @param productId 更新したい商品のID。この引数はRequestParamで受け取られます。
     * @param product   更新しようとしている商品の詳細情報。これはRequestBodyを介してバリデートされた状態で受け取ります。
     * @return 更新された商品のIDが含まれるResponseEntity<Integer> レスポンスを返します。これはOKステータスを持つ。
     * @CacheEvict(value = "productPage", allEntries = true)は、キャッシュから全ての商品ページを削除します。
     */
    @CacheEvict(value = "productPage", allEntries = true)
    @PutMapping("/update")
    public ResponseEntity<Integer> updateProduct(
            @RequestParam(name = "productId") Integer productId,
            @Valid @RequestBody ReqProductDetailDTO product) {
        return ResponseEntity.ok(productService.updateProductById(productId, product).getId());
    }

    /**
     * @param createProduct プロダクト生成要求、ヴァリデーションを経ています。
     * @return 生成されたプロダクトのID、応答エンティティにラップされています。
     * @CacheEvict(value = "productPage", allEntries = true)
     * プロダクトを生成します。
     */
    @CacheEvict(value = "productPage", allEntries = true)
    @PostMapping(value = "/create")
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid ReqProductDetailDTO createProduct
    ) {
        Product product = productService.addNewProduct(createProduct);
        return ResponseEntity.ok(product.getId());
    }

    /**
     * 商品IDを指定して、画像を商品に追加します。
     *
     * @param productId 追加する商品の識別子 (パス変数)
     * @param files     商品に追加する画像のリスト (リクエストパート)
     */
    @PostMapping(value = "/product-images/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addImages(
            @PathVariable("productId") Integer productId,
            @RequestPart("file") List<MultipartFile> files) {
        productService.addImages(productId, files, s3Buckets.getProduct());
    }

//    @PostMapping(value = "/product-images",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public void addImages(
//            @RequestPart("file") List<MultipartFile> files) {
//        productService.addImages(6, files, s3Buckets.getProduct());
//    }

    //     GET / profile-image
    @GetMapping("get-product-images/{productId}")
    public List<String> getImages(@PathVariable("productId") Integer productId) {
        return productService.getImagesByID(productId, s3Buckets.getProduct());
    }


    /**
     * 与えられた製品IDの製品を削除します。
     *
     * @param productId 製品の識別子（パス変数）
     * @return 成功メッセージを伴うHTTPレスポンスエンティティ
     */
    @CacheEvict(value = "productPage", allEntries = true)
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        productService.deleteById(productId);
        return ResponseEntity.ok().body("Product deleted successfully");
    }

}






