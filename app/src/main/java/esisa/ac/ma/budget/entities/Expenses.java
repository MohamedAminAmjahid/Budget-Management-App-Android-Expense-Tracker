package esisa.ac.ma.budget.entities;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Expenses implements Serializable {
    private int id;
    private String label;
    private float price;
    private String date;
}
