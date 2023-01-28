package cinema;

public class Seat {
    private int row;
    private int column;
    private int price;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.price = row > 4 ? 8 : 10;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(getClass() != o.getClass() || o == null) {
            return false;
        }
        Seat s = (Seat) o;
        if(s.getColumn() == this.column && s.getRow() == this.row) {
            return true;
        }
        return false;
    }
}
