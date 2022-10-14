package com.casper.sdk.model.storedvalue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class DummyClass implements Serializable {
    private final long serialVersionUID = 1231399587346346L;

    private String name;
    private Integer value;
}
