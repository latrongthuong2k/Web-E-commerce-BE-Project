package com.ecommerce.myapp.Entity.Bill;

import com.ecommerce.myapp.Users.Entity.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @CreatedDate
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "approve_date")
    private LocalDateTime approvedDate;

    @CreatedBy
    @Column(name = "create_by", nullable = false)
    private Integer createdBy;

    @Column(name = "approve_by")
    private Integer approvedBy;

    // Status
    @Enumerated(EnumType.STRING)
    @Column(name = "bill_status")
    private BillStatus billStatus;

    @OneToMany(mappedBy = "bill")
    private List<Purchases> purchases;

    @OneToMany(mappedBy = "bill_Id")
    private List<Sales> sales;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;




}
