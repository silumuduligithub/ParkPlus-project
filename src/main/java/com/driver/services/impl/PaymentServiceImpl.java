package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.driver.model.PaymentMode.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Optional<Reservation> reservationOptional = reservationRepository2.findById(reservationId);
        if(reservationOptional.isPresent() == false){
            throw new Exception("reservation id not found");
        }
        Reservation reservation = reservationOptional.get();
        mode = mode.toUpperCase();
        int cost = reservation.getNumberOfHours() * reservation.getSpot().getPricePerHour();
        if((!mode.equals("CARD")) && (!mode .equals("CASH")) && (!mode .equals("UPI"))){
            throw new Exception("Payment mode not detected");
        }
        if(amountSent < cost){
            throw new Exception("Insufficient Amount");
        }
        Payment payment = new Payment();
        if(mode.equals("cash"))
        {
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else if(mode.equals("card"))
        {
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else
        {
            payment.setPaymentMode(PaymentMode.UPI);
        }
        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);
        reservationRepository2.save(reservation);
        return payment;
    }
}
