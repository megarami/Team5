package com.tim5.demo.Controller;

import com.tim5.demo.entity.Hotel;
import com.tim5.demo.entity.Reservation;
import com.tim5.demo.entity.Users;
import com.tim5.demo.repository.HotelRepository;
import com.tim5.demo.repository.ReservationRepository;
import com.tim5.demo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UsersRepository usersRepository;

    //GET ALL RESERVATIONS
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public String findAll(Model model){

        List<Reservation> reservations = this.reservationRepository.findAll();

        List<Double> startLongitude = new ArrayList<Double>();
        List<Double> startLatitude = new ArrayList<Double>();
        List<Double> endLongitude = new ArrayList<Double>();
        List<Double> endLatitude = new ArrayList<Double>();

        for(int i = 0; i < reservations.size(); i++){
            startLongitude.add(reservations.get(i).getUsers().getLongitude());
            startLatitude.add(reservations.get(i).getUsers().getLatitude());
            endLongitude.add(reservations.get(i).getHotel().getLongitude());
            endLatitude.add(reservations.get(i).getHotel().getLatitude());
        }

        model.addAttribute("startLongitude", startLongitude);
        model.addAttribute("startLatitude", startLatitude);
        model.addAttribute("endLongitude",endLongitude);
        model.addAttribute("endLatitude", endLatitude);
        model.addAttribute("reservationNumber", startLatitude.size());
        model.addAttribute("allReservations", reservations);

        return "supervisorProfile";
    }

    // RETRIEVE ONE CAKE SHOP
    @RequestMapping(method = RequestMethod.GET, value = "/{reservationId}")
    ResponseEntity<?> getReservation(@PathVariable Long reservationId) {

        Optional<Reservation> reservation = this.reservationRepository.findById(reservationId);
        if (!reservation.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Optional<Reservation>>(reservation, HttpStatus.OK);
    }

    //CREATE NEW RESERVATION
    @RequestMapping(method = RequestMethod.GET, value = "{currentUsrId}/{hotel_id}/add")
    public String findUserbyName(@RequestParam Long currentUsrId, @RequestParam Long hotel_id, Model model){

        List<Reservation> reservations = this.reservationRepository.findAll();
        boolean exists = false;
        String message = "Done!";

        for(int i = 0; i < reservations.size(); i++){
            if(reservations.get(i).getHotel().getId().equals(hotel_id) && reservations.get(i).getUsers().getId().equals(currentUsrId)){
                exists = true;
                message = "You have already booked this hotel!";
                model.addAttribute("message", message);
            }
        }

        Optional<Users> users = usersRepository.findById(currentUsrId);
        Optional<Hotel> hotel = hotelRepository.findById(hotel_id);

        if (!exists && users.isPresent() && hotel.isPresent()){
            reservationRepository.save(new Reservation(users.get(), hotel.get()));
        }


        return "redirect:/users/"+currentUsrId+"/reserve/" + message;
    }


    //DELETE ONE)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteReservation(@PathVariable("id") long id) {

        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (!reservation.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        reservationRepository.deleteById(id);

        return new ResponseEntity<Reservation>(HttpStatus.NO_CONTENT);
    }

}