package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int total_rows;
    private int total_columns;
    private List<Seat> available_seats;
    @JsonIgnore
    private List<OrderedSeat> ordered_seats;

    public Room(int total_rows, int total_columns, List<Seat> available_seats) {
        this.total_rows = total_rows;
        this.total_columns = total_columns;
        this.available_seats = available_seats;
        this.ordered_seats = new ArrayList<>();
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }

    public List<Seat> getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(List<Seat> available_seats) {
        this.available_seats = available_seats;
    }

    public List<OrderedSeat> getOrdered_seats() {
        return ordered_seats;
    }

    public void setOrdered_seats(List<OrderedSeat> ordered_seats) {
        this.ordered_seats = ordered_seats;
    }
}
