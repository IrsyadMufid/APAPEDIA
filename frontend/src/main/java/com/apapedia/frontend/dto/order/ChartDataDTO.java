package com.apapedia.frontend.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChartDataDTO {
    private LocalDateTime date;
    private int quantitySold;

    // Constructors, getters, and setters
}
