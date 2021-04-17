package com.vipin.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public class ClientConfiguration {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String hostName="http://localhost:1234/";
        private LocalDateTime createdAt;
        @OneToMany(cascade = CascadeType.ALL)
        List<LongUrl> longUrls;
}
