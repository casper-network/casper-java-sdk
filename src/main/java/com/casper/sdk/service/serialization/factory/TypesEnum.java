package com.casper.sdk.service.serialization.factory;

public enum TypesEnum {

    Bool, // boolean primitive
    I32, // signed 32-bit integer primitive
    I64, // signed 64-bit integer primitive
    U8, // unsigned 8-bit integer primitive
    U32, // unsigned 32-bit integer primitive
    U64, // unsigned 64-bit integer primitive
    U128, // unsigned 128-bit integer primitive
    U256, // unsigned 256-bit integer primitive
    U512, // unsigned 512-bit integer primitive
    Unit, // singleton value without additional semantics
    String, // e.g. "Hello, World!"
    URef, // unforgeable reference (see above)
    Key, // global state key (see above)
//    Option(CLType), // optional value of the given type
//    List(CLType), // list of values of the given type (e.g. Vec in rust)
//    FixedList(CLType, u32), // same as `List` above, but number of elements
//    // is statically known (e.g. arrays in rust)
//    Result(CLType, CLType), // co-product of the the given types;
//    // one variant meaning success, the other failure
//    Map(CLType, CLType), // key-value association where keys and values have the given types
//    Tuple1(CLType), // single value of the given type
//    Tuple2(CLType, CLType), // pair consisting of elements of the given types
//    Tuple3(CLType, CLType, CLType), // triple consisting of elements of the given types
    Any // Indicates the type is not known

}
