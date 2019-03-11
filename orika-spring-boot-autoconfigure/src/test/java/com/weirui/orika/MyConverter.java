package com.weirui.orika;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class MyConverter extends BidirectionalConverter<String,Integer> {

   @Override
   public Integer convertTo(String source, Type<Integer> destinationType) {
      return new Integer(source);
   }

   @Override
   public String convertFrom(Integer source, Type<String> destinationType) {
      return source.toString();
   }
}