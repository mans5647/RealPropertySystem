package com.real_property_system_api.real_property_system.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.bodies.SingleValue;
import com.real_property_system_api.real_property_system.models.Deal;
import com.real_property_system_api.real_property_system.models.DealStatus;
import com.real_property_system_api.real_property_system.models.DealType;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.repos.DealStatusRepository;
import com.real_property_system_api.real_property_system.services.DealService;
import com.real_property_system_api.real_property_system.services.DealStatusService;
import com.real_property_system_api.real_property_system.services.DealTypeService;
import com.real_property_system_api.real_property_system.services.RealPropertyService;
import com.real_property_system_api.real_property_system.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

@RestController
@RequestMapping("/api/business/deals")
public class DealController 
{
    @Autowired
    private DealTypeService dealTypeService;

    @Autowired
    private UserService userService;    

    @Autowired
    private DealService dealService;

    @Autowired
    private DealStatusRepository dealStatusRepository;

    @Autowired
    private DealStatusService dealStatusService;

    @Autowired
    private RealPropertyService realPropertyService;


    @PostMapping("/add_new_deal")
    public Deal addNewDeal(@RequestBody Deal deal)
    {
        return dealService.add(deal);
    }

    @PutMapping("/update_deal/{id}")
    public Deal updateDeal(@PathVariable("id") Long id, @RequestBody Deal body)
    {
        var value = dealService.findById(id);
        
        if (value != null)
        {
            return dealService.update(body);
        }
        
        return null;
    }


    @GetMapping("/check_by_property")
    public String checkStatusOfDeal(@RequestParam("prop_id") Long propId, HttpServletResponse resp)
    {
        var value = realPropertyService.findById(propId);
        
        if (value != null)
        {
            var deal = dealService.findByProperty(value);
            
            if (deal != null)
            {
                if (dealService.isDealAlreadyBegan(deal))
                {
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    return "";
                }
            }

        }
        
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "";
    }


    @GetMapping("/all")
    public List<Deal> fetchAllDeals(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        var pageContent = dealService.findAllPaged(pageable);

        
        if (pageContent.hasContent()) return pageContent.getContent();

        return Collections.emptyList();
    }

