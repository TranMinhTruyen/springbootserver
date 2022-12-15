package com.ggapp.services;

import com.ggapp.common.dto.request.NewsRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.NewsResponse;
import com.ggapp.common.exception.ApplicationException;

public interface NewsService {
	NewsResponse createNews (Long userCreateId, NewsRequest newsRequest) throws ApplicationException;
	CommonResponsePayload getAllNews (int page, int size) throws ApplicationException;
	CommonResponsePayload getNewsByKeyWord (int page, int size, Long userCreateId, String userCreateName, String title) throws Exception;
	NewsResponse updateNews (Long newsId, NewsRequest newsRequest) throws ApplicationException;
	BaseResponse deleteNews (Long newsId) throws ApplicationException;
}
