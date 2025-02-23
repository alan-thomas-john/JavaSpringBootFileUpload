package com.fileupload.Globa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consumerimgtab")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class consumerImgTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumer_Id", nullable = false)
    private Consumer consumer;

    @ManyToOne
    @JoinColumn(name = "img_Id", nullable = false)
    private ImageData imageData;

    @Column(name = "file_name")
    private String imgFile;
}
