package com.rathod.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrder {
    private int custId;
    private int restId;
    private int itemId;
    private int qty;
}
