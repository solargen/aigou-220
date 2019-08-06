package cn.itsource.common.repository;

import cn.itsource.common.domain.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<ProductDoc,Long> {
}
