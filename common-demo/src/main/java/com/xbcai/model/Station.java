package com.xbcai.model;

import lombok.Data;

@Data
public class Station {
    private Double lng;
    private Double lat;
    private String name;
    private StaVO staVO;
}

