package com.sdy.bbbb.redis;

import com.sdy.bbbb.entity.RedisRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisRefreshToken, String> {
}
