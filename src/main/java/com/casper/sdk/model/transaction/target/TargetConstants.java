package com.casper.sdk.model.transaction.target;

/**
 * Constants for transaction targets.
 *
 * @author ian@meywood.com
 */
public abstract class TargetConstants {

    public static final String ID = "id";
    public static final String MODULE_BYTES = "module_bytes";
    public static final String NATIVE = "Native";
    public static final String RUNTIME = "runtime";
    public static final String SESSION = Session.class.getSimpleName();
    public static final String STORED = Stored.class.getSimpleName();
}
