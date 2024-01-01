package com.ecommerce.myapp.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_images")
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_image_key", nullable = false)
    private String Key;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_image_fk"))
    private AppUser appUser;

}
