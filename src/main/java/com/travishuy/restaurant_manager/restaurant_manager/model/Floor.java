package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "floors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Floor {
    @Id
    private String id;
    private String name;
    private List<String> tableIds;
}
