package com.casper.sdk.service.serialization.factory;


public interface TypesInterface {

    String serialize(final Object toSerialize);

    String serialize(final String toSerialize, final TypesFactory typesFactory);
}
