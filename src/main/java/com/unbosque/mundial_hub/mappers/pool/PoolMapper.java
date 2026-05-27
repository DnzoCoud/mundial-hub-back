package com.unbosque.mundial_hub.mappers.pool;

import com.unbosque.mundial_hub.dto.domain.pool.PoolResponse;
import com.unbosque.mundial_hub.models.PoolEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PoolMapper {
    PoolResponse toPoolResponse(PoolEntity pool);
}
