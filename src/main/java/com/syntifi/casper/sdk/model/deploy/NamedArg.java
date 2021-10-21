package com.syntifi.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.syntifi.casper.sdk.model.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;

import lombok.Data;

/**
 * Named arguments to a contract
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class NamedArg<T, P extends AbstractCLType> {

  /**
   * The first value in the array is the type of the arg
   */
  private String type;

  /**
   * The second value in the array is a CLValue type
   */
  private AbstractCLValue<T, P> clValue;
}
