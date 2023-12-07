package com.ecommerce.myapp.Entity.ProductConnectEntites;

import com.ecommerce.myapp.Entity.*;
import com.ecommerce.myapp.Entity.Bill.Purchases;
import com.ecommerce.myapp.Entity.Bill.Sales;
import com.ecommerce.myapp.Entity.Bill.Supplier;
import com.ecommerce.myapp.Entity.ShopingCart.ProductCartDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // tên sản phẩm
    @Size(max = 50)
    @Column(name = "product_name", nullable = false, length = 50)
    private String productName;

    // mô tả
    @Lob
    @Column(name = "description")
    private String description;

    // giá sản phẩm
    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    // giá sản phẩm
    @Column(name = "discount_price", precision = 15, scale = 2)
    private BigDecimal discountPrice;

    // Ngày hết hạn giảm giá
    @Column(name = "discount_expiration")
    private LocalDateTime discountExpirationDate;

    // ngày tạo hay ( ngày nhập mới )
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // thời điểm update sản phẩm
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    // Ghi chú lại người sửa
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    // Ghi chú lại người sửa cuối cùng
    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;

    private Integer status;


    //---------------- ManyToMany ----------------

    // size relationship table
    @ManyToMany
    @JoinTable(name = "product_size",
            joinColumns = @JoinColumn(name = "product_id",
                    nullable = false,foreignKey = @ForeignKey(name = "product_id_fk")),
            inverseJoinColumns = @JoinColumn(name = "size_id",
                    nullable = false,foreignKey = @ForeignKey(name = "size_id_fk")))
    private List<Sizes> sizes = new ArrayList<>();

    // color relationship table
    @ManyToMany
    @JoinTable(name = "product_color",
            joinColumns = @JoinColumn(name = "product_id",
                    nullable = false,foreignKey = @ForeignKey(name = "product_id_color_fk")),
            inverseJoinColumns = @JoinColumn(name = "color_id",
                    nullable = false,foreignKey = @ForeignKey(name = "color_id_fk")))
    private List<Colors> colors = new ArrayList<>();

    // tags
    @ManyToMany
    @JoinTable(name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id",
                    nullable = false,foreignKey = @ForeignKey(name = "product_id_tag_fk")),
            inverseJoinColumns = @JoinColumn(name = "tag_id",
                    nullable = false,foreignKey = @ForeignKey(name = "tag_id_fk")))
    private List<Tags> tags = new ArrayList<>();

    // wishlist relationship table
    @ManyToMany
    @JoinTable(name = "product_wishlist",
            joinColumns = @JoinColumn(name = "product_id",
                    nullable = false, foreignKey = @ForeignKey(name = "product_id_wishlist_fk")),
            inverseJoinColumns = @JoinColumn(name = "wishlist_id",
                    nullable = false), foreignKey = @ForeignKey(name = "wishlist_id_fk"))
    private List<WishList> wishLists = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "product_client_type",
            joinColumns = @JoinColumn(name = "product_id",
                    nullable = false, foreignKey = @ForeignKey(name = "product_id_client_type_fk")),
            inverseJoinColumns = @JoinColumn(name = "client_type_id",
                    nullable = false), foreignKey = @ForeignKey(name = "client_type_id_fk"))
    private List<ClientType> clientType = new ArrayList<>();

    //---------------- OneToMany ----------------

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductCartDetail> productCartDetails = new ArrayList<>();

    // review k xoá vì để lưu data phát triển sản phẩm
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    // image relationship table
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();






    //---------------- ManyToOne ----------------
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;


    // category relationship table
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;


    // Bills không dùng cascade = CascadeType.ALL để lưu data mua bán để tính lời lỗ
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "suppliers_id")
    private Supplier supplier;

    //---------------- OneToOne ----------------
    @OneToOne(mappedBy = "product")
    private Purchases purchases;
    @OneToOne(mappedBy = "product")
    private Sales sale;
}