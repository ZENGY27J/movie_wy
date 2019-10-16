package com.wuyan.film.vo;

import java.io.Serializable;
import java.util.List;

public class ActorInfos implements Serializable {
    private List actors;
    private ActorMsg director;

    public List getActors() {
        return actors;
    }

    public void setActors(List actors) {
        this.actors = actors;
    }

    public ActorMsg getDirector() {
        return director;
    }

    public void setDirector(ActorMsg director) {
        this.director = director;
    }
}
