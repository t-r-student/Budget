package de.budget.project.services.impl;

import de.budget.project.model.entites.Wallet;
import de.budget.project.repository.WalletRepository;
import de.budget.project.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Override
    public void insertWallet(Long userId, Integer currencyId) {
        walletRepository.insertWallet(userId, currencyId);
    }

    @Override
    public Wallet getWalletById(Long id) {
        return walletRepository.getWalletById(id);
    }

    @Override
    public List<Wallet> getAllByUserId(Long userId) {
        return walletRepository.getAllByUserId(userId);
    }
}