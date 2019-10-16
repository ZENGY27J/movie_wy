package com.wuyan.film.vo;

import java.io.Serializable;
import java.util.Map;

public class FilmMsg implements Serializable {
    private String biopgraphy;
    private String filmId;
    private Map imgVO;
    private ActorInfos actors;

    public String getBiopgraphy() {
        return biopgraphy;
    }

    public void setBiopgraphy(String biopgraphy) {
        this.biopgraphy = biopgraphy;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public Map getImgVO() {
        return imgVO;
    }

    public void setImgVO(Map imgVO) {
        this.imgVO = imgVO;
    }

    public ActorInfos getActors() {
        return actors;
    }

    public void setActors(ActorInfos actors) {
        this.actors = actors;
    }
}
