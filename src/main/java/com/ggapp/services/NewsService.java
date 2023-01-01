package com.ggapp.services;

import com.ggapp.common.dto.request.NewsRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.NewsResponse;
import com.ggapp.common.exception.ApplicationException;

public interface NewsService {
	NewsResponse createNews (int userCreateId, NewsRequest newsRequest) throws ApplicationException;
	CommonResponsePayload getAllNews (int page, int size) throws ApplicationException;
	CommonResponsePayload getNewsByKeyWord (int page, int size, int userCreateId, String userCreateName, String title) throws Exception;
	NewsResponse updateNews (int newsId, NewsRequest newsRequest) throws ApplicationException;
	BaseResponse deleteNews (int newsId) throws ApplicationException;
}
