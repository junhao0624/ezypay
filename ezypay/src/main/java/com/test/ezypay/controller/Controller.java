package com.test.ezypay.controller;

import com.test.ezypay.constant.SubscriptionConstants;
import com.test.ezypay.model.SubscriptionRequest;
import com.test.ezypay.model.SubscriptionResponse;
import com.test.ezypay.repository.SubscriptionRepo;
import com.test.ezypay.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@RestController
public class Controller {

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping(value = "/create")
    public SubscriptionResponse createSubscription(@RequestBody SubscriptionRequest request) {
        SubscriptionResponse response = new SubscriptionResponse();

        try {
            validateRequest(request);
            response = subscriptionService.postSubscription(request);
            subscriptionRepo.save(request);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal Argument");

        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parse Exception");
        }

        return response;
    }

    public void validateRequest (SubscriptionRequest request) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        if (!SubscriptionConstants.isSubscriptionTypeValid(request.getSubType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subscription Type is not available");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            Date startDate = sdf.parse(request.getStartDate());
            Date endDate = sdf.parse(request.getEndDate());

            if (endDate.compareTo(startDate) > 0) {
                long seconds =  (endDate.getTime() - startDate.getTime())/1000;
                int days = (int) TimeUnit.SECONDS.toDays(seconds);

                if (days > SubscriptionConstants.DATE_MAX_RANGE){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date range must be less than 3 months");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End Date must be greater than Start Date");
            }
        }
    }
}
