package com.example.common.Enum;

public enum HistoryChangeType {
    NEW(1),
    UPDATE(2),
    DELETE(3);

    private int typeValue;

    HistoryChangeType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(int typeValue) {
        this.typeValue = typeValue;
    }
}
