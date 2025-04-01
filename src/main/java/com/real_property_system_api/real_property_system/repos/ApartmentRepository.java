package com.real_property_system_api.real_property_system.repos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.Apartment;
import com.real_property_system_api.real_property_system.models.Deal;
import com.real_property_system_api.real_property_system.models.LandPlot;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long> {
    // Find all apartments with sorting
    List<Apartment> findAll();


    Optional<Apartment> findById(Long id);

    Apartment save(Apartment apartment);
    void deleteById(Long id);

    // Custom query for filtering by number of rooms
    List<Apartment> findByApartmentRooms(int numberOfRooms);

    Optional<Apartment> findByPropertyPropId(Long id);

}
