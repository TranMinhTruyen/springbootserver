package com.ggapp.common.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@JsonPropertyOrder(value = {
		"data",
		"totalRecord",
		"page",
		"size",
		"totalPage"
})
@Data
public class CommonResponse implements Serializable {
	private Object[] data;
	private int totalRecord;
	private int page;
	private int size;
	private int totalPage;

	public CommonResponse() {}

	public CommonResponse(Object[] data, int totalRecord, int page, int size, int totalPage) {
		this.data = data;
		this.totalRecord = totalRecord;
		this.page = page;
		this.size = size;
		this.totalPage = totalPage;
	}

	public CommonResponse getCommonResponse(int page, int size, List result){
		int offset = (page - 1) * size;
		int total = result.size();
		int totalPage = (total % size) == 0 ? (int)(total / size) : (int)((total / size) + 1);
		Object[] data = result.stream().skip(offset).limit(size).toArray();
		return new CommonResponse(data, total, page, size, totalPage);
	}
}
