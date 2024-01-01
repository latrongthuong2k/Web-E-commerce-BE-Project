package com.ecommerce.myapp.model.checkoutGroup;

import com.ecommerce.myapp.model.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;
    @PrePersist
    public void initializeUUID() {
        if (serialNumber == null) {
            serialNumber = UUID.randomUUID().toString();
        }
    }

    @CreatedDate
    @Column(name = "order_at", nullable = false)
    private LocalDateTime orderAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private OrderStatusV2 status;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "receive_name", length = 100, nullable = false)
    private String receiveName;

    @Column(name = "receive_address", nullable = false)
    private String receiveAddress;

    @Column(name = "receive_phone", length = 15, nullable = false)
    private String receivePhone;


    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

}
