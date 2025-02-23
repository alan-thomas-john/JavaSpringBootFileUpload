package com.fileupload.Globa.repository;

import com.fileupload.Globa.entity.Consumer;
//import com.fileupload.Globa.entity.ConsumerImg;
import com.fileupload.Globa.entity.consumerImgTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerImgRepository extends JpaRepository<consumerImgTable, Long> {
    List<consumerImgTable> findByConsumer(Consumer consumer);
}