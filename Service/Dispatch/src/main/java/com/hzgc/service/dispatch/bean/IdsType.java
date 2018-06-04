package com.hzgc.service.dispatch.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IdsType<T> implements Serializable{

    private static final long serialVersionUID = 114352653081166330L;
    private List<T> id;
}
