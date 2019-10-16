package com.stylefeng.guns.rest.common.persistence.model;

import java.io.Serializable;

public class Single implements Serializable {

    private int seatId;
    private int row;
    private int column;
    public void setSeatId(int seatId) {
         this.seatId = seatId;
     }
     public int getSeatId() {
         return seatId;
     }

    public void setRow(int row) {
         this.row = row;
     }
     public int getRow() {
         return row;
     }

    public void setColumn(int column) {
         this.column = column;
     }
     public int getColumn() {
         return column;
     }

}
