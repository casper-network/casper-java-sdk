package com.syntifi.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;

import com.syntifi.casper.sdk.filter.block.CasperBlockByHashFilter;
import com.syntifi.casper.sdk.filter.block.CasperBlockByHeightFilter;
import com.syntifi.casper.sdk.service.CasperService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
class CasperSdkApplicationTests {
	private static String ip = "144.91.79.58";
	private static int port = 7777;

	private static CasperService casperService;

	@BeforeAll
	public static void setUp() throws MalformedURLException {
		casperService = CasperService.withConfig(ip, port);
	}

	/**
	 * Retrieve peers list and assert it has elements
	 * 
	 * @throws Throwable
	 */
	@Test
	void retrieveNonEmptyListOfPeers() {
		var peerData = casperService.getPeerData();

		assertNotNull(peerData);
		assertFalse(peerData.getPeers().isEmpty());
	}

	@Test
	void retrieveLastBlock() {
		var blockData = casperService.getBlock();

		assertNotNull(blockData);
	}

	//"7d9462f91e33e3df16b1eadd5d3767af942470cc3422971377594cf82e7e4fc4"
	//"d8f921f792064202749cbd286379acb6c0fdb3d3d0526c2c7e92c03ff2c26c1d"
	@Test
	void retrieveBlockByHash() {
		var blockData = casperService.getBlock(new CasperBlockByHashFilter("d8f921f792064202749cbd286379acb6c0fdb3d3d0526c2c7e92c03ff2c26c1d"));

		assertNotNull(blockData);
	}

	@Test
	void retrieveBlockByHeight() {
		var blockData = casperService.getBlock(new CasperBlockByHeightFilter(5));

		assertNotNull(blockData);
	}

}
