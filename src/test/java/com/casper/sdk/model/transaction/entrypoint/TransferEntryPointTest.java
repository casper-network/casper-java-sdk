package com.casper.sdk.model.transaction.entrypoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Unit tests for the {@link TransferEntryPoint} class
 *
 * @author ian@meywood.com
 */
class TransferEntryPointTest {

    @Test
    void activationBidEntryPoint() throws JsonProcessingException {

        String json = "\"ActivateBid\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(ActivateBidEntryPoint.class)));

        assertThat(entryPoint.toString(), is("ActivateBid"));
        assertThat(entryPoint.getByteTag(), is((byte) 8));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void addBidEntryPoint() throws JsonProcessingException {

        String json = "\"AddBid\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(AddBidEntryPoint.class)));

        assertThat(entryPoint.getName(), is("AddBid"));
        assertThat(entryPoint.getByteTag(), is((byte) 3));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void callEntryPoint() throws JsonProcessingException {

        String json = "\"Call\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(CallEntryPoint.class)));

        assertThat(entryPoint.getName(), is("Call"));
        assertThat(entryPoint.getByteTag(), is((byte) 0));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void changeBidPublicKeyEntryPoint() throws JsonProcessingException {

        String json = "\"ChangeBidPublicKey\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(ChangeBidPublicKeyEntryPoint.class)));

        assertThat(entryPoint.getName(), is("ChangeBidPublicKey"));
        assertThat(entryPoint.getByteTag(), is((byte) 9));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void customEntryPoint() throws JsonProcessingException {

        String json = "{\"Custom\":\"I'm a custom entry point\"}";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(CustomEntryPoint.class)));

        assertThat(entryPoint.getName(), is("Custom"));
        assertThat(entryPoint.getByteTag(), is((byte) 1));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void delegateEntryPoint() throws JsonProcessingException {

        String json = "\"Delegate\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(DelegateEntryPoint.class)));

        assertThat(entryPoint.getName(), is("Delegate"));
        assertThat(entryPoint.getByteTag(), is((byte) 5));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void redelegateEntryPoint() throws JsonProcessingException {

        String json = "\"Redelegate\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(RedelegateEntryPoint.class)));

        assertThat(entryPoint.getName(), is("Redelegate"));
        assertThat(entryPoint.getByteTag(), is((byte) 7));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void transferEntryPoint() throws JsonProcessingException {

        String json = "\"Transfer\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(TransferEntryPoint.class)));

        assertThat(entryPoint.getName(), is("Transfer"));
        assertThat(entryPoint.getByteTag(), is((byte) 2));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void undelegateEntryPoint() throws JsonProcessingException {

        String json = "\"Undelegate\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(UndelegateEntryPoint.class)));

        assertThat(entryPoint.getName(), is("Undelegate"));
        assertThat(entryPoint.getByteTag(), is((byte) 6));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }

    @Test
    void withdrawBidEntryPoint() throws JsonProcessingException {

        String json = "\"WithdrawBid\"";
        final TransactionEntryPoint entryPoint = new ObjectMapper().readValue(json, TransactionEntryPoint.class);
        assertThat(entryPoint, is(instanceOf(WithdrawBidEntryPoint.class)));

        assertThat(entryPoint.getName(), is("WithdrawBid"));
        assertThat(entryPoint.getByteTag(), is((byte) 4));
        assertThat(new ObjectMapper().writeValueAsString(entryPoint), is(json));
    }
}
