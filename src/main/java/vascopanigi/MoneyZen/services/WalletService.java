package vascopanigi.MoneyZen.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.*;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.exceptions.UnauthorizedException;
import vascopanigi.MoneyZen.payloads.wallet.*;
import vascopanigi.MoneyZen.repositories.*;
import vascopanigi.MoneyZen.security.JWTTools;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private SharedWalletRepository sharedWalletRepository;

    @Autowired
    private PersonalWalletRepository personalWalletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    public PersonalWallet savePersonalWallet(NewWalletDTO body, @AuthenticationPrincipal User currentUser){
        PersonalWallet personalWallet = new PersonalWallet(body.name(), currentUser);
        // Save the wallet
        return walletRepository.save(personalWallet);
    }

    public SharedWallet saveSharedWallet(NewWalletDTO body, @AuthenticationPrincipal User currentUser){
        SharedWallet sharedWallet = new SharedWallet(body.name());
        // Save the wallet
        Set<User> usersSet = new HashSet<>();
        usersSet.add(currentUser);
        sharedWallet.setUsers(usersSet);
        return walletRepository.save(sharedWallet);
    }

    public Wallet getWalletById(UUID walletId, User currentUser) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet not found."));

        if (wallet instanceof PersonalWallet personalWallet) {
            if (!personalWallet.getUser().getId().equals(currentUser.getId())) {
                throw new UnauthorizedException("You are not authorized to view this wallet.");
            }
        } else if (wallet instanceof SharedWallet sharedWallet) {
            boolean isMember = sharedWallet.getUsers().stream()
                    .anyMatch(user -> user.getId().equals(currentUser.getId()));
            if (!isMember) {
                throw new UnauthorizedException("You are not authorized to view this wallet.");
            }
        }
        return wallet;
    }

    public List<Wallet> findAllWalletsByUserId(UUID userId) {
        List<PersonalWallet> personalWallets = walletRepository.findPersonalWalletsByUserId(userId);
        List<SharedWallet> sharedWallets = walletRepository.findSharedWalletsByUserId(userId);

        List<Wallet> allWallets = new ArrayList<>();
        allWallets.addAll(personalWallets);
        allWallets.addAll(sharedWallets);

        return allWallets;
    }

    public SharedWallet addUserToSharedWallet(AddUserToSharedWalletDTO body, User currentUser){
        SharedWallet requestedWallet = this.sharedWalletRepository.findById(body.walletId()).orElseThrow(()-> new NotFoundException("Wallet with Id: " + body.walletId() + " not found."));
        Set<User> walletUsers = requestedWallet.getUsers();
        if(walletUsers.stream().anyMatch(user -> user.getId().equals(currentUser.getId()))){
            //meglio dare un errore generico per evitare possibili falle di sicurezza in cui un utente malintenzionato voglia scoprire se qualcuno
            //fa parte di uno shared wallet per secondi fini
            throw new UnauthorizedException("Operation not valid.");
        }
        walletUsers.add(currentUser);
        requestedWallet.setUsers(walletUsers);
        return sharedWalletRepository.save(requestedWallet);
    }

    public Wallet findById(UUID id) {
        return this.walletRepository.findById(id).orElseThrow(() -> new NotFoundException("Wallet with id: " + id +" not found."));
    }

    public void findByIdAndDelete(UUID walletId, User currentUser) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet not found."));

        if (wallet instanceof PersonalWallet personalWallet) {
            if (!personalWallet.getUser().getId().equals(currentUser.getId())) {
                throw new UnauthorizedException("You are not authorized to view this wallet.");
            } else {
                Set<PersonalWallet> usersPersonalWallets = personalWallet.getUser().getPersonalWallets();
                usersPersonalWallets.removeIf(singleWallet -> singleWallet.getId().equals(walletId));
                logger.info("Personal wallet removed from user's collection: " + walletId);
            }
        } else if (wallet instanceof SharedWallet sharedWallet) {
            boolean isMember = sharedWallet.getUsers().stream()
                    .anyMatch(user -> user.getId().equals(currentUser.getId()));
            if (!isMember) {
                throw new UnauthorizedException("You are not authorized to view this wallet.");
            }
            Set<SharedWallet> usersSharedWallets = sharedWallet.getUsers().stream()
                    .flatMap(user -> user.getSharedWallets().stream())
                    .collect(Collectors.toSet());
            usersSharedWallets.removeIf(singleWallet -> singleWallet.getId().equals(walletId));
            logger.info("Shared wallet removed from user's collection: " + walletId);
        }

        // Delete associated transactions
        if (wallet.getTransactions() != null && !wallet.getTransactions().isEmpty()) {
            logger.info("Deleting associated transactions for wallet: " + walletId);
            transactionRepository.deleteAll(wallet.getTransactions());
        }

        if (wallet instanceof PersonalWallet) {
            personalWalletRepository.delete((PersonalWallet) wallet);
        } else if (wallet instanceof SharedWallet) {
            sharedWalletRepository.delete((SharedWallet) wallet);
        }

        // Log the wallet deletion
        logger.info("Wallet deleted: " + walletId);
    }
}
