package com.emanuelvictor.api.nonfunctional.authengine.domain;


import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories.JwtAccessTokenConverter;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories.TokenRepository;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories.TokenRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
public abstract class AbstractsTests {

    /**
     *
     */
    protected TokenRepository tokenStore;

    /**
     *
     */
//    @Before before to junit
    @BeforeEach
    public void beforeTests() {
        tokenStore = new TokenRepositoryImpl(new JwtAccessTokenConverter());
    }

}
