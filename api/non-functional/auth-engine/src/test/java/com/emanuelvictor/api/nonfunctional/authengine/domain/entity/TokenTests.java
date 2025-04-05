package com.emanuelvictor.api.nonfunctional.authengine.domain.entity;

import com.emanuelvictor.api.nonfunctional.authengine.domain.AbstractsTests;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.Token;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.TokenImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TokenTests extends AbstractsTests {

    private static final String TOKEN_NAME = "token";

    /**
     *
     */
    @Test
    public void recursiveTokensHandler() {

        //Creates two composites containing the tokens
        final Token token1 = new TokenImpl(TOKEN_VALUES[1], TOKEN_NAME);
        final Token token2 = new TokenImpl(TOKEN_VALUES[2], TOKEN_NAME);
        token1.add(token2);

        final Token token3 = new TokenImpl(TOKEN_VALUES[3], TOKEN_NAME);
        token1.add(token3);

        final Token token4 = new TokenImpl(TOKEN_VALUES[4], TOKEN_NAME);
        token3.add(token4);
        final Token token5 = new TokenImpl(TOKEN_VALUES[6], TOKEN_NAME);
        token4.add(token5);
        final Token token7 = new TokenImpl(TOKEN_VALUES[7], TOKEN_NAME);
        token4.add(token7);


        final Token token6 = new TokenImpl(TOKEN_VALUES[5], TOKEN_NAME);
        token3.add(token6);
        final Token token8 = new TokenImpl(TOKEN_VALUES[8], TOKEN_NAME);
        token6.add(token8);
        final Token token9 = new TokenImpl(TOKEN_VALUES[9], TOKEN_NAME);
        token6.add(token9);

        Assertions.assertFalse(token1.isRevoked());
        Assertions.assertFalse(token2.isRevoked());
        Assertions.assertFalse(token3.isRevoked());
        Assertions.assertFalse(token4.isRevoked());
        Assertions.assertFalse(token5.isRevoked());
        Assertions.assertFalse(token7.isRevoked());
        Assertions.assertFalse(token6.isRevoked());
        Assertions.assertFalse(token8.isRevoked());
        Assertions.assertFalse(token9.isRevoked());

        token5.printFromRoot();

        token5.revoke();

        Assertions.assertTrue(token1.isRevoked());
        Assertions.assertTrue(token2.isRevoked());
        Assertions.assertTrue(token3.isRevoked());
        Assertions.assertTrue(token4.isRevoked());
        Assertions.assertTrue(token6.isRevoked());
        Assertions.assertTrue(token7.isRevoked());
        Assertions.assertTrue(token5.isRevoked());
        Assertions.assertTrue(token8.isRevoked());
        Assertions.assertTrue(token9.isRevoked());

    }

    /**
     *
     */
    @Test
    public void addTokenMustPass() {

        final Token token1 = new TokenImpl(TOKEN_VALUES[1], TOKEN_NAME);

        final Token token2 = new TokenImpl(TOKEN_VALUES[2], TOKEN_NAME);
        token1.add(token2);

        final Token token3 = new TokenImpl(TOKEN_VALUES[3], TOKEN_NAME);
        token1.add(token3);

        final Token token4 = new TokenImpl(TOKEN_VALUES[4], TOKEN_NAME);
        token2.add(token4);

        final Token token5 = new TokenImpl(TOKEN_VALUES[5], TOKEN_NAME);
        token1.add(token5);

        Assertions.assertNotNull(token4.getNext(), "The next token must be non null");
        Assertions.assertEquals(token5, token4.getNext().orElseThrow(), "The next token must be equals to token5");

        Assertions.assertNotNull(token2.getNext(), "The next token must be non null");
        Assertions.assertEquals(token3, token2.getNext().orElseThrow(), "The next token must be equals to token3");
        Assertions.assertNotNull(token2.getNext().orElseThrow().getNext().orElseThrow().getNext(), "The next token must be non null");
        Assertions.assertEquals(token5, token2.getNext().orElseThrow().getNext().orElseThrow().getNext().orElseThrow(), "The next token must be equals to token5");
    }

    /**
     *
     */
    @Test
    public void countTests() {
        final Token token1 = new TokenImpl(TOKEN_VALUES[1], TOKEN_NAME);

        final Token token2 = new TokenImpl(TOKEN_VALUES[2], TOKEN_NAME);
        token1.add(token2);

        final Token token3 = new TokenImpl(TOKEN_VALUES[3], TOKEN_NAME);
        token1.add(token3);

        final Token token4 = new TokenImpl(TOKEN_VALUES[4], TOKEN_NAME);
        token2.add(token4);

        final Token token5 = new TokenImpl(TOKEN_VALUES[5], TOKEN_NAME);
        token1.add(token5);

        final int expected = 5;

        Assertions.assertEquals(expected, token1.count());

        Assertions.assertEquals(expected, token4.count());

        Assertions.assertEquals(expected, token3.count());

        Assertions.assertEquals(expected, token5.count());

    }

    /**
     *
     */
    @Test
    public void findByValue() {

        Assertions.assertNotNull(dataSet().stream().findFirst().orElseThrow().findByValue(TOKEN_VALUES[2]));

        Assertions.assertNotNull(dataSet().stream().findFirst().orElseThrow().findByValue(TOKEN_VALUES[3]));

    }

    /**
     *
     */
    @Test
    public void getRoot() {
        Assertions.assertEquals(dataSet().stream().findFirst().orElseThrow().findByValue(TOKEN_VALUES[4]).orElseThrow().getRoot().orElseThrow().getValue(), TOKEN_VALUES[1]);
    }

    /**
     *
     */
    @Test
    public void getLeaf() {
        Assertions.assertEquals(dataSet().stream().findFirst().orElseThrow().findByValue(TOKEN_VALUES[4]).orElseThrow().getLeaf().orElseThrow().getValue(), TOKEN_VALUES[9]);
    }

    /**
     *
     */
    @Test
    public void getAccessToken() {
        final Token token = dataSet().stream().findFirst().orElseThrow().findByValue(TOKEN_VALUES[4]).orElseThrow();
        Assertions.assertEquals(token.getAccess().orElseThrow().getValue(), TOKEN_VALUES[token.count() - 1]);
    }

    /**
     *
     */
    @Test
    public void getRefreshToken() {
        final Token token = dataSet().stream().findFirst().orElseThrow().findByValue(TOKEN_VALUES[4]).orElseThrow();
        Assertions.assertEquals(token.getRefresh().orElseThrow().getValue(), TOKEN_VALUES[token.count()]);
    }

    /**
     *
     */
    public static final String[] TOKEN_VALUES = new String[]{"token0", "token1", "token2", "token3", "token4", "token5", "token6", "token7", "token8", "token9", "token10", "token11", "token12", "token13", "token14", "token15", "token16", "token17", "token18", "token19"};

    /**
     * @return Set<IToken>
     */
    public static Set<Token> dataSet() {

        //Creates two composites containing the tokens
        final Token token1 = new TokenImpl(TOKEN_VALUES[1], TOKEN_NAME);
        final Token token2 = new TokenImpl(TOKEN_VALUES[2], TOKEN_NAME);
        token1.add(token2);

        final Token token3 = new TokenImpl(TOKEN_VALUES[3], TOKEN_NAME);
        token1.add(token3);

        final Token token4 = new TokenImpl(TOKEN_VALUES[4], TOKEN_NAME);
        token3.add(token4);
        final Token token5 = new TokenImpl(TOKEN_VALUES[6], TOKEN_NAME);
        token4.add(token5);
        final Token token7 = new TokenImpl(TOKEN_VALUES[7], TOKEN_NAME);
        token4.add(token7);


        final Token token6 = new TokenImpl(TOKEN_VALUES[5], TOKEN_NAME);
        token3.add(token6);
        final Token token8 = new TokenImpl(TOKEN_VALUES[8], TOKEN_NAME);
        token6.add(token8);
        final Token token9 = new TokenImpl(TOKEN_VALUES[9], TOKEN_NAME);
        token6.add(token9);

        return Set.of(token1);

    }

    /**
     *
     */
    @Test
    void extractNameFromTokenMustPass() {
        final Token token1 = new TokenImpl("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDM5MTE1MjEsInVzZXJfbmFtZSI6ImFkbWluQGFkbWluLmNvbSIsImF1dGhvcml0aWVzIjpbInJvb3QvYWNjZXNzLW1hbmFnZXIvdXNlcnMvZGVsZXRlIiwicm9vdC9hY2Nlc3MtbWFuYWdlci9ncm91cHMvZGVsZXRlIiwicm9vdC9hY2Nlc3MtbWFuYWdlci91c2VycyIsInJvb3QvYWNjZXNzLW1hbmFnZXIvYXBwbGljYXRpb25zL3B1dC9hY3RpdmF0ZSIsInJvb3QvYWNjZXNzLW1hbmFnZXIvdXNlcnMvcG9zdCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvZ3JvdXBzIiwicm9vdC9hY2Nlc3MtbWFuYWdlci9hcHBsaWNhdGlvbnMvcHV0Iiwicm9vdC9hY2Nlc3MtbWFuYWdlci91c2Vycy9wdXQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2dyb3Vwcy9nZXQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2dyb3Vwcy9wb3N0Iiwicm9vdC9hY2Nlc3MtbWFuYWdlci91c2Vycy9wdXQvYWN0aXZhdGUiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2dyb3Vwcy9wdXQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2FwcGxpY2F0aW9ucy9nZXQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2dyb3Vwcy9wdXQvYWN0aXZhdGUiLCJyb290L2FjY2Vzcy1tYW5hZ2VyIiwicm9vdCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvYXBwbGljYXRpb25zL2RlbGV0ZSIsInJvb3QvYWNjZXNzLW1hbmFnZXIvYXBwbGljYXRpb25zL3B1dC9jaGFuZ2UtcGFzc3dvcmQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2FwcGxpY2F0aW9ucy9wb3N0Iiwicm9vdC9hY2Nlc3MtbWFuYWdlci91c2Vycy9nZXQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL3VzZXJzL3B1dC9jaGFuZ2UtcGFzc3dvcmQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2FwcGxpY2F0aW9ucyJdLCJqdGkiOiIxOGJhNGE3ZC0zN2NkLTRkZTMtYmM4My1kODE2YzM3NzYwOTEiLCJjbGllbnRfaWQiOiJicm93c2VyIiwic2NvcGUiOlsicm9vdC9hY2Nlc3MtbWFuYWdlci91c2Vycy9kZWxldGUiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2dyb3Vwcy9kZWxldGUiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL3VzZXJzIiwicm9vdC9hY2Nlc3MtbWFuYWdlci9hcHBsaWNhdGlvbnMvcHV0L2FjdGl2YXRlIiwicm9vdC9hY2Nlc3MtbWFuYWdlci91c2Vycy9wb3N0Iiwicm9vdC9hY2Nlc3MtbWFuYWdlci9ncm91cHMiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL2FwcGxpY2F0aW9ucy9wdXQiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL3VzZXJzL3B1dCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvZ3JvdXBzL2dldCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvZ3JvdXBzL3Bvc3QiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL3VzZXJzL3B1dC9hY3RpdmF0ZSIsInJvb3QvYWNjZXNzLW1hbmFnZXIvZ3JvdXBzL3B1dCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvYXBwbGljYXRpb25zL2dldCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvZ3JvdXBzL3B1dC9hY3RpdmF0ZSIsInJvb3QvYWNjZXNzLW1hbmFnZXIiLCJyb290Iiwicm9vdC9hY2Nlc3MtbWFuYWdlci9hcHBsaWNhdGlvbnMvZGVsZXRlIiwicm9vdC9hY2Nlc3MtbWFuYWdlci9hcHBsaWNhdGlvbnMvcHV0L2NoYW5nZS1wYXNzd29yZCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvYXBwbGljYXRpb25zL3Bvc3QiLCJyb290L2FjY2Vzcy1tYW5hZ2VyL3VzZXJzL2dldCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvdXNlcnMvcHV0L2NoYW5nZS1wYXNzd29yZCIsInJvb3QvYWNjZXNzLW1hbmFnZXIvYXBwbGljYXRpb25zIl19.ea_AstAbyaO8019VzK0XGAWySrUJOJqq8LKuEBc-sLg", TOKEN_NAME);
        Assertions.assertEquals(token1.getName(), "admin@admin.com");
    }
}
