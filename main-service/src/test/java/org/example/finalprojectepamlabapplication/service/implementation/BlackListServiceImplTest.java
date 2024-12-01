package org.example.finalprojectepamlabapplication.service.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

public class BlackListServiceImplTest {

    BlackListServiceImpl blackListService;
    Set<String> blacklistedTokens;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        blackListService = new BlackListServiceImpl();

        Field field = BlackListServiceImpl.class.getDeclaredField("blacklistedTokens");
        field.setAccessible(true);
        blacklistedTokens = (Set<String>) field.get(blackListService);

    }

    @Test
    public void addToBlacklistTest(){
        blackListService.addToBlacklist("test");
        Assertions.assertTrue(blacklistedTokens.contains("test"));
    }

    @Test
    public void isBlacklistedTest(){
        blackListService.addToBlacklist("test");
        Assertions.assertTrue(blackListService.isBlacklisted("test"));
    }
}
