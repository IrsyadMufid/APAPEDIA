package com.apapedia.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ShowCategoryRequestDTO {

    private String id;
    private String name;

    // Other properties if needed
}
