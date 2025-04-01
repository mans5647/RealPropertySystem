package com.real_property_system_api.real_property_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.Deal;
import com.real_property_system_api.real_property_system.models.DealStatus;
import com.real_property_system_api.real_property_system.models.RealProperty;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.repos.DealRepository;

@Service
public class DealService {

    @Autowired
    private DealRepository dealRepository;

    public List<Deal> findAll() {
        return dealRepository.findAll();
    }

    public Page<Deal> findAllPaged(Pageable pageable) {
        return dealRepository.findAll(pageable);
    }


    public Page<Deal> findAllPaged(Pageable pageable, User user)
    {
        return dealRepository.findAllByBuyer(pageable, user);
    }

    public Deal findById(Long id) {
        return dealRepository.findById(id).orElse(null);
    }

    

    public void delete(Long id) {
        dealRepository.deleteById(id);
    }

    public Deal update(Deal deal)
    {
        return dealRepository.save(deal);
    }

    public Deal add(Deal deal)
    {
        return dealRepository.save(deal);
    }

    public Deal findByProperty(RealProperty realProperty)
    {
        return dealRepository.findByProperty(realProperty).orElse(null);
    }

    public boolean isDealFinished(Deal deal)
    {
        return deal.getDealStatus().getStatusTechType().intValue() == DealStatus.STATUS_CLOSED;
    }

    public boolean isDealAlreadyBegan(Deal deal)
    {
        return deal.getDealStatus().getStatusTechType().intValue() == DealStatus.STATUS_IN_DEAL;
    }

    public User getBuyerFromDeal(Deal deal)
    {
        return deal.getBuyer();
    }

    public User getSellerFromDeal(Deal deal)
    {
        return deal.getProperty().getSeller();
    }

    public Long countAll()
    {
        return dealRepository.countAll();
    }

}