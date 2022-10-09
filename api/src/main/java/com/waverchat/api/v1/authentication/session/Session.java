package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.ApplicationEntity;
import com.waverchat.api.v1.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session extends ApplicationEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
