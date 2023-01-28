package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SeatsController {
    private Room room;
    private SeatsController() {
        this.room = getRoom();
    }
    public Room getRoom() {
        int totalRows = 9, totalColumns = 9;
        List<Seat> seats = new ArrayList<>();
        for(int i = 1; i <= totalRows; i++) {
            for(int j = 1; j <= totalColumns; j++) {
                seats.add(new Seat(i, j));
            }
        }
        return new Room(totalRows, totalColumns, seats);
    }

    @GetMapping("/seats")
    public Room getSeats() {
        return getRoom();
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat seat) {
        if(seat.getRow() > room.getTotal_rows()
                || seat.getColumn() > room.getTotal_columns()
                || seat.getRow() < 1
                || seat.getColumn() < 1) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"),
                    HttpStatus.BAD_REQUEST);
        }
        for(int i = 0; i < room.getAvailable_seats().size(); i++){
            Seat s = room.getAvailable_seats().get(i);
            if(s.equals(seat)) {
                OrderedSeat os = new OrderedSeat(UUID.randomUUID(), s);
                room.getOrdered_seats().add(os);
                room.getAvailable_seats().remove(i);
                return new ResponseEntity<>(os, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<?> refundSeat(@RequestBody Token token) {
        for(int i = 0; i < room.getOrdered_seats().size(); i++) {
            OrderedSeat os = room.getOrdered_seats().get(i);
            if(token.getToken().equals(os.getToken())) {
                room.getOrdered_seats().remove(os);
                room.getAvailable_seats().add(os.getTicket());
                return new ResponseEntity<>(Map.of("returned_ticket", os.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) String password) {
        if(password == null || !password.equals("super_secret")){
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.resolve(401));
        }
        Map<String, Integer> stats = new HashMap<>();
        int income = 0;
        for(int i = 0; i < room.getOrdered_seats().size(); i++) {
            income = income + room.getOrdered_seats().get(i).getTicket().getPrice();
        }
        int available = room.getAvailable_seats().size();
        int purchased = room.getOrdered_seats().size();
        stats.put("current_income", income);
        stats.put("number_of_available_seats", available);
        stats.put("number_of_purchased_tickets", purchased);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
