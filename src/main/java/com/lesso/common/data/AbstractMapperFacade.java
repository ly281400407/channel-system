package com.lesso.common.data;

public interface AbstractMapperFacade {

    <T> T getMapper(String id, Class<T> var1 ) throws Exception;

}
