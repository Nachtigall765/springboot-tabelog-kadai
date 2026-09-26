package com.example.kadai_002.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.ReservationRepository;
import com.example.kadai_002.repository.ShopRepository;
import com.example.kadai_002.repository.UserRepository;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;
	private final ShopRepository shopRepository;

	public ReservationController(
	        ReservationRepository reservationRepository,
	        UserRepository userRepository,
	        ShopRepository shopRepository) {

	    this.reservationRepository = reservationRepository;
	    this.userRepository = userRepository;
	    this.shopRepository = shopRepository;
	}


	@GetMapping("/reservations")
	public String index(Principal principal, Model model) {

	    User user = userRepository.findByEmail(principal.getName())
	            .orElseThrow();

	    List<Reservation> reservations =
	            reservationRepository.findByUser(user);

	    model.addAttribute("reservations", reservations);

	    return "reservations/index";
	}

	@GetMapping("/reservations/create")
	public String create(@RequestParam Long shopId, Model model) {

	    Shop shop = shopRepository.findById(shopId)
	            .orElseThrow();

	    model.addAttribute("shop", shop);

	    return "reservations/create";
	}
    @PostMapping("/reservations/confirm")
    public String confirm(
            @RequestParam Long shopId,
            @RequestParam LocalDateTime reservationAt,
            @RequestParam Integer numberOfPeople,
            Model model) {

        model.addAttribute("shopId", shopId);
        model.addAttribute("reservationAt", reservationAt);
        model.addAttribute("numberOfPeople", numberOfPeople);

        return "reservations/confirm";
    }
    @PostMapping("/reservations")
    public String store(
            @RequestParam Long shopId,
            @RequestParam LocalDateTime reservationAt,
            @RequestParam Integer numberOfPeople,
            Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow();

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShop(shop);
        reservation.setReservationAt(reservationAt);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);

        return "reservations/complete";
    }
    @PostMapping("/reservations/{id}/delete")
    public String delete(
            @PathVariable Long id,
            Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow();

        // 他人の予約を削除できないようにする
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("この予約はキャンセルできません");
        }

        reservationRepository.delete(reservation);

        return "redirect:/reservations";
    }

}