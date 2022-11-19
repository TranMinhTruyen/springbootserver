package com.ggapp.services;

import com.ggapp.common.dto.request.NewsRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.dto.response.NewsResponse;

public interface NewsServices {
	NewsResponse createNews (int userCreateId, NewsRequest newsRequest) throws Exception;
	CommonResponse getAllNews (int page, int size) throws Exception;
	CommonResponse getNewsByKeyWord (int page, int size, int userCreateId, String userCreateName, String title) throws Exception;
	NewsResponse updateNews (int newsId, NewsRequest newsRequest) throws Exception;
	BaseResponse deleteNews (int newsId) throws Exception;
}
