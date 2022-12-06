package com.sales.forecasting.views.FileUpload;

import java.util.HashMap;
import java.util.Map;

public class RowS {
    private Map<String, Object> values = new HashMap<>();

    public Object getValue(String column) {
        return values.get(column);
    }

    public void setValue(String column, Object value) {
        values.put(column, value);
    }
}
