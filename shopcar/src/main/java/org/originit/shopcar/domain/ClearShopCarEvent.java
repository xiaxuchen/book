package org.originit.shopcar.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClearShopCarEvent implements Serializable {

    private Integer id;

    private String name;
}
