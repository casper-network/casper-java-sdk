package com.syntifi.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import com.syntifi.casper.sdk.identifier.block.BlockIdentifierByHash;
import com.syntifi.casper.sdk.identifier.block.BlockIdentifierByHeight;
import com.syntifi.casper.sdk.model.block.JsonBlock;
import com.syntifi.casper.sdk.model.block.JsonBlockData;
import com.syntifi.casper.sdk.model.key.Algorithm;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.peer.PeerData;
import com.syntifi.casper.sdk.model.stateroothash.StateRootHashData;
import com.syntifi.casper.sdk.model.storedvalue.StoredValueData;
import com.syntifi.casper.sdk.model.transfer.Transfer;
import com.syntifi.casper.sdk.model.transfer.TransferData;
import com.syntifi.casper.sdk.service.CasperService;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Unit tests for {@link CasperService}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
class CasperSdkApplicationTests {
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public enum CasperNetwork {
		MAIN_NET("195.201.142.76", 7777), TEST_NET("144.76.97.151", 7777);

		private String ip;
		private int port;
	}

	private static Logger LOGGER = LoggerFactory.getLogger(CasperSdkApplicationTests.class);

	private static CasperService casperServiceMainnet;
	private static CasperService casperServiceTestnet;

	@BeforeAll
	public static void setUp() throws MalformedURLException {
		casperServiceMainnet = CasperService.usingPeer(CasperNetwork.MAIN_NET.getIp(), CasperNetwork.MAIN_NET.getPort());
		casperServiceTestnet = CasperService.usingPeer(CasperNetwork.TEST_NET.getIp(), CasperNetwork.TEST_NET.getPort());
	}

	/**
	 * Test if get block matches requested by height
	 */
	@Test
	void testIfBlockReturnedMatchesRequestedByHeight() {
		int blocks_to_check = 3;
		for (int i = 0; i < blocks_to_check; i++) {
			JsonBlockData result = casperServiceMainnet.getBlock(new BlockIdentifierByHeight(i));
			assertEquals(result.getBlock().getHeader().getHeight(), i);
		}
	}

	/**
	 * Test if get block matches requested by hash
	 */
	@Test
	void testIfBlockReturnedMatchesRequestedByHash() {
		int blocks_to_check = 3;
		for (int i = 0; i < blocks_to_check; i++) {
			LOGGER.debug(String.format("Testing with block height %d", i));
			JsonBlockData resultByHeight = casperServiceMainnet.getBlock(new BlockIdentifierByHeight(i));
			String hash = resultByHeight.getBlock().getHash();
			JsonBlockData resultByHash = casperServiceMainnet.getBlock(new BlockIdentifierByHash(hash));

			assertEquals(resultByHash.getBlock().getHash(), hash);
		}
	}

	/**
	 * Test if get public key serialization is correct
	 */
	@Test
	void testFirstBlocksPublicKeySerialization() throws DecoderException {
		JsonBlockData result = casperServiceMainnet.getBlock(new BlockIdentifierByHeight(0));
		PublicKey key = result.getBlock().getBody().getProposer();

		assertEquals(Algorithm.ED25519, key.getAlgorithm());
		assertEquals("2bac1d0ff9240ff0b7b06d555815640497861619ca12583ddef434885416e69b",
				new String(Hex.encodeHex(key.getKey(), true)));
	}

	/**
	 * Retrieve peers list and assert it has elements
	 * 
	 * @throws Throwable
	 */
	@Test
	void retrieveNonEmptyListOfPeers() {
		PeerData peerData = casperServiceMainnet.getPeerData();

		assertNotNull(peerData);
		assertFalse(peerData.getPeers().isEmpty());
	}

	@Test
	void retrieveLastBlock() {
		JsonBlockData blockData = casperServiceMainnet.getBlock();

		assertNotNull(blockData);
	}

	// "7d9462f91e33e3df16b1eadd5d3767af942470cc3422971377594cf82e7e4fc4"
	// "d8f921f792064202749cbd286379acb6c0fdb3d3d0526c2c7e92c03ff2c26c1d"
	@Test
	void getBlockByHash() {
		JsonBlockData blockData = casperServiceMainnet.getBlock(
				new BlockIdentifierByHash("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8"));

		assertNotNull(blockData);
		JsonBlock block = blockData.getBlock();
		assertEquals("0000000000000000000000000000000000000000000000000000000000000000",
				block.getHeader().getParentHash());
		assertEquals(0, block.getHeader().getHeight());
	}

