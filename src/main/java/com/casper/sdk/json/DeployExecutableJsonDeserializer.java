package com.casper.sdk.json;

import com.casper.sdk.domain.DeployExecutable;
import com.casper.sdk.domain.DeployNamedArg;
import com.casper.sdk.domain.Transfer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeployExecutableJsonDeserializer extends JsonDeserializer<DeployExecutable> {

    @Override
    public DeployExecutable deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

        ObjectCodec codec = p.getCodec();
        TreeNode treeNode = codec.readTree(p);
        TreeNode args = null;

        Class type = null;

        final Iterator<String> iterator = treeNode.fieldNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            switch (name) {
                case "Transfer":
                    type = Transfer.class;
                    TreeNode transfer = treeNode.get(name);
                    args = transfer.get("args");
                    break;
                case "args":
                    type = DeployExecutable.class;
                    args = treeNode.get(name);
            }
        }

        if (args != null) {
            try {
                //noinspection unchecked
                return (DeployExecutable) type.getConstructor(List.class).newInstance(convertArgs(args));
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // TODO create exception
                throw new RuntimeException(e);
            }
        }

        return new DeployExecutable(new ArrayList<>());
    }

    private List<DeployNamedArg> convertArgs(TreeNode argsNode) throws IOException {

        List<DeployNamedArg> args = new ArrayList<>();

        if (argsNode.isArray()) {
            for (int i = 0; i < argsNode.size(); i++) {
                args.add(argsNode.get(i).traverse().readValueAs(DeployNamedArg.class));
            }
        }
        return args;
    }
}
