package esisa.ac.ma.budget.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int id;
    private String mail;
    private String password;
}
