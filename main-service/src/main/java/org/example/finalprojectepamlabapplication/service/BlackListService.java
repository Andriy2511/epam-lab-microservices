package org.example.finalprojectepamlabapplication.service;

public interface BlackListService {
    void addToBlacklist(String token);

    boolean isBlacklisted(String token);
}
