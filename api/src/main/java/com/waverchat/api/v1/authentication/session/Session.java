package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.applicationresource.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session extends AbstractApplicationEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
