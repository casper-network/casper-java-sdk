package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.transaction.IdentityKind;
import com.casper.sdk.model.transaction.PruneKind;
import com.casper.sdk.model.transaction.WriteKind;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a single transformation occurring during execution.
 *
 * @author ian@meywood.com
 */
public enum TransformKindV2 {

    /**
     * An identity transformation that does not modify a value in the global state.
     * Created as a result of reading from the global state.
     */
    @JsonProperty("Identity")
    Identity(IdentityKind.class),
    /** Writes a new value in the global state. */
    @JsonProperty("Write")
    Write(WriteKind.class),
    /**
     * A wrapping addition of an `i32` to an existing numeric value (not necessarily an `i32`) in
     * the global state.
     */
    @JsonProperty("AddInt32")
    AddInt32(AddInt32.class),
    /**
     * A wrapping addition of a `u64` to an existing numeric value (not necessarily an `u64`) in
     * the global state.
     */
    @JsonProperty("AddUInt64")
    AddUInt64(AddUInt64.class),
    /**
     * A wrapping addition of a `U128` to an existing numeric value (not necessarily an `U128`) in
     * the global state.
     */
    @JsonProperty("AddUInt128")
    AddUInt128(AddUInt128.class),
    /**
     * A wrapping addition of a `U256` to an existing numeric value (not necessarily an `U256`) in
     * the global state.
     */
    @JsonProperty("AddUInt256")
    AddUInt256(AddUInt256.class),
    /**
     * A wrapping addition of a `U512` to an existing numeric value (not necessarily an `U512`) in
     * the global state.
     */
    @JsonProperty("AddUInt512")
    AddUInt512(AddUInt512.class),
    /**
     * Adds new named keys to an existing entry in the global state.
     * ///
     * This transform assumes that the existing stored value is either an Account or a Contract.
     */
    @JsonProperty("AddKeys")
    AddKeys(AddKeys.class),
    /**
     * Removes the pathing to the global state entry of the specified key. The pruned element
     * remains reachable from previously generated global state root hashes, but will not be
     * included in the next generated global state root hash and subsequent state accumulated
     * from it.
     */
    @JsonProperty("Prune")
    Prune(PruneKind.class),
    /** Represents the case where applying a transform would cause an error. */
    @JsonProperty("TransformError")
    Failure(Failure.class);

    private final Class<?> transfromClass;

    TransformKindV2(final Class<?> transfromClass) {
        this.transfromClass = transfromClass;
    }

    /**
     * Retrieve Transform implementation class from Transform name
     *
     * @param name {@link TransformTypeData} class name
     * @return the class object for the {@link TransformTypeData}
     * @throws NoSuchTypeException if no type is found for given name
     */
    public static Class<?> getClassByName(final String name) throws NoSuchTypeException {
        for (TransformKindV2 t : values()) {
            if (t.name().equalsIgnoreCase(name)) {
                return t.transfromClass;
            }
        }
        throw new NoSuchTypeException();
    }
}
