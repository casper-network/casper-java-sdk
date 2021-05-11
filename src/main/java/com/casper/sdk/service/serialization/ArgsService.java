package com.casper.sdk.service.serialization;

import com.casper.sdk.domain.ArgsOption;
import com.casper.sdk.domain.ArgsStandard;
import com.casper.sdk.service.serialization.factory.TypesEnum;
import com.casper.sdk.service.serialization.factory.TypesFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgsService {

    private final TypesFactory factory = new TypesFactory();

    public JsonNode buildTransfer(final String amount, final String target, final Object id, final String additionalInfo){

        Map<String, Object> transfer = new HashMap(){
            {
                put("Transfer", buildArgs(amount, target, id, additionalInfo));
            }
        };

        //intermediate bytes
        //add to transfer/module bytes
        //concat args session and payment hash for body hash

        return new ObjectMapper().convertValue(transfer, JsonNode.class);

    }


    /**
     *  Build the args map from supplied values
     * @param amount amount
     * @param target target
     * @param id id
     * @param additionalInfo additionInfo
     * @return hashmap representation of teh args json
     */
    private Map<String, Object> buildArgs(final String amount, final String target, final Object id, final String additionalInfo){

        final List<List<Object>> lists = Arrays.asList(List.of("amount", new ArgsStandard(factory.getInstance(TypesEnum.U512.name()).serialize(amount), amount, "U512")),
                List.of("target", new ArgsOption(target, target, new HashMap<>(){
                    {
                        put("ByteArray", 32);
                    }
                })),
                List.of("id", new ArgsStandard(factory.getInstance(TypesEnum.U64.name()).serialize(id), id, "U64")),
                List.of("additional_info", new ArgsStandard(factory.getInstance(TypesEnum.String.name()).serialize(additionalInfo, factory), additionalInfo, "String")));

        return new HashMap<>() {
            {
                put("args", lists);
            }
        };



    }


}
