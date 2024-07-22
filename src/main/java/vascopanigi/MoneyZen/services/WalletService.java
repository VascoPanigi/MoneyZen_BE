package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.PersonalWallet;
import vascopanigi.MoneyZen.entities.SharedWallet;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.entities.Wallet;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.exceptions.UnauthorizedException;
import vascopanigi.MoneyZen.payloads.user.UserDTO;
import vascopanigi.MoneyZen.payloads.wallet.NewWalletDTO;
import vascopanigi.MoneyZen.payloads.wallet.PersonalWalletDTO;
import vascopanigi.MoneyZen.payloads.wallet.SharedWalletDTO;
import vascopanigi.MoneyZen.payloads.wallet.WalletDTO;
import vascopanigi.MoneyZen.repositories.PersonalWalletRepository;
import vascopanigi.MoneyZen.repositories.SharedWalletRepository;
import vascopanigi.MoneyZen.repositories.WalletRepository;
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

//    public List<WalletDTO> getAllUserWallets(User currentUser) {
//        List<WalletDTO> walletDTOs = new ArrayList<>();
//
//        // Fetch personal wallets
//        List<PersonalWallet> personalWallets = personalWalletRepository.findByUser(currentUser);
//        for (PersonalWallet personalWallet : personalWallets) {
//            PersonalWalletDTO dto = new PersonalWalletDTO(
//                    personalWallet.getId(),
//                    personalWallet.getName(),
//                    personalWallet.getBalance()
//            );
//            walletDTOs.add(dto);
//        }
//
//        // Fetch shared wallets
//        List<SharedWallet> sharedWallets = sharedWalletRepository.findByUsersContains(currentUser);
//        for (SharedWallet sharedWallet : sharedWallets) {
//            Set<UserDTO> userDTOs = sharedWallet.getUsers().stream().map(user ->
//                    new UserDTO(
//                            user.getId(),
//                            user.getName(),
//                            user.getSurname(),
//                            user.getUsername(),
//                            user.getEmail(),
//                            user.getAvatarURL()
//                    )
//            ).collect(Collectors.toSet());
//
//            SharedWalletDTO dto = new SharedWalletDTO(
//                    sharedWallet.getId(),
//                    sharedWallet.getName(),
//                    sharedWallet.getBalance(),
//                    userDTOs
//            );
//            walletDTOs.add(dto);
//        }
//
//        return walletDTOs;
//    }


}
