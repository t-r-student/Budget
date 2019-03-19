package de.budget.project.repository;

import de.budget.project.model.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet getWalletById(Long id);

    Wallet getWalletByUserId(Long id);

}