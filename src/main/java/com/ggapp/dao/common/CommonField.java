package com.ggapp.dao.common;

import com.ggapp.common.commonenum.HistoryChangeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonField {
    private Integer fieldMaxLength;
    private String fieldType;
    private String fieldColor;
    private boolean isDisable;
    private HistoryChangeType historyChangeType;
}
