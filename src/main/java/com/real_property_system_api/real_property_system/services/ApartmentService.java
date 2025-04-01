package com.real_property_system_api.real_property_system.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.Apartment;
import com.real_property_system_api.real_property_system.models.LandPlot;
import com.real_property_system_api.real_property_system.repos.ApartmentRepository;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    public List<Apartment> findAll() {
        return (List<Apartment>) apartmentRepository.findAll();
    }

    public Page<Apartment> findAllPaged(Pageable pageable) {
        return apartmentRepository.findAll(pageable);
    }

    public Apartment findById(Long id) {
        return apartmentRepository.findById(id).orElse(null);
    }

    public List<Apartment> findByNumberOfRooms(int numberOfRooms) {
        return apartmentRepository.findByApartmentRooms(numberOfRooms);
    }

    public Optional<Apartment> findByPropertyId(Long propId)
    {
        return apartmentRepository.findByPropertyPropId(propId);
    }

    public boolean doesRecordWithPropertyIdExists(Long propId)
    {
        return findByPropertyId(propId).isPresent();
    }

    public Apartment save(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public void deleteById(Long id) {
        apartmentRepository.deleteById(id);
    }
}
