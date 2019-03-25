package de.budget.project.repository;

import de.budget.project.model.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet getWalletById(Long id);

    List<Wallet> getAllByUserId(Long userId);
}