package com.yousian.service;

import com.yousian.entity.Result;

import java.util.Map;

public interface OrderService {
    Result orderAdd(Map orderInfo) throws Exception;

    Map findorderSuccess(Integer id) throws Exception;
}
