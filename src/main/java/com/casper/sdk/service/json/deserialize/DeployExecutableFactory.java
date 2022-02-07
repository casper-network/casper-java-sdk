package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.Constants;
import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.types.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static com.casper.sdk.Constants.ARGS;

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

        T create(final String fieldName, final String entryPoint, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                final List<DeployNamedArg> args = convertArgs(getArgsNode(entryPoint, treeNode), codec);
                return getType().getConstructor(List.class).newInstance(args);
            } catch (Exception e) {
                throw new ConversionException(e);
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

            if (argsNode != null && argsNode.isArray()) {
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

        protected TreeNode getFieldNode(final String entryPoint, final TreeNode treeNode, final String fieldName) {
            TreeNode childNode = treeNode.get(entryPoint);
            if (childNode == null) {
                childNode = treeNode;
            }
            return childNode.get(fieldName);
        }

        protected Optional<Number> getNumericFieldValue(final String entryPoint,
                                                        final TreeNode treeNode,
                                                        final String fieldName) {

            final TreeNode fieldNode = getFieldNode(entryPoint, treeNode, fieldName);
            if (fieldNode instanceof TextNode) {
                return Optional.of(new BigInteger(((TextNode) fieldNode).textValue()));
            } else if (fieldNode instanceof NumericNode) {
                return Optional.of(((NumericNode) fieldNode).numberValue());
            } else {
                return Optional.empty();
            }
        }

        protected Optional<String> getFieldValue(final String entryPoint, final TreeNode treeNode, final String fieldName) {

            final TreeNode fieldNode = getFieldNode(entryPoint, treeNode, fieldName);
            if (fieldNode instanceof TextNode) {
                return Optional.of(((TextNode) fieldNode).textValue());
            } else {
                return Optional.empty();
            }
        }

        protected byte[] convertModuleBytes(final TreeNode treeNode) {
            final TreeNode moduleBytes = treeNode.get(ModuleBytes.class.getSimpleName());
            if (moduleBytes instanceof ObjectNode) {
                final TreeNode textNode = moduleBytes.get(Constants.MODULE_BYTES);
                if (textNode instanceof TextNode) {
                    return CLValue.fromString(((TextNode) textNode).textValue());
                }
            }
            return null;
        }
    }

    /**
     * Converts JSON into a Transfer
     */
    private static class TransferJsonFactory extends AbstractDeployExecutableJsonFactory<Transfer> {
        @Override
        protected TreeNode getArgsNode(final String fieldName, final TreeNode treeNode) {
            return treeNode.get(fieldName).get(ARGS);
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
     * Converts JSON into a StoredContractByName such as a payment.
     */
    private static class ModuleBytesFactory extends AbstractDeployExecutableJsonFactory<ModuleBytes> {

        @Override
        ModuleBytes create(final String fieldName, String entryPoint, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                final List<DeployNamedArg> args = convertArgs(getArgsNode(entryPoint, treeNode), codec);
                return new ModuleBytes(convertModuleBytes(treeNode), args);
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }

        @Override
        protected TreeNode getArgsNode(final String entryPoint, final TreeNode treeNode) {
            return getFieldNode(entryPoint, treeNode, ARGS);
        }

        @Override
        protected Class<ModuleBytes> getType() {
            return ModuleBytes.class;
        }
    }

    /**
     * Converts JSON into a StoredContractByName such as a payment.
     */
    private static class StoredContractByNameFactory extends AbstractDeployExecutableJsonFactory<StoredContractByName> {

        @Override
        StoredContractByName create(final String fieldName, String entryPoint, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                return new StoredContractByName(
                        getFieldValue(entryPoint, treeNode, Constants.NAME).orElse(null),
                        getFieldValue(entryPoint, treeNode, Constants.ENTRY_POINT).orElse(null),
                        convertModuleBytes(treeNode),
                        convertArgs(getArgsNode(entryPoint, treeNode), codec)
                );
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }

        @Override
        protected TreeNode getArgsNode(final String entryPoint, final TreeNode treeNode) {
            return getFieldNode(entryPoint, treeNode, ARGS);
        }

        @Override
        protected Class<StoredContractByName> getType() {
            return StoredContractByName.class;
        }
    }

    private static class StoredVersionedContractByNameFactory extends AbstractDeployExecutableJsonFactory<StoredVersionedContractByName> {

        StoredVersionedContractByName create(final String fieldName, String entryPoint, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                return new StoredVersionedContractByName(
                        getFieldValue(entryPoint, treeNode, Constants.NAME).orElse(null),
                        getNumericFieldValue(entryPoint, treeNode, Constants.VERSION).orElse(null),
                        getFieldValue(entryPoint, treeNode, Constants.ENTRY_POINT).orElse(null),
                        convertModuleBytes(treeNode),
                        convertArgs(getArgsNode(entryPoint, treeNode), codec)
                );
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }

        @Override
        protected TreeNode getArgsNode(final String entryPoint, final TreeNode treeNode) {
            return getFieldNode(entryPoint, treeNode, ARGS);
        }

        @Override
        protected Class<StoredVersionedContractByName> getType() {
            return StoredVersionedContractByName.class;
        }
    }

    /**
     * Converts JSON into a StoredContractByName such as a payment.
     */
    private static class StoredContractByHashFactory extends AbstractDeployExecutableJsonFactory<StoredContractByHash> {

        @Override
        StoredContractByHash create(final String fieldName, String entryPoint, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                return new StoredContractByHash(
                        getFieldValue(entryPoint, treeNode, Constants.HASH).map(ContractHash::new).orElse(null),
                        getFieldValue(entryPoint, treeNode, Constants.ENTRY_POINT).orElse(null),
                        convertArgs(getArgsNode(entryPoint, treeNode), codec)
                );
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }


        @Override
        protected TreeNode getArgsNode(final String entryPoint, final TreeNode treeNode) {
            return getFieldNode(entryPoint, treeNode, ARGS);
        }

        @Override
        protected Class<StoredContractByHash> getType() {
            return StoredContractByHash.class;
        }
    }

    /**
     * Converts JSON into a StoredVersionedContractByHash such as a payment.
     */
    private static class StoredVersionedContractByHashFactory extends AbstractDeployExecutableJsonFactory<StoredVersionedContractByHash> {

        @Override
        StoredVersionedContractByHash create(final String fieldName, String entryPoint, final TreeNode treeNode, final ObjectCodec codec) {
            try {
                return new StoredVersionedContractByHash(
                        getFieldValue(entryPoint, treeNode, Constants.HASH).map(ContractHash::new).orElse(null),
                        getNumericFieldValue(entryPoint, treeNode, Constants.VERSION).orElse(null),
                        getFieldValue(entryPoint, treeNode, Constants.ENTRY_POINT).orElse(null),
                        convertArgs(getArgsNode(entryPoint, treeNode), codec)
                );
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }

        @Override
        protected TreeNode getArgsNode(final String entryPoint, final TreeNode treeNode) {
            return getFieldNode(entryPoint, treeNode, ARGS);
        }

        @Override
        protected Class<StoredVersionedContractByHash> getType() {
            return StoredVersionedContractByHash.class;
        }
    }

    /** The map of field names to DeployExecutable Factories */
    private static final Map<String, AbstractDeployExecutableJsonFactory<?>> argsFactoryMap = new HashMap<>();

    static {
        argsFactoryMap.put("ModuleBytes", new ModuleBytesFactory());
        argsFactoryMap.put("StoredContractByHash", new StoredContractByHashFactory());
        argsFactoryMap.put("StoredContractByName", new StoredContractByNameFactory());
        argsFactoryMap.put("StoredVersionedContractByHash", new StoredVersionedContractByHashFactory());
        argsFactoryMap.put("StoredVersionedContractByName", new StoredVersionedContractByNameFactory());
        argsFactoryMap.put("Transfer", new TransferJsonFactory());
        argsFactoryMap.put(ARGS, new DefaultDeployExecutableJsonFactory());
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
    @SuppressWarnings("unchecked")
    <T extends DeployExecutable> T create(final String fieldName,
                                          final String entryPoint,
                                          final TreeNode treeNode,
                                          final ObjectCodec codec) {
        final AbstractDeployExecutableJsonFactory<?> jsonDeserializer = argsFactoryMap.get(entryPoint);
        if (jsonDeserializer != null) {
            return (T) jsonDeserializer.create(fieldName, entryPoint, treeNode, codec);
        } else {
            throw new IllegalArgumentException(fieldName + " is not a valid DeployExecutable field");
        }
    }
}
