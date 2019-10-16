package com.wuyan.film.vo;

import java.io.Serializable;
import java.util.Date;

public class FilmDetailResponseVO implements Serializable {
    private String filmEnName;
    private String filmId;
    private String filmName;
    private String imgAddress;
    private String info01;
    private String info02;
    private Date info03;
    private FilmMsg info04;
    private String score;
    private String scoreNum;
    private String totalBox;

    public String getFilmEnName() {
        return filmEnName;
    }

    public void setFilmEnName(String filmEnName) {
        this.filmEnName = filmEnName;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public String getInfo01() {
        return info01;
    }

    public void setInfo01(String info01) {
        this.info01 = info01;
    }

    public String getInfo02() {
        return info02;
    }

    public void setInfo02(String info02) {
        this.info02 = info02;
    }

    public Date getInfo03() {
        return info03;
    }

    public void setInfo03(Date info03) {
        this.info03 = info03;
    }

    public FilmMsg getInfo04() {
        return info04;
    }

    public void setInfo04(FilmMsg info04) {
        this.info04 = info04;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(String scoreNum) {
        this.scoreNum = scoreNum;
    }

    public String getTotalBox() {
        return totalBox;
    }

    public void setTotalBox(String totalBox) {
        this.totalBox = totalBox;
    }
}
