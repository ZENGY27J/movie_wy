package com.wuyan.film.service;

import com.wuyan.film.vo.*;

import java.util.List;

public interface FilmModuleService {
    List<ActorMsg> getAllActors(Integer id);

    ActorMsg getDirector(Integer id);

    FilmDetailResponseVO getFilmDetail(Integer id);

    BaseRespVO getFilmsByShowType(FilmQueryRequestVO filmQueryRequestVO);

    FilmQueryResult getFilmsByKeyword(String kw);
}
