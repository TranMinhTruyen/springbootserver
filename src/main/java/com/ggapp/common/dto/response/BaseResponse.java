package com.ggapp.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static com.ggapp.common.utils.Constant.DATE_TIME_FORMAT_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {
	private String timestamp = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN).format(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+7")));
	private int status;
	private String statusname;
	private String message;
	private Object payload;
}
