package com.project.price.repository;

import com.project.price.entity.AssetMetadata;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetMetaDataRepository extends JpaRepository<AssetMetadata, String> {

  // 키워드로 리스트 검색
  List<AssetMetadata> findByNameContainingIgnoreCaseOrSymbolContaining(
      String nameKeyword,
      String symbolKeyword
  );


}
