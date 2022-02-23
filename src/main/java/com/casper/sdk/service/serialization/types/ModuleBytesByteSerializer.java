package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.types.ModuleBytes;

/**
 * The byte serializer for {@link ModuleBytes} deploy executables
 */
public class ModuleBytesByteSerializer extends AbstractDeployExecutableByteSerializer<ModuleBytes> {

    public ModuleBytesByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        super(ModuleBytes.class, factory, typesFactory);
    }

    @Override
    protected byte[] toSpecializedBytes(final ModuleBytes deployExecutable) {
        return byteArraySerializer.serialize(deployExecutable.getModuleBytes());
    }
}
