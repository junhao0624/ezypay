package com.test.ezypay.services;

import com.test.ezypay.constant.SubscriptionConstants;
import com.test.ezypay.model.SubscriptionRequest;
import com.test.ezypay.model.SubscriptionResponse;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SubscriptionResponse postSubscription (SubscriptionRequest request) {

        String subscriptionType = request.getSubType();

        String subscriptionInput = request.getSubInput();

        String startDate = request.getStartDate();

        String endDate = request.getEndDate();

        List<String> invoiceDateList = generateInvoiceDate(subscriptionType, subscriptionInput, startDate, endDate);

        SubscriptionResponse response = new SubscriptionResponse();

        response.setSubscriptionType(subscriptionType);
        response.setSubscriptionPrice(request.getSubPrice());
        response.setInvoiceDate(invoiceDateList);

        return response;
    }

    private List<String> generateInvoiceDate(String subscriptionType, String subscriptionInput, String startDate, String endDate) {
        LocalDate fromDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate toDate = LocalDate.parse(endDate, dateTimeFormatter);

        List<String> invoicesDate = new ArrayList<>();

        if (subscriptionType.equals(SubscriptionConstants.SubscriptionType.DAILY.name())) {
            invoicesDate = processDailySubscription(invoicesDate, fromDate, toDate);
        } else if (subscriptionType.equals(SubscriptionConstants.SubscriptionType.WEEKLY.name())) {
            invoicesDate = processWeeklySubscription(invoicesDate, subscriptionInput, fromDate, toDate);
        } else {
            invoicesDate = processMonthlySubscription(invoicesDate, subscriptionInput, fromDate, toDate);
        }

        return invoicesDate;
    }

    private List<String> processDailySubscription (List<String> invoicesDate, LocalDate fromDate, LocalDate toDate) {

        while (toDate.compareTo(fromDate) >= 0) {
            invoicesDate.add(fromDate.format(dateTimeFormatter));
            fromDate = fromDate.plusDays(1);
        }

        return invoicesDate;
    }

    private List<String> processWeeklySubscription (List<String> invoicesDate, String subscriptionInput, LocalDate fromDate, LocalDate toDate) {

        DayOfWeek dayOfWeek = null;

        switch (subscriptionInput) {
            case "MONDAY" :
                dayOfWeek = DayOfWeek.MONDAY;
                break;
            case "TUESDAY" :
                dayOfWeek = DayOfWeek.TUESDAY;
                break;
            case "WEDNESDAY" :
                dayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case "THURSDAY" :
                dayOfWeek = DayOfWeek.THURSDAY;
                break;
            case "FRIDAY" :
                dayOfWeek = DayOfWeek.FRIDAY;
                break;
            case "SATURDAY" :
                dayOfWeek = DayOfWeek.SATURDAY;
                break;
            case "SUNDAY" :
                dayOfWeek = DayOfWeek.SUNDAY;
                break;
        }

        while (toDate.compareTo(fromDate) >= 0) {
            if (fromDate.getDayOfWeek().equals(dayOfWeek)) {
                invoicesDate.add(fromDate.format(dateTimeFormatter));
                fromDate = fromDate.plusWeeks(1);
            } else {
                fromDate = fromDate.plusDays(1);
            }
        }
        return invoicesDate;
    }

    private List<String> processMonthlySubscription (List<String> invoicesDate, String subscriptionInput, LocalDate fromDate, LocalDate toDate) {
        int dateOfMonth = Integer.parseInt(subscriptionInput);

        while (toDate.compareTo(fromDate) >= 0) {
            if (fromDate.getDayOfMonth() ==  dateOfMonth) {
                invoicesDate.add(fromDate.withDayOfMonth(dateOfMonth).format(dateTimeFormatter));
                fromDate = fromDate.plusMonths(1);
            } else {
                fromDate = fromDate.plusDays(1);
            }
        }
        return invoicesDate;
    }
}
