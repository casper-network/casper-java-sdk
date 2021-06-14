{
  deploy: {
    approvals: [
      {
        signature: "130 chars",
        signer: "012d9dded24145247421eb8b904dda5cce8a7c77ae18de819a25628c4a01adbf76",
      },
    ],
    hash: "ceaaa76e7fb850a09d5c9d16ac995cb52eff2944066cfd8cac27f3595f11b652",
    header: {
      account: "012d9dded24145247421eb8b904dda5cce8a7c77ae18de819a25628c4a01adbf76",
      body_hash: "0e68d66a9dfab19bb1898d5f4d11a4f55dd06a0cae3917afc1eae4a5b56352e7",
      chain_name: "casper-test",
      dependencies: [
      ],
      gas_price: 1,
      timestamp: "2021-05-06T07:49:32.583Z",
      ttl: "30m",
    },
    payment: {
      ModuleBytes: {
        args: [
          [
            "amount",
            {
              bytes: "0500e40b5402",
              cl_type: "U512",
              parsed: "10000000000",
            },
          ],
        ],
        module_bytes: "",
      },
    },
    session: {
      Transfer: {
        args: [
          [
            "amount",
            {
              bytes: "0500743ba40b",
              cl_type: "U512",
              parsed: "50000000000",
            },
          ],
          [
            "target",
            {
              bytes: "1541566bdad3a3cfa9eb4cba3dcf33ee6583e0733ae4b2ccdfe92cd1bd92ee16",
              cl_type: {
                ByteArray: 32,
              },
              parsed: "1541566bdad3a3cfa9eb4cba3dcf33ee6583e0733ae4b2ccdfe92cd1bd92ee16",
            },
          ],
          [
            "id",
            {
              bytes: "01a086010000000000",
              cl_type: {
                Option: "U64",
              },
              parsed: 100000,
            },
          ],
        ],
      },
    },
  },
}