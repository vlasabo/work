package com.doctrine7.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity(name = "users")
@Slf4j
public class User {

    @Id
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "registered_at")
    private Timestamp registeredAt;
    @Column(name = "registration_attempts")
    private int registrationAttempts;
    @Column(name = "separated_shedule")
    private Boolean separatedShedule;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_employees", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "employee")
    private List<String> employees = new ArrayList<>();
    @Column(name = "banned")
    private boolean isBanned;
    @Column(name = "bot_banned")
    private boolean isBotBanned;
}