	@Test
	void getBlockByHeight() {
		JsonBlockData blockData = casperServiceMainnet.getBlock(new BlockIdentifierByHeight(0));
		JsonBlock block = blockData.getBlock();
		assertEquals("0000000000000000000000000000000000000000000000000000000000000000",
				block.getHeader().getParentHash());
		assertEquals("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8", block.getHash());
		assertNotNull(blockData);
	}

	@Test
	void retrieveLastBlockTransfers() {
		TransferData transferData = casperServiceMainnet.getBlockTransfers();

		assertNotNull(transferData);
	}

	@Test
	void getTransferByHeight() {
		TransferData transferData = casperServiceMainnet.getBlockTransfers(new BlockIdentifierByHeight(198148));

		assertNotNull(transferData);
		assertEquals(1, transferData.getTransfers().size());
		Transfer transaction = transferData.getTransfers().get(0);
		assertEquals("c22fab5364c793bb859fd259b808ea4c101be8421b7d638dc8f52490ab3c3539", transaction.getDeployHash());
		assertEquals("account-hash-2363d9065b1ecc26f50f108c22c8f3bbe6a891c81e37e0e454c68370708a6937",
				transaction.getFrom());
		assertEquals("account-hash-288797af5b4eeb5d4f36bd228b2e6479a77a27e808597ced1a7d6afe4c29febc",
				transaction.getTo());
		assertEquals(BigInteger.valueOf(597335999990000L), transaction.getAmount());
	}

	@Test
	void getTransferByHash() {
		TransferData transferData = casperServiceMainnet.getBlockTransfers(
				new BlockIdentifierByHash("db9820938ee64c7037f13ea05ab8fe7576215c3a62b02ed35c2564c2138eeb57"));

		assertNotNull(transferData);
		assertEquals(1, transferData.getTransfers().size());
		Transfer transaction = transferData.getTransfers().get(0);
		assertEquals("c22fab5364c793bb859fd259b808ea4c101be8421b7d638dc8f52490ab3c3539", transaction.getDeployHash());
		assertEquals("account-hash-2363d9065b1ecc26f50f108c22c8f3bbe6a891c81e37e0e454c68370708a6937",
				transaction.getFrom());
		assertEquals("account-hash-288797af5b4eeb5d4f36bd228b2e6479a77a27e808597ced1a7d6afe4c29febc",
				transaction.getTo());
		assertEquals(BigInteger.valueOf(597335999990000L), transaction.getAmount());
	}

	@Test
	void retrieveLastBlockStateRootHash() {
		StateRootHashData stateRootData = casperServiceMainnet.getStateRootHash();

		assertNotNull(stateRootData);
	}

	@Test
	void getStateRootHashByHeight() {
		StateRootHashData stateRootHashData = casperServiceMainnet.getStateRootHash(new BlockIdentifierByHeight(0));
		assertNotNull(stateRootHashData);
		assertEquals("8e22e3983d5ca9bcf9804bd3a6724b8c24effdf317a1d9c05175125a1bf8b679",
				stateRootHashData.getStateRootHash());
	}

	@Test
	void getStateRootHashByHash() {
		StateRootHashData stateRootHashData = casperServiceMainnet.getStateRootHash(
				new BlockIdentifierByHash("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8"));

		assertNotNull(stateRootHashData);
		assertEquals("8e22e3983d5ca9bcf9804bd3a6724b8c24effdf317a1d9c05175125a1bf8b679",
				stateRootHashData.getStateRootHash());
	}

	@Test
	void getBlockState() {
		// FIXME: This test fails on mainnet, no root hash.
		String stateRootHash = "c0eb76e0c3c7a928a0cb43e82eb4fad683d9ad626bcd3b7835a466c0587b0fff";
		String key = "account-hash-a9efd010c7cee2245b5bad77e70d9beb73c8776cbe4698b2d8fdf6c8433d5ba0";
		List<String> path = Arrays.asList("special_value");
		StoredValueData result = casperServiceTestnet.getStateItem(stateRootHash, key, path);

		assertTrue(result.getCasperStoredValue().getClValue().getValue() instanceof String);
		// Should be equal incoming parsed
		assertEquals(result.getCasperStoredValue().getClValue().getValue(),
				result.getCasperStoredValue().getClValue().getParsed());
	}

}
