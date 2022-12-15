package com.sdy.bbbb.redis;

import com.sdy.bbbb.entity.RedisRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisDataRepository extends CrudRepository<RedisData, String> {
}
