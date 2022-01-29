{
    "approvals": [
        {
            "signature": "0100E61b1E318Aed0231C88eBa80CBA466FA3ae4AeF07665139C9F23851A936488124aF30136F5EA3bc916cf44C089eD7d6F2d871793022bA62eFE7aF04E1C3206",
            "signer": "01F56E258a89BD12Cea9D1d77cd2dd367F5134dA564572E1c330Fcae5579Ad0613"
        }
    ],
    "hash": "6Ba2513213f8202490f95fCF033b5ca9D989147A0df0799Db8Aa79030Ae4E9Af",
    "header": {
        "account": "01F56E258a89BD12Cea9D1d77cd2dd367F5134dA564572E1c330Fcae5579Ad0613",
        "body_hash": "B32783c31745F618754573F2C76eBe9fE8CDEd49b38eaD644f9755cD718d996D",
        "chain_name": "casper-net-1",
        "dependencies": [],
        "gas_price": 1,
        "timestamp": "2022-01-26T11:02:01.557Z",
        "ttl": "30m"
    },
    "payment": {
        "ModuleBytes": {
            "args": [
                [
                    "amount",
                    {
                        "cl_type": "U512",
                        "bytes": "0500F2052a01",
                        "parsed": "5000000000"
                    }
                ]
            ],
            "module_bytes": ""
        }
    },
    "session": {
        "StoredContractByHash": {
            "args": [
                [
                    "token_id",
                    {
                        "cl_type": "String",
                        "bytes": "4000000065653763393334326564653166306331373430613538616332653837353138666339616137653239666132666462656466373361393735626335363665326433",
                        "parsed": "ee7c9342ede1f0c1740a58ac2e87518fc9aa7e29fa2fdbedf73a975bc566e2d3"
                    }
                ],
                [
                    "instrument_id",
                    {
                        "cl_type": "String",
                        "bytes": "110000004953494e3a444530303058584232554c32",
                        "parsed": "ISIN:DE000XXB2UL2"
                    }
                ],
                [
                    "asset_decimals",
                    {
                        "cl_type": "U256",
                        "bytes": "010A",
                        "parsed": "10"
                    }
                ],
                [
                    "asset_units",
                    {
                        "cl_type": "U256",
                        "bytes": "0340420F",
                        "parsed": "1000000"
                    }
                ],
                [
                    "asset_holders",
                    {
                        "cl_type": {
                            "Map": {
                                "key": {
                                    "ByteArray": 32
                                },
                                "value": "U256"
                            }
                        },
                        "bytes": "02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709",
                        "parsed": [
                            {
                                "key": "e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb",
                                "value": "400000"
                            },
                            {
                                "key": "Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970",
                                "value": "600000"
                            }
                        ]
                    }
                ],
                [
                    "liability_decimals",
                    {
                        "cl_type": "U256",
                        "bytes": "010A",
                        "parsed": "10"
                    }
                ],
                [
                    "liability_units",
                    {
                        "cl_type": "U256",
                        "bytes": "0340420F",
                        "parsed": "1000000"
                    }
                ],
                [
                    "liability_holders",
                    {
                        "cl_type": {
                            "Map": {
                                "key": {
                                    "ByteArray": 32
                                },
                                "value": "U256"
                            }
                        },
                        "bytes": "02000000e3d394334ce46c6043bcd33e4686d2b7a369c606bfcce4c26ca14d2c73fac82403801a06219ac9a617de3433d6ab1c9fa4aa9fb8d874dba9a00b2b562d16da533460657503c02709",
                        "parsed": [
                            {
                                "key": "e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824",
                                "value": "400000"
                            },
                            {
                                "key": "219ac9a617DE3433d6ab1C9fA4aa9FB8D874DBa9A00b2B562d16da5334606575",
                                "value": "600000"
                            }
                        ]
                    }
                ]
            ],
            "entry_point": "issue_token_type_01",
            "hash": "7782DF56d76dB301C9f1c6C0d734f14c6f02e2a897428b887e9F49cf44604457"
        }
    }
}