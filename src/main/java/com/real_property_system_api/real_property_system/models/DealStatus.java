package com.real_property_system_api.real_property_system.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rps_deal_statuses")
@Getter
@Setter
public class DealStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("status_id")
    private Long statusId;

    @Column(name = "status_name", nullable = false)
    @JsonProperty("status_name")
    private String statusPrettyName;

    @Column(name = "status_type")
    @JsonProperty("status_type")
    private Integer statusTechType;

    public static final String
        NAME_CLOSED = "Закрыта",
        NAME_IN_DEAL = "Сделка уже ведется",
        NAME_ASSIGN = "В ожидании назначения специалиста",
        NAME_DOCS_NEEDED = "В ожидании документов",
        NAME_DOCS_CHECK = "Проверка документов",
        NAME_ACCEPT_STAT = "В ожидании одобрения";

    public static final int
         STATUS_CLOSED = 0,
         STATUS_IN_DEAL = 1,
         STATUS_WAIT_FOR_REALTOR_ASSIGN = 2,
         STATUS_DOCS_NEEDED_BY_USER = 3,
         STATUS_DOCS_CHECK = 4,
         STATUS_WAIT_FOR_ACCEPT = 5;
}
