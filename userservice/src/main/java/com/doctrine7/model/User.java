package com.doctrine7.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private boolean banned;
    @Column(name = "bot_banned")
    private boolean botBanned;
    @Column(name = "authenticated")
    private boolean authenticated;

    @Override
    public int hashCode() {
        return this.chatId.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this){
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final User other = (User) obj;
        return Objects.equals(this.chatId, other.chatId);
    }
}
