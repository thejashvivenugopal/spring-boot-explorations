package com.redis.redis.repo;

import com.redis.redis.model.Customers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepo extends MongoRepository<Customers,String> {
    @Cacheable(value = "customerEvict",key = "#mobileNumber")
    Customers findByMobileNumber(String mobileNumber);

    @CacheEvict(value = "customerEvict",key = "#entity.mobileNumber")
    @Override
    <S extends Customers> S save(S entity);

    @CacheEvict(value = "customerEvict",key = "#entity.mobileNumber")
    @Override
    void delete(Customers entity);
}
