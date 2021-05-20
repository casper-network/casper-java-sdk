package com.casper.sdk.json;

import com.casper.sdk.domain.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class for converting all DeployExecutable from JSON
 */
class DeployExecutableFactory {

    /**
     * The abstract factory class for all DeployExecutable
     *
     * @param <T> the type of DeployExecutable
     */
    private static abstract class AbstractDeployExecutableJsonFactory<T extends DeployExecutable> {

        T create(final String fieldName, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                final List<DeployNamedArg> args = convertArgs(getArgsNode(fieldName, treeNode), codec);
                return getType().getConstructor(List.class).newInstance(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Obtains the node that contains the 'args' field value
         *
         * @param fieldName the name of the current field
         * @param treeNode  the current tree node
         * @return the node that contains the 'args' value
         */
        protected abstract TreeNode getArgsNode(final String fieldName, final TreeNode treeNode);

        /**
         * @return the type of the
         */
        protected abstract Class<T> getType();

        /**
         * Converts the arguments tree node into a list of DeployNamedArg
         *
         * @param argsNode the tre mode to read
         * @return a list of DeployNamedArg converted from JSON
         * @throws IOException if there is either an underlying I/O problem or decoding issue at format layer
         */
        protected List<DeployNamedArg> convertArgs(final TreeNode argsNode, ObjectCodec codec) throws IOException {

            final List<DeployNamedArg> args = new ArrayList<>();

            if (argsNode.isArray()) {
                for (int i = 0; i < argsNode.size(); i++) {

                    final JsonParser p = argsNode.get(i).traverse();

                    // If the code is not set use root codec
                    if (p.getCodec() == null) {
                        p.setCodec(codec);
                    }
                    final DeployNamedArg deployNamedArg = p.readValueAs(DeployNamedArg.class);
                    args.add(deployNamedArg);
                }
            }
            return args;
        }
    }

    /**
     * Converts JSON into a Transfer
     */
    private static class TransferJsonFactory extends AbstractDeployExecutableJsonFactory<Transfer> {
        @Override
        protected TreeNode getArgsNode(final String fieldName, final TreeNode treeNode) {
            return treeNode.get(fieldName).get("args");
        }

        @Override
        protected Class<Transfer> getType() {
            return Transfer.class;
        }
    }

    /**
     * Converts JSON into a DeployExecutable
     */
    private static class DefaultDeployExecutableJsonFactory extends AbstractDeployExecutableJsonFactory<DeployExecutable> {

        @Override
        protected TreeNode getArgsNode(final String fieldName, final TreeNode treeNode) {
            return treeNode.get(fieldName);
        }

        @Override
        protected Class<DeployExecutable> getType() {
            return DeployExecutable.class;
        }
    }

    /**
     * Converts JSON into a Payment
     */
    private static class PaymentJsonFactory extends AbstractDeployExecutableJsonFactory<Payment> {

        @Override
        Payment create(final String fieldName, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                final List<DeployNamedArg> args = convertArgs(getArgsNode(fieldName, treeNode), codec);
                return new Payment(convertModuleBytes(treeNode), args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private byte[] convertModuleBytes(final TreeNode treeNode) {
            TreeNode moduleBytes = treeNode.get("ModuleBytes");
            if (moduleBytes instanceof ObjectNode) {
                final TreeNode textNode = moduleBytes.get("module_bytes");
                if (textNode instanceof TextNode) {
                    return CLValue.fromString(((TextNode) textNode).textValue());
                }
            }
            return null;
        }

        @Override
        protected TreeNode getArgsNode(final String fieldName, final TreeNode treeNode) {
            return treeNode.get(fieldName);
        }

        @Override
        protected Class<Payment> getType() {
            return Payment.class;
        }
    }


    /** The map of field names to DeployExecutable Factories */
    private static final Map<String, AbstractDeployExecutableJsonFactory<?>> argsProviderMap = new HashMap<>();

    static {
        argsProviderMap.put("ModuleBytes", new PaymentJsonFactory());
        argsProviderMap.put("Transfer", new TransferJsonFactory());
        argsProviderMap.put("args", new DefaultDeployExecutableJsonFactory());
    }

    /**
     * Creates a DeployExecutable for a named field
     *
     * @param <T>       the type of the DeployExecutable to create
     * @param fieldName the name of the field
     * @param treeNode  the tree node containing the value of the field
     * @param codec     the codec so we can call other JsonDeserializer classes
     * @return the DeployExecutable for the specified fieldName and tree node value
     */
    <T extends DeployExecutable> T create(final String fieldName, final TreeNode treeNode, final ObjectCodec codec) {
        final AbstractDeployExecutableJsonFactory<?> jsonDeserializer = argsProviderMap.get(fieldName);
        if (jsonDeserializer != null) {
            //noinspection unchecked
            return (T) jsonDeserializer.create(fieldName, treeNode, codec);
        } else {
            throw new IllegalArgumentException(fieldName + " is not a valid DeployExecutable field");
        }
    }
}