    @GetMapping("/all_by_login")
    public List<Deal> fetchByLoginPaged(
        @RequestParam(name = "login", required = true) String login,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5")  int size)
    {
        
        User u = userService.getUserByLogin(login).get();
        
        Pageable pageable = PageRequest.of(page, size);

        var pageContent = dealService.findAllPaged(pageable, u);
        
        if (pageContent.hasContent()) return pageContent.getContent();

        return Collections.emptyList();

    }

    @GetMapping("/count")
    public SingleValue<Integer> getTotalCount()
    {
        Pageable pageable = PageRequest.of(0, 5);

        var unusedContent = dealService.findAllPaged(pageable);


        return SingleValue.of(unusedContent.getTotalPages());

    }

    @GetMapping("/types")
    public List<DealType> fetchAllDealTypes()
    {
        return dealTypeService.findAll();
    }

    
    @PostMapping("/types/add_if_not_exist")
    public DealType saveTypeByTypeTechType(@RequestParam("tech_id") Integer techId)
    {
        if (dealTypeService.existsByTechType(techId))
        {
            return dealTypeService.findByTechType(techId);
        }
        
        int i = techId.intValue();

        DealType dealType = new DealType();
        switch (i)
        {
            case DealType.TYPE_TECH_BUY:
                dealType.setTypeName(DealType.TYPE_BUY);
                dealType.setTypeTech(DealType.TYPE_TECH_BUY);
                break;
            case DealType.TYPE_TECH_RENT:
                dealType.setTypeName(DealType.TYPE_RENT);
                dealType.setTypeTech(DealType.TYPE_TECH_RENT);
                break;
        }

        return dealTypeService.save(dealType);
    }

    @PostMapping("/stats/add_if_not_exist")
    public DealStatus saveStatusByStatusType(@RequestParam("tech_id") Integer techId)
    {
        if (dealStatusService.existsByStatusType(techId))
        {
            return dealStatusService.findByStatus(techId);
        }
        
        int i = techId.intValue();

        DealStatus status = new DealStatus();
        status.setStatusTechType(techId);
        
        switch (i)
        {
            case DealStatus.STATUS_CLOSED:
            status.setStatusPrettyName(DealStatus.NAME_CLOSED);
            break;
        
            case DealStatus.STATUS_DOCS_CHECK:
            status.setStatusPrettyName(DealStatus.NAME_DOCS_CHECK);
            break;
            
            case DealStatus.STATUS_DOCS_NEEDED_BY_USER:
            status.setStatusPrettyName(DealStatus.NAME_DOCS_NEEDED);
            break;

            case DealStatus.STATUS_IN_DEAL:
            status.setStatusPrettyName(DealStatus.NAME_IN_DEAL);
            break;
            case DealStatus.STATUS_WAIT_FOR_REALTOR_ASSIGN:

            status.setStatusPrettyName(DealStatus.NAME_ASSIGN);
            break;
            case DealStatus.STATUS_WAIT_FOR_ACCEPT:
            status.setStatusPrettyName(DealStatus.NAME_ACCEPT_STAT);
            break;

        }

        return dealStatusService.add(status);
    }

    @GetMapping("/stats")
    public List<DealStatus> fetchAllStatuses()
    {
        return dealStatusRepository.findAll();
    }

    @GetMapping("/stats/find_by_tech_id")
    public DealStatus findStatusByTechId(@RequestParam("tech_id") Integer techId)
    {
        return _addStatusIfNotExist(techId);
    }

    @GetMapping("/types/find_by_tech_id")
    public DealType findTypeByTechId(@RequestParam("tech_id") Integer techId)
    {
        return _addTypeIfNotExist(techId);
    }

    public DealStatus _addStatusIfNotExist(Integer techId)
    {
        if (dealStatusService.existsByStatusType(techId))
        {
            return dealStatusService.findByStatus(techId);
        }
        
        int i = techId.intValue();

        DealStatus status = new DealStatus();
        status.setStatusTechType(techId);
        
        switch (i)
        {
            case DealStatus.STATUS_CLOSED:
            status.setStatusPrettyName(DealStatus.NAME_CLOSED);
            break;
        
            case DealStatus.STATUS_DOCS_CHECK:
            status.setStatusPrettyName(DealStatus.NAME_DOCS_CHECK);
            break;
            
            case DealStatus.STATUS_DOCS_NEEDED_BY_USER:
            status.setStatusPrettyName(DealStatus.NAME_DOCS_NEEDED);
            break;

            case DealStatus.STATUS_IN_DEAL:
            status.setStatusPrettyName(DealStatus.NAME_IN_DEAL);
            break;
            case DealStatus.STATUS_WAIT_FOR_REALTOR_ASSIGN:

            status.setStatusPrettyName(DealStatus.NAME_ASSIGN);
            break;
            case DealStatus.STATUS_WAIT_FOR_ACCEPT:
            status.setStatusPrettyName(DealStatus.NAME_ACCEPT_STAT);
            break;

        }

        return dealStatusService.add(status);
    }

    public DealType _addTypeIfNotExist(Integer techId)
    {
        if (dealTypeService.existsByTechType(techId))
        {
            return dealTypeService.findByTechType(techId);
        }
        
        int i = techId.intValue();

        DealType dealType = new DealType();
        switch (i)
        {
            case DealType.TYPE_TECH_BUY:
                dealType.setTypeName(DealType.TYPE_BUY);
                dealType.setTypeTech(DealType.TYPE_TECH_BUY);
                break;
            case DealType.TYPE_TECH_RENT:
                dealType.setTypeName(DealType.TYPE_RENT);
                dealType.setTypeTech(DealType.TYPE_TECH_RENT);
                break;
        }

        return dealTypeService.save(dealType);
    }
}
