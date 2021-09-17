package com.syntifi.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import com.syntifi.casper.sdk.filter.block.CasperBlockByHashFilter;
import com.syntifi.casper.sdk.filter.block.CasperBlockByHeightFilter;
import com.syntifi.casper.sdk.model.block.CasperBlock;
import com.syntifi.casper.sdk.model.block.CasperBlockData;
import com.syntifi.casper.sdk.model.peer.CasperPeerData;
import com.syntifi.casper.sdk.model.stateroothash.CasperStateRootHashData;
import com.syntifi.casper.sdk.model.transfer.CasperTransfer;
import com.syntifi.casper.sdk.model.transfer.CasperTransferData;
import com.syntifi.casper.sdk.service.CasperService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link CasperService}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
class CasperSdkApplicationTests {
	private static Logger LOGGER = LoggerFactory.getLogger(CasperSdkApplicationTests.class);

	//MainNet node
	private static String testIP = "195.201.142.76";
	private static int testPort = 7777;

	private static CasperService casperService;

	@BeforeAll
	public static void setUp() throws MalformedURLException {
		casperService = CasperService.usingPeer(testIP, testPort);
	}

	/**
	 * Test if get block matches requested by height
	 */
	@Test
	void testIfBlockReturnedMatchesRequestedByHeight() {
		var blocks_to_check = 3;
		for (var i = 0; i < blocks_to_check; i++) {
			var result = casperService.getBlock(new CasperBlockByHeightFilter(i));
			assertEquals(result.getBlock().getHeader().getHeight(), i);
		}
	}

	/**
	 * Test if get block matches requested by hash
	 */
	@Test
	void testIfBlockReturnedMatchesRequestedByHash() {
		var blocks_to_check = 3;		
		for (var i = 0; i < blocks_to_check; i++) {
			LOGGER.debug(String.format("Testing with block height %d", i));
			var resultByHeight = casperService.getBlock(new CasperBlockByHeightFilter(i));
			var hash = resultByHeight.getBlock().getHash();
			var resultByHash = casperService.getBlock(new CasperBlockByHashFilter(hash));

			assertEquals(resultByHash.getBlock().getHash(), hash);
		}
	}

	/**
	 * Retrieve peers list and assert it has elements
	 * 
	 * @throws Throwable
	 */
	@Test
	void retrieveNonEmptyListOfPeers() {
		CasperPeerData peerData = casperService.getPeerData();

		assertNotNull(peerData);
		assertFalse(peerData.getPeers().isEmpty());
	}

	@Test
	void retrieveLastBlock() {
		CasperBlockData blockData = casperService.getBlock();

		assertNotNull(blockData);
	}

	// "7d9462f91e33e3df16b1eadd5d3767af942470cc3422971377594cf82e7e4fc4"
	// "d8f921f792064202749cbd286379acb6c0fdb3d3d0526c2c7e92c03ff2c26c1d"
	@Test
	void getBlockByHash() {
		CasperBlockData blockData = casperService.getBlock(
				new CasperBlockByHashFilter("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8"));

		assertNotNull(blockData);
		CasperBlock block = blockData.getBlock();
		assertEquals("0000000000000000000000000000000000000000000000000000000000000000", block.getHeader().getParentHash());
		assertEquals(0, block.getHeader().getHeight());
	}

	@Test
	void getBlockByHeight() {
		CasperBlockData blockData = casperService.getBlock(new CasperBlockByHeightFilter(0));
		CasperBlock block = blockData.getBlock();
		assertEquals("0000000000000000000000000000000000000000000000000000000000000000", block.getHeader().getParentHash());
		assertEquals("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8", block.getHash());
		assertNotNull(blockData);
	}

	@Test
	void retrieveLastBlockTransfers() {
		CasperTransferData transferData = casperService.getBlockTransfers();

		assertNotNull(transferData);
	}

	@Test
	void getTransferByHeight() {
		CasperTransferData transferData = casperService.getBlockTransfers(new CasperBlockByHeightFilter(198148));

		assertNotNull(transferData);
		assertEquals(1, transferData.getTransfers().size());
		CasperTransfer transaction = transferData.getTransfers().get(0);
		assertEquals("c22fab5364c793bb859fd259b808ea4c101be8421b7d638dc8f52490ab3c3539", transaction.getDeployHash());
		assertEquals("account-hash-2363d9065b1ecc26f50f108c22c8f3bbe6a891c81e37e0e454c68370708a6937", transaction.getFrom());
		assertEquals("account-hash-288797af5b4eeb5d4f36bd228b2e6479a77a27e808597ced1a7d6afe4c29febc", transaction.getTo());
		assertEquals(597335999990000., transaction.getAmount() , 1E-5);
	}

	@Test
	void getTransferByHash() {
		CasperTransferData transferData = casperService.getBlockTransfers(
				new CasperBlockByHashFilter("db9820938ee64c7037f13ea05ab8fe7576215c3a62b02ed35c2564c2138eeb57"));

		assertNotNull(transferData);
		assertEquals(1, transferData.getTransfers().size());
		CasperTransfer transaction = transferData.getTransfers().get(0);
		assertEquals("c22fab5364c793bb859fd259b808ea4c101be8421b7d638dc8f52490ab3c3539", transaction.getDeployHash());
		assertEquals("account-hash-2363d9065b1ecc26f50f108c22c8f3bbe6a891c81e37e0e454c68370708a6937", transaction.getFrom());
		assertEquals("account-hash-288797af5b4eeb5d4f36bd228b2e6479a77a27e808597ced1a7d6afe4c29febc", transaction.getTo());
		assertEquals(597335999990000., transaction.getAmount(), 1E-5);
	}

	@Test
	void retrieveLastBlockStateRootHash() {
		CasperStateRootHashData stateRootData = casperService.getStateRootHash();

		assertNotNull(stateRootData);
	}

	@Test
	void getStateRootHashByHeight() {
		CasperStateRootHashData stateRootHashData = casperService.getStateRootHash(new CasperBlockByHeightFilter(0));
		assertNotNull(stateRootHashData);
		assertEquals("8e22e3983d5ca9bcf9804bd3a6724b8c24effdf317a1d9c05175125a1bf8b679", stateRootHashData.getStateRootHash());
	}

	@Test
	void getStateRootHashByHash() {
		CasperStateRootHashData stateRootHashData = casperService.getStateRootHash(
				new CasperBlockByHashFilter("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8"));

		assertNotNull(stateRootHashData);
		assertEquals("8e22e3983d5ca9bcf9804bd3a6724b8c24effdf317a1d9c05175125a1bf8b679", stateRootHashData.getStateRootHash());
	}
	/*
	@Test
	void getBlockState() {
		var stateRootHash = "c0eb76e0c3c7a928a0cb43e82eb4fad683d9ad626bcd3b7835a466c0587b0fff";
		var key = "account-hash-a9efd010c7cee2245b5bad77e70d9beb73c8776cbe4698b2d8fdf6c8433d5ba0";
		List<String> path = Arrays.asList("special_value");
		var result = casperService.getStateItem(stateRootHash, key, path);
<<<<<<< HEAD

=======
		
>>>>>>> 4ac42dbb5355e0fa30ea0da7a1420f9ddabf87ec
		assertNotNull(result);
	}
	*/
}
