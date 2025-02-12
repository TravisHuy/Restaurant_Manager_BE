package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    @Id
    private String id;
    private int number;
    private int capacity;
    private boolean available;
    private List<String> orderIds;
    private List<String> reservationIds;
}
