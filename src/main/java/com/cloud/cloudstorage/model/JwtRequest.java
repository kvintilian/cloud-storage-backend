package com.cloud.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6388606018396019780L;

    private String login;
    private String password;

}
